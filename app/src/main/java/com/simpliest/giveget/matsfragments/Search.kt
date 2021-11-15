package com.simpliest.giveget.matsfragments

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.simpliest.giveget.*
import com.simpliest.giveget.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.marker_popup.view.*
import org.w3c.dom.Comment
import java.io.File

class Search : Fragment(R.layout.fragment_dashboard) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>?= null
    lateinit var adapter: ArrayAdapter<*>
    var markList = ArrayList<Marker>()


    private lateinit var database: DatabaseReference
    lateinit var currentUsername : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        SavedInstance: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_search, container, false)


        val tittelList: MutableList<String> = ArrayList()
        val descList: MutableList<String> = ArrayList()


        database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

        adapter = ArrayAdapter<String>(this.requireContext(), android.R.layout.simple_list_item_1, tittelList)

        var childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildAdded:" + dataSnapshot.key!!)


                /*val add_title = dataSnapshot.child("tittel").value.toString()
                val add_desc = dataSnapshot.child("beskrivelse").value.toString()*/
                val add_title = dataSnapshot.child("tittel").value.toString()
                val add_descr = dataSnapshot.child("beskrivelse").value.toString()
                val add_type = dataSnapshot.child("kategorier").value.toString()
                val add_lat = dataSnapshot.child("lat").value as Double
                val add_long = dataSnapshot.child("long").value as Double
                val brukerID = dataSnapshot.child("brukerID").value.toString()
                val brukernavn = dataSnapshot.child("brukernavn").value.toString()
                val marker = Marker(add_lat, add_long, add_title, add_descr, brukerID, brukernavn,add_type)
                markList += marker

                tittelList.add(add_title)
                descList.add(add_descr)

                val searchList = v.findViewById<ListView>(R.id.search_list)
                searchList.adapter = adapter
                searchList.isVisible = false

                //val searchView = v.findViewById<SearchView>(R.id.search_view)

                // søke-algoritme
                v.search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchList.isVisible = v.search_view.hasFocus()
                        if (tittelList.contains(query) || descList.contains(query)) {
                            adapter.filter.filter(query)
                        }
                        else {
                            searchList.isVisible = false
                            Toast.makeText(context, "Ingen treff funnet", Toast.LENGTH_LONG).show()
                        }
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        searchList.isVisible = v.search_view.hasFocus()

                        adapter.filter.filter(newText)
                        return false
                    }
                })


                searchList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItemText = parent.getItemAtPosition(position)

                    for(i in markList.indices) {
                        if(markList[i].title == selectedItemText || markList[i].desc == selectedItemText) {
                            Toast.makeText(context, "lat: " + markList[i].lat, Toast.LENGTH_SHORT).show()
                            val fragment = MapsFragment()
                            val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                            fm.beginTransaction().replace(R.id.secondLayout, fragment).commit()
                            fragment.callback = OnMapReadyCallback {googleMap ->
                                val newMarker = LatLng(markList[i].lat, markList[i].long)
                                googleMap.addMarker(MarkerOptions().position(newMarker).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(newMarker))

                                googleMap!!.setOnMarkerClickListener { marker ->
                                    val popupDialog = LayoutInflater.from(fragment.context).inflate(R.layout.marker_popup, null)
                                    val builder = AlertDialog.Builder(fragment.context).setView(popupDialog)
                                    builder.setTitle(markList[i].title)
                                    builder.setMessage(markList[i].desc)

                                    //Henter profilbilde fra firebase storage
                                    val storageRef = FirebaseStorage.getInstance().reference.child("image/${markList[i].uid}")
                                    val localfile = File.createTempFile("tempImage", "jpg")
                                    storageRef.getFile(localfile).addOnSuccessListener {
                                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                        popupDialog.pbView.setImageBitmap(bitmap)
                                    }

                                    val dialog = builder.show()

                                    popupDialog.popup_btn.setOnClickListener {
                                        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                                        database = FirebaseDatabase.getInstance().getReference("Samtaler")

                                        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid.toString()).get().addOnSuccessListener {
                                            val samtale = MapsFragment.Samtale(
                                                markList[i].uname,
                                                it.child("username").value.toString(),
                                                markList[i].title
                                            )

                                            database.addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {


                                                    for (postSnapshot in dataSnapshot.children) {
                                                        if (postSnapshot.child("add_eier").value.toString() == samtale.add_eier &&
                                                                postSnapshot.child("annonse_tittel").value.toString() == samtale.annonse_tittel &&
                                                                postSnapshot.child("kontakteren").value.toString() == samtale.kontakteren) {

                                                            return
                                                        }
                                                    }

                                                    database.push().setValue(samtale).addOnSuccessListener {
                                                        dialog.dismiss()
                                                        val fragment2 = chatFragment(markList[i].uname, markList[i].title)
                                                        //val fm: FragmentManager = (fragment.context as AppCompatActivity).supportFragmentManager
                                                        fm.beginTransaction().replace(R.id.secondLayout, fragment2).commit()
                                                    }.addOnFailureListener {
                                                        Toast.makeText(context, "Noe gikk galt når du prøvde å starte samtale med " + samtale.add_eier, Toast.LENGTH_LONG)
                                                    }

                                                }

                                                override fun onCancelled(databseError: DatabaseError) {
                                                    TODO("Not yet implemented")
                                                }


                                            })

                                        }
                                    }

                                    true
                                }
                            }

                        }
                    }

                    /*val fragment = MapsFragment()
                    fragment.callback = OnMapReadyCallback {googleMap ->

                    }
                    val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                    fm.beginTransaction().replace(R.id.secondLayout, fragment).commit()*/
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.", Toast.LENGTH_SHORT).show()
            }
        }
        database.addChildEventListener(childEventListener)


        return v
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

}