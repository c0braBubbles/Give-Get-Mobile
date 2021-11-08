package com.simpliest.giveget.matsfragments

import android.app.AlertDialog
import android.content.ContentValues
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.simpliest.giveget.*
import com.simpliest.giveget.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.marker_popup.view.*
import org.w3c.dom.Comment

class Search : Fragment(R.layout.fragment_dashboard) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>?= null
    lateinit var adapter: ArrayAdapter<*>


    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        SavedInstance: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_search, container, false)


        //val search_bar = search_view

        val tittelList: MutableList<String> = ArrayList()
        val descList: MutableList<String> = ArrayList()

        database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

        var childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildAdded:" + dataSnapshot.key!!)


                val add_title = dataSnapshot.child("tittel").value.toString()
                val add_desc = dataSnapshot.child("beskrivelse").value.toString()


                tittelList.add(add_title)
                descList.add(add_desc)


                adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, tittelList)
                val searchList = v.findViewById<ListView>(R.id.search_list)
                searchList.adapter = adapter
                /*searchList.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    //adapter = RecyclerAdapter(tittelList, descList)
                    adapter = adapter
                }*/
                //searchList.visibility = View.INVISIBLE


                // søke-algoritme
                search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        if (tittelList.contains(query) || descList.contains(query)) {
                            adapter.filter.filter(query)
                        }
                        else {
                            Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                        }
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })
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
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }


        }
        database.addChildEventListener(childEventListener)


        return v
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

}