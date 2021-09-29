package com.simpliest.giveget

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MinAnnonse : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annonse)

        //Onclick for "tilbake" knapp
        val backButton = findViewById<ImageButton>(R.id.backAnnonser)
        backButton.setOnClickListener {
            val intent = Intent(this, MineAnnonser::class.java)
            startActivity(intent)
        }

    }


}