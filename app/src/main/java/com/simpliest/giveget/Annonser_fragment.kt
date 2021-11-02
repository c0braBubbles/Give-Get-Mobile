package com.simpliest.giveget

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_annonser.*
import kotlinx.android.synthetic.main.activity_main2.*
import android.os.Build
import android.widget.HorizontalScrollView
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_annonser.view.*


class Annonser_fragment : Fragment() {

    private lateinit var database: DatabaseReference
    val brukerID = FirebaseAuth.getInstance().currentUser!!.uid
    val tittelList: MutableList<String> = ArrayList()
    val beskList: MutableList<String> = ArrayList()

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.activity_annonser, container, false)

        val bt = v.findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        bt.setOnClickListener {
            val secondFragment = NyAnnonse_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.secondLayout, secondFragment)
            transaction.commit()
        }

        //Firebase
        tittelList.clear()
        beskList.clear()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val title = snapshot.child("tittel").value.toString()
                val description = snapshot.child("beskrivelse").value.toString()
                val brukeridA = snapshot.child("brukerID").value.toString()
                val annonseid = snapshot.child("id").value.toString()
                if(brukeridA == brukerID) { //adder data fra db til to lister
                    tittelList.add(title)
                    beskList.add(description)
                }
                // RecyclerView adapter og layout, kaller konstruktør og tar to lister som input
                val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = RecyclerAdapter(tittelList, beskList)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        FirebaseDatabase.getInstance().getReference("AnnonseAndroid").addChildEventListener(childEventListener)

        return v

    }

}