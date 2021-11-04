package com.simpliest.giveget

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profil.*


class NyAnnonse_fragment : Fragment() {

    private lateinit var database : DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient   //posisjonting
    var lat: Double = 0.0
    var long: Double = 0.0
    lateinit var ImageUri : Uri
    lateinit var annonseID: String

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
        val brukerID = FirebaseAuth.getInstance().currentUser!!.uid



        //listener for "Publiser annonse" knappen
        publiserBtn.setOnClickListener {
            //Konstruktør for annonsene
            data class Annonse(
                val tittel: String, val beskrivelse: String, val id: String,
                val kategorier: String, val lat: Double, val long: Double, val brukerID: String
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
            annonseID = UUID.randomUUID().toString()



            //Referanse til der Annonsen skal lagres
            database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")

            //Lager et annonse objekt ved bruk av konstruktøren
            val annonse = Annonse(title, desc, annonseID, kategorier, lat, long, brukerID)

            database.child(annonseID).setValue(annonse).addOnSuccessListener {
                sendTitle.text.clear()
                sendDesc.text.clear()
                uploadImage(annonseID)


            }.addOnFailureListener {
                //Gjør dette, dersom det feiler
                Toast.makeText(
                    this.context,
                    "Kunne ikke opprette annonse",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        v.findViewById<ImageButton>(R.id.findPic).setOnClickListener{
            selectImage()
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
    private fun uploadImage(id :String) {
        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Laster opp bilde....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = id
        val storageReference = FirebaseStorage.getInstance().getReference("image/$fileName")
        if (fileName != null) {
            storageReference.putFile(ImageUri).addOnSuccessListener {

                Toast.makeText(this.context, "Bildet er lastet opp", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                //Navigerer til samme fragment på nytt, for å "refreshe"
                val secondFragment = Profil_fragment()
                val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
                transaction.replace(R.id.secondLayout, secondFragment)
                transaction.commit()
            }.addOnFailureListener {
                Toast.makeText(this.context, "Bildet kunne ikke lastes opp", Toast.LENGTH_SHORT)
                    .show()
           }
        }

    }
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            ImageUri = data?.data!!
           // imageView.setImageURI(ImageUri)
        }
    }
}