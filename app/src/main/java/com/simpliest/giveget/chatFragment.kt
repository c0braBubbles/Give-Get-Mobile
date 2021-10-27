package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class chatFragment : Fragment() {

    val listenMin: MutableList<String> = ArrayList()

    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val v = inflater.inflate(R.layout.chat_fragment, container, false)

            val listView = v.findViewById<ListView>(R.id.msgList)

            val adapter =ArrayAdapter (
                requireActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listenMin)

            listView.setAdapter(adapter)

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
                    listenMin.add(nyMld)
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


            // Inflate the layout for this fragment
            return v
    }


}