package com.simpliest.giveget.matsfragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.simpliest.giveget.*
import com.simpliest.giveget.R
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.marker_popup.*
import kotlinx.android.synthetic.main.marker_popup.view.*
import org.w3c.dom.Comment
import java.io.File

class MapsFragment : Fragment() {

    private lateinit var database: DatabaseReference

    public var callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        var markList = ArrayList<Marker>()
        database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                val add_title = dataSnapshot.child("tittel").value.toString()
                val add_descr = dataSnapshot.child("beskrivelse").value.toString()
                val add_lat = dataSnapshot.child("lat").value as Double
                val add_long = dataSnapshot.child("long").value as Double
                val brukerID = dataSnapshot.child("brukerID").value.toString()
                val brukernavn = dataSnapshot.child("brukernavn").value.toString()
                val marker = Marker(add_lat, add_long, add_title, add_descr, brukerID, brukernavn)

                markList += marker


                for(i in markList.indices) {
                    val gmark = googleMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                markList[i].lat,
                                markList[i].long
                            )
                        ).title(markList[i].desc + "\n \n"  + markList[i].uname)
                    )
                    gmark.snippet = markList[i].uid
                    gmark.tag = markList[i].title
                }


                googleMap!!.setOnMarkerClickListener { marker ->
                    val popupDialog = LayoutInflater.from(context).inflate(R.layout.marker_popup, null)
                    val aBuilder = AlertDialog.Builder(context).setView(popupDialog)
                    aBuilder.setTitle("${marker.tag}")
                    aBuilder.setMessage(marker.title)

                    //Henter profilbilde fra firebase storage
                    val storageRef = FirebaseStorage.getInstance().reference.child("image/${marker.snippet}")
                    val localfile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        popupDialog.pbView.setImageBitmap(bitmap)

                    }
                    //popupDialog.userView.setText()
                    val aDialog = aBuilder.show()

                    popupDialog.popup_btn.setOnClickListener{
                        /*
                            HER SKAL DET STARTES NY SAMTALE OG FLYTTES TIL CHATFRAGMENT.
                            MÅ SNAKKE MED JACOB OM HVORDAN
                        */
                        Toast.makeText(context, brukerID, Toast.LENGTH_SHORT)
                            .show()

                        aDialog.dismiss()

                        val fragment = chatFragment("bernt", marker.tag.toString())
                        val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                        fm.beginTransaction().replace(R.id.secondLayout, fragment).commit()
                    }

                    true
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.addChildEventListener(childEventListener)


        /*val sydney = LatLng(59.148066, 9.692892)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //tester markør-onclick DET FUNKET
        googleMap.setOnMarkerClickListener { sydney ->
            Toast.makeText(context, "onclick på markør test",
                Toast.LENGTH_SHORT).show()
            true
        }*/
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}