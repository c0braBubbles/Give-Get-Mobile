package com.simpliest.giveget

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_annonser.*
import kotlinx.android.synthetic.main.activity_main2.*
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_annonser.view.*
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.card_layout.*
import java.io.File


class Annonser_fragment : Fragment() {

    private lateinit var database: DatabaseReference
    val brukerID = FirebaseAuth.getInstance().currentUser!!.uid
    val tittelList: MutableList<String> = ArrayList()
    val beskList: MutableList<String> = ArrayList()
    val idList: MutableList<String> = ArrayList()

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

        //Åpne ny fragment ved buttonclick på "pluss"
        val bt = v.findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        bt.setOnClickListener {
            val secondFragment = NyAnnonse_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.secondLayout, secondFragment)
            transaction.commit()
        }

        //Firebase, henting av annonser++
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
                    idList.add(annonseid)
                }
                // RecyclerView adapter og layout, kaller konstruktør og tar to lister som input
                val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = RecyclerAdapter(tittelList, beskList)

                }
                //Henter annonsebilde fra firebase storage
                val storageRef = FirebaseStorage.getInstance().reference.child("image/$annonseid")
                val localfile = File.createTempFile("tempImage", "jpg")
                storageRef.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    item_image.setImageBitmap(bitmap)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + snapshot.key!!)
                val annonseid = snapshot.child("id").value.toString()
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = snapshot.key
                if(commentKey == annonseid) {
                    tittelList.clear()
                    beskList.clear()
                }

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Swipe for å slette annonser
         val swipeHandler = object : SwipeToDeleteCallback(this.context) {
             override fun onMove(
                 recyclerView: RecyclerView,
                 viewHolder: RecyclerView.ViewHolder,
                 target: RecyclerView.ViewHolder
             ): Boolean {
                 TODO("Not yet implemented")
             }
             /* Henter id listen, sjekker positionen til den elementet som ble swipet (viewHolder.adapterPosition)
             og gir oss iden til annonsen som blir swipet, deretter henter vi annonsen med denne "keyen" */
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")
                 val idAnnonse = idList.get(viewHolder.adapterPosition)
                 val tittelAnnonse = tittelList.get(viewHolder.adapterPosition)
                 Toast.makeText(context, "Annonse med Tittel: " +tittelAnnonse+ " ble slettet.", Toast.LENGTH_SHORT).show()
                 database.child(idAnnonse).setValue(null).addOnSuccessListener {
                     //Clearer arraylistene for gamle verdier før vi refresher
                     tittelList.clear()
                     beskList.clear()
                     idList.clear()
                     //Navigerer til samme fragment på nytt, for å "refreshe"
                     val secondFragment = Annonser_fragment()
                     val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
                     transaction.replace(R.id.secondLayout, secondFragment)
                     transaction.commit()
                 }
             }
         }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        fragmentManager
    }

}