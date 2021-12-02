package com.simpliest.giveget.matsfragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.simpliest.giveget.*
import com.simpliest.giveget.R
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.android.synthetic.main.marker_popup.*
import kotlinx.android.synthetic.main.marker_popup.view.*
import org.w3c.dom.Comment
import java.io.File

class MapsFragment : Fragment() {

    data class Samtale(
        val add_eier: String,
        val kontakteren: String,
        val annonse_tittel: String
    )

    private lateinit var database: DatabaseReference

    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var currentUsername : String

    public var callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        // Låser opp modusen så du kan sette telefonen i landskaps-modus
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)

        var markList = ArrayList<Marker>()      // Liste for alle markører
        database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")    // RTDB referanse

        val childEventListener = object : ChildEventListener {
            // Når noe er lagt til i DB-en vil denne intreffe
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                val add_title = dataSnapshot.child("tittel").value.toString()
                val add_descr = dataSnapshot.child("beskrivelse").value.toString()
                val add_type = dataSnapshot.child("kategorier").value.toString()
                val add_lat = dataSnapshot.child("lat").value as Double
                val add_long = dataSnapshot.child("long").value as Double
                val brukerID = dataSnapshot.child("brukerID").value.toString()
                val brukernavn = dataSnapshot.child("brukernavn").value.toString()
                // Objekt for å ta vare på alle verdiene til en annonse:
                val marker = Marker(add_lat, add_long, add_title, add_descr, brukerID, brukernavn, add_type)
                markList += marker


                // Itererer gjennom lista og legger til på kart
                for(i in markList.indices) {
                    val gmark = googleMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                markList[i].lat,
                                markList[i].long
                            )
                        ).title(markList[i].desc + "\n \n"  + markList[i].uname)
                    )
                    // improviserer for å ta vare på uid tittel, da gmark er et eget objekt i google
                    gmark.snippet = markList[i].uid
                    gmark.tag = markList[i].title

                    // setter farge på markøren
                    if(markList[i].type == "Tilbud") {
                        gmark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    } else if(markList[i].type == "Etterspørsel") {
                        gmark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    }
                }


                // markør onclick
                googleMap!!.setOnMarkerClickListener { marker ->
                    var bnavn: String = ""
                    for(i in markList.indices) {
                        if(markList[i].uid == marker.snippet) {
                            bnavn = markList[i].uname
                        }
                    }

                    // bygger popup. popup boksen er en vanlig alertbox som har vår egen layout
                    val popupDialog = LayoutInflater.from(context).inflate(R.layout.marker_popup, null)
                    val aBuilder = AlertDialog.Builder(context).setView(popupDialog)
                    aBuilder.setTitle("${marker.tag}")
                    aBuilder.setMessage(marker.title)

                    //Henter profilbilde fra firebase storage
                    val storageRef = FirebaseStorage.getInstance().reference.child("image/${marker.snippet}")
                    val localfile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        popupDialog.pbView.setImageBitmap(bitmap)
                    }

                    val aDialog = aBuilder.show()

                    popupDialog.popup_btn.setOnClickListener{
                        //val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                        //var currentUsername = ""
                        database = FirebaseDatabase.getInstance().getReference("Samtaler")

                        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid.toString()).get().addOnSuccessListener {
                            val samtale = Samtale(bnavn, it.child("username").value.toString(), marker.tag as String) // RTDB "tabell" for samtale

                            var i = 0

                            // lager samtale som sendes til DB for å registreres også tar deg til chat-fragment
                            database.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot : DataSnapshot) {
                                    for (postSnapshot in dataSnapshot.children) {
                                        if (postSnapshot.child("add_eier").value.toString() == samtale.add_eier &&
                                            postSnapshot.child("annonse_tittel").value.toString() == samtale.annonse_tittel &&
                                            postSnapshot.child("kontakteren").value.toString() == samtale.kontakteren
                                        ) {
                                            //Toast.makeText(context?.applicationContext, "Kan ikke starte samtale knyttet til denne annonsen", Toast.LENGTH_SHORT).show()
                                            Toast.makeText(requireContext(), "Kan ikke starte samtale knyttet til denne annonsen", Toast.LENGTH_SHORT).show()
                                            return
                                        }
                                    }
                                    database.push().setValue(samtale).addOnSuccessListener {
                                        aDialog.dismiss()
                                        val fragment = chatFragment(bnavn, marker.tag.toString())
                                        val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                                        fm.beginTransaction().replace(R.id.secondLayout, fragment)
                                            .commit()
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Noe gikk galt når du prøvde å starte samtale med " + samtale.add_eier,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    //håndter feil
                                }
                            })
                        }
                    }
                    true
                }
            }

            // Resterende firebase funksjoner blir ikke brukt

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.addChildEventListener(childEventListener)


        // "Vanlig måte å legge til markør på kartet. Kom med fragmentet. Kjekt å gå i tilfelle jeg...
        // glemmer hvordan man gjorde enkelte ting
        /*val sydney = LatLng(59.148066, 9.692892)
        googleMap.addMarker(MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //tester markør-onclick DET FUNKET
        googleMap.setOnMarkerClickListener { sydney ->
            Toast.makeText(context, "onclick på markør test",
                Toast.LENGTH_SHORT).show()
            true
        }*/
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}