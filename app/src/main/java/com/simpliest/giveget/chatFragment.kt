package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class chatFragment(val samtalePartner: String, val annonseNavn: String) : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MsgRecyclerAdapter.ViewHolder>?= null

    var sendList: MutableList<String> = ArrayList()
    var receiveList: MutableList<String> = ArrayList()

    //val listenMin: MutableList<String> = ArrayList()

    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(this.context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        sendList.add("Hei1")
        receiveList.add("")

        sendList.add("Hei2")
        receiveList.add("")

        receiveList.add("Heisan")
        sendList.add("")

        sendList.add("Hei3")
        receiveList.add("")


        val v = inflater.inflate(R.layout.chat_fragment, container, false)

        val tf = v.findViewById<TextView>(R.id.chat_info)
        tf.text = samtalePartner +"\n" + annonseNavn

        val rView = v.findViewById<RecyclerView>(R.id.msg_list)
        rView.layoutManager = layoutManager

        adapter = MsgRecyclerAdapter(sendList, receiveList)
        rView.adapter = adapter


            /*val listView = v.findViewById<ListView>(R.id.msgList)

            val adapter =ArrayAdapter (
                requireActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listenMin)

            listView.setAdapter(adapter)
            *//*
            val sendBtn = v.findViewById<Button>(R.id.sendMsgBtn)
            val sendTxt = v.findViewById<EditText>(R.id.writeMsgField)

            val midlertidigBrukernavn = "midlertidigBruker"

            val currentUserUid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
            var currentUsername = "blank"
            FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid).get().addOnSuccessListener {
                currentUsername = it.child("username").value.toString()
            }


            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val nyMld = snapshot.child("message").value.toString()
                    val sender = snapshot.child("sender").value.toString()
                    val receiver = snapshot.child("receiver").value.toString()
                    //listenMin.add(nyMld)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
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

            FirebaseDatabase.getInstance().getReference("mobilMelding").addChildEventListener(childEventListener)

            sendBtn.setOnClickListener {
                val melding = sendTxt.text.toString()
                //genererer en tilfeldig ID for hver melding som blir sendt av en bruker
                val unikID = UUID.randomUUID().toString()

                database = FirebaseDatabase.getInstance().getReference("mobilMelding")
                val meldingInfo = Message(melding,currentUsername,midlertidigBrukernavn)
                database.child(unikID).setValue(meldingInfo).addOnSuccessListener {
                    //listenMin.add(melding)
                    sendTxt.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(this.context, "Kunne ikke sende melding", Toast.LENGTH_SHORT).show()
                }

            }
            */




            // Inflate the layout for this fragment
            return v
    }

    fun loadFragment(fragment : Fragment) {
        val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
        transaction.replace(R.id.secondLayout,fragment)
        transaction.commit()
    }
}