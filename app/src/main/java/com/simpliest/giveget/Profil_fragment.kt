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


class Profil_fragment : Fragment() {


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


        return v
    }


}