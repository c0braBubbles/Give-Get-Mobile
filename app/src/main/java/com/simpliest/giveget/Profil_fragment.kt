package com.simpliest.giveget

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class Profil_fragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.activity_profil, container, false)


        // Log-ut med Firebase Auth
        val btn_logout = v.findViewById<Button>(R.id.logUtBtn).setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this.context, MainActivity::class.java))
        }

        val currentUserUid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
        var currentUsername = "blank"
        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid).get().addOnSuccessListener {
            currentUsername = it.child("username").value.toString()
            val navn = v.findViewById<TextView>(R.id.profil_navn)
            navn.setText(currentUsername)
        }


        return v
    }


}