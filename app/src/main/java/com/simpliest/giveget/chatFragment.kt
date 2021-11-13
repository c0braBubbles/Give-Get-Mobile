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
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var currentUsername : String


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

        val v = inflater.inflate(R.layout.chat_fragment, container, false)

        val tf = v.findViewById<TextView>(R.id.chat_info)
        tf.text = samtalePartner +"\n" + annonseNavn

        val rView = v.findViewById<RecyclerView>(R.id.msg_list)
        rView.layoutManager = layoutManager

        //adapter = MsgRecyclerAdapter(sendList, receiveList)
        //rView.adapter = adapter




        val sendBtn = v.findViewById<Button>(R.id.sendMsgBtn)
        val sendTxt = v.findViewById<EditText>(R.id.writeMsgField)

        /*val currentUserUid = FirebaseAuth.getInstance().currentUser?.getUid()
        var currentUsername = "blank"
        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid).get().addOnSuccessListener {
            currentUsername = it.child("username").value.toString()
        }*/

        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid.toString()).get().addOnSuccessListener {
            currentUsername = it.child("username").value.toString()


            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val nyMld = snapshot.child("message").value.toString()
                    val sender = snapshot.child("sender").value.toString()
                    val receiver = snapshot.child("receiver").value.toString()
                    val annonse = snapshot.child("annonse").value.toString()
                    if (annonse == annonseNavn) {
                        if (sender == currentUsername && receiver == samtalePartner) {
                            sendList.add(nyMld)
                            receiveList.add("")
                        } else if (sender == samtalePartner && receiver == currentUsername) {
                            receiveList.add(nyMld)
                            sendList.add("")
                        }
                    }
                    adapter = MsgRecyclerAdapter(sendList, receiveList)
                    rView.adapter = adapter

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

            FirebaseDatabase.getInstance().getReference("mobilMelding")
                .addChildEventListener(childEventListener)
        }
        sendBtn.setOnClickListener {
            val melding = sendTxt.text.toString()
            if ( melding.trim().isEmpty() ) return@setOnClickListener

            database = FirebaseDatabase.getInstance().getReference("mobilMelding")
            val meldingInfo = Message(melding,currentUsername,samtalePartner, annonseNavn)
            database.push().setValue(meldingInfo).addOnSuccessListener {
                sendTxt.text.clear()
            }.addOnFailureListener {
                Toast.makeText(this.context, "Kunne ikke sende melding", Toast.LENGTH_SHORT).show()
            }



        }




        //adapter = MsgRecyclerAdapter(sendList, receiveList)
        //rView.adapter = adapter
        // Inflate the layout for this fragment
        return v
    }


}