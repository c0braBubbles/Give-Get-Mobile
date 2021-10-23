package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class NyAnnonse_fragment : Fragment() {

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ny_annonse_, container, false)
        val btn = v.findViewById<ImageButton>(R.id.backBtn2)
        btn.setOnClickListener {
            val secondFragment = Annonser_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.secondLayout,secondFragment)
            transaction.commit()
        }

        val publiserBtn = v.findViewById<Button>(R.id.publiserbtn)
        val sendTitle = v.findViewById<EditText>(R.id.titleField)
        val sendDesc = v.findViewById<EditText>(R.id.descField)




        //listener for "Publiser annonse" knappen
        publiserBtn.setOnClickListener {
            //Konstruktør for annonsene
            data class Annonse(val tittel: String, val beskrivelse: String, val id: String)

            //henter og gjør om tittel og beskrivelse til String
            val title = sendTitle.text.toString()
            val desc = sendDesc.text.toString()
            //genererer en random ID, brukes som "Key" til den enkelte annonsen
            val unikID = UUID.randomUUID().toString()

            //Referanse til der Annonsen skal lagres
            database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

                //Lager et annonse objekt ved bruk av konstruktøren
                val annonse = Annonse(title, desc, unikID)

                database.child(unikID).setValue(annonse).addOnSuccessListener {
                        sendTitle.text.clear()
                        sendDesc.text.clear()

                    }.addOnFailureListener {
                        //Gjør dette, dersom det feiler
                        Toast.makeText(
                            this.context,
                            "Kunne ikke opprette annonse",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }



        return v

    }
}