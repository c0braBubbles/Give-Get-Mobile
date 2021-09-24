package com.simpliest.giveget
import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar


class MineAnnonser : AppCompatActivity() {

    private lateinit var myToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annonser)

        myToolbar = findViewById(R.id.toolbarProfil)
        myToolbar.title = " "
        setSupportActionBar(myToolbar)

        val listView = findViewById<ListView>(R.id.ListView)

        //Adapter kobling for listview
        listView.adapter = MyCustomAdapter(this)

        //Onclick for "tilbake" knapp
        val backButton = findViewById<ImageButton>(R.id.backAnnonser)
        backButton.setOnClickListener {
            val intent = Intent(this, MinProfil::class.java)
            startActivity(intent)
        }

    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        //Hvor mange rader
        override fun getCount(): Int {
            return 20
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(p0: Int): Any {
            return "Test String"
        }

        //Rendrer hver rad
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "Annonser"
            textView.textSize = 30F
            return textView
        }
    }
}
