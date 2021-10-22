package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        //Dette er listener for "send melding" knappen, i chat-vinduet
        sendBtn.setOnClickListener {
            //henter teksten til skrivefeltet, i chat-vinduet
            val melding = sendTxt.text.toString()
            //genererer en tilfeldig ID for hver melding som blir sendt av en bruker
            val unikID = UUID.randomUUID().toString()


            //Referanse til plassen hvor meldinger skal lagres
            database = FirebaseDatabase.getInstance().getReference("mobilMelding")
            //setter inn verdi til referansen over, i databasen
            database.child(unikID).setValue(melding).addOnSuccessListener {
                //Gjør dette, dersom alt går som det skal
                listenMin.add(melding)
                sendTxt.text.clear()
            }.addOnFailureListener {
                //Gjør dette, dersom det feiler
                Toast.makeText(this.context, "Kunne ikke sende melding", Toast.LENGTH_SHORT).show()
            }

        }


        // Inflate the layout for this fragment
        return v
    }


}