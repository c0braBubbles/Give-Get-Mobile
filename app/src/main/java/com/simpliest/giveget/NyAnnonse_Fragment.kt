package com.simpliest.giveget

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.simpliest.giveget.databinding.ActivityMain2Binding
import kotlinx.android.synthetic.main.fragment_ny_annonse_.*
import java.util.*


class NyAnnonse_fragment : Fragment() {

    private lateinit var database : DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient   //posisjonting
    var lat: Double = 0.0
    var long: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity)
        checkLocationPermissions()

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
        val radioTilbud = v.findViewById<RadioButton>(R.id.Tilbud)
        val radioEtterspørsel = v.findViewById<RadioButton>(R.id.Etterspørsel)
        var kategorier = "";


        //listener for "Publiser annonse" knappen
        publiserBtn.setOnClickListener {
            //Konstruktør for annonsene
            data class Annonse(
                val tittel: String, val beskrivelse: String, val id: String,
                val kategorier: String, val lat: Double, val long: Double
            )

            //henter og gjør om tittel og beskrivelse til String
            val title = sendTitle.text.toString()
            val desc = sendDesc.text.toString()

            //Sjekker om annonse er tilbud eller etterspørsel
            if (radioTilbud.isChecked) {
              kategorier = "Tilbud";
            } else if(radioEtterspørsel.isChecked) {
                kategorier = "Etterspørsel";
            }



            //genererer en random ID, brukes som "Key" til den enkelte annonsen
            val unikID = UUID.randomUUID().toString()

            //Referanse til der Annonsen skal lagres
            database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

                //Lager et annonse objekt ved bruk av konstruktøren
                val annonse = Annonse(title, desc, unikID, kategorier, lat, long)

                database.child(unikID).setValue(annonse).addOnSuccessListener {
                        sendTitle.text.clear()
                        sendDesc.text.clear()

                    }.addOnFailureListener {
                        //Gjør dette, dersom det feiler
                        Toast.makeText(
                            this.context,
                            "Kunne ikke opprette annonse",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }



        return v

    }


    fun checkLocationPermissions() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if(it != null) {
                //Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                lat = it.latitude
                long = it.longitude

                Toast.makeText(
                    this.context,
                    lat.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}