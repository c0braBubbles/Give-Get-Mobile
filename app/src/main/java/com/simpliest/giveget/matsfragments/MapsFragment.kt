package com.simpliest.giveget.matsfragments

import android.app.AlertDialog
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.content.Intent

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.simpliest.giveget.MainActivity2
import com.simpliest.giveget.R

class MapsFragment : Fragment() {

    private val secAct = MainActivity2()
    private lateinit var database: DatabaseReference

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        database = FirebaseDatabase.getInstance().getReference("AnnonseAndroid")
        database.child("2433e118-e966-4139-8fca-b65bff114f28").get().addOnSuccessListener {
            if(it.exists()) {
                val db_lat: Double = it.child("lat").value as Double
                val db_long: Double = it.child("long").value as Double

                val marker1 = LatLng(db_lat, db_long)
                googleMap.addMarker(MarkerOptions().position(marker1).title("Annonsetest"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker1))
                Toast.makeText(this.requireContext(), "lat: " + db_lat, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.requireContext(), "Ikke funnet", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this.requireContext(), "Operasjonen failet", Toast.LENGTH_SHORT).show()
        }

        val sydney = LatLng(59.148066, 9.692892)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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

        /*val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Test")
        builder.setMessage("Hei på deg")
        builder.show()*/
    }

}