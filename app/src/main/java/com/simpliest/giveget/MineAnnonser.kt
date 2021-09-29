package com.simpliest.giveget
import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import android.widget.AdapterView





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


        // list view onclick
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, MinAnnonse::class.java)
                startActivity(intent)
                //val selectedItemText = parent.getItemAtPosition(position)
                // textView.text = "Selected : $selectedItemText"
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
