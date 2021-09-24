package com.simpliest.giveget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar


class MinProfil : AppCompatActivity() {

    private lateinit var toolbarProfil : Toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        //Onclick for "Mine Annonser" knapp
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MineAnnonser::class.java)
            startActivity(intent)
        }

        toolbarProfil = findViewById(R.id.toolbarProfil)
        toolbarProfil.title = " "
        setSupportActionBar(toolbarProfil)

    }


}