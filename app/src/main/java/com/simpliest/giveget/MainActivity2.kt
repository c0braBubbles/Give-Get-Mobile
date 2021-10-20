package com.simpliest.giveget

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationBarItemView
import com.simpliest.giveget.databinding.ActivityMain2Binding
import com.simpliest.giveget.matsfragments.Dashboard
import com.simpliest.giveget.matsfragments.MapsFragment
import com.simpliest.giveget.matsfragments.Search
import kotlinx.android.synthetic.main.activity_main2.*
import kotlin.properties.Delegates

class MainActivity2 : AppCompatActivity() {

    private val dashboardFragment = Dashboard()
    private val searchFragment = Search()
    private val fragmentChat = chatFragment()
    private val profileFragment = Profil_fragment()
    private val addsFragment = Annonser_fragment()
    private val newAddFragment = NyAnnonse_fragment()

    private lateinit var binding: ActivityMain2Binding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient   //posisjonting
    var lat: Double = 0.0
    var long: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        // kall på posisjon stuff (bricked)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocationPermissions()

        navbar_top.setOnNavigationItemReselectedListener {
            when(it.itemId) {
                R.id.nav_home -> replaceFragment(dashboardFragment)
                R.id.nav_search -> replaceFragment(searchFragment)
                R.id.nav_add -> replaceFragment(addsFragment)
                R.id.nav_chat -> replaceFragment(fragmentChat)
                R.id.nav_profile -> replaceFragment(profileFragment)
            }
            true
        }


        // Setter dashboard fragmentet på (kart)
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().replace(R.id.secondLayout, dashboardFragment).commit()
    }


    // metode for å hente posisjon, men alt her er bricka
    fun checkLocationPermissions() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if(it != null) {
                //Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                lat = it.latitude.toDouble()
                long = it.longitude.toDouble()

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Test")
                builder.setMessage("lat: " + lat + " long: " + long)
                builder.show()
            }
        }
    }


    fun replaceFragment(fragment: Fragment) {
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().replace(R.id.secondLayout, fragment).commit()
    }

}