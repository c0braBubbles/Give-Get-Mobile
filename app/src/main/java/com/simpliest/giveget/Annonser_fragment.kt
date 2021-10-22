package com.simpliest.giveget

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Annonser_fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private lateinit var myToolbar : Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.activity_annonser, container, false)
        val bt = v.findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        bt.setOnClickListener {
            val secondFragment = NyAnnonse_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.secondLayout,secondFragment)
            transaction.commit()
        }
        /*
        myToolbar = v.findViewById(R.id.toolbarProfil)
        myToolbar.title = " "
        setSupportActionBar(myToolbar)
*/
        val listView = v.findViewById<ListView>(R.id.ListView)

        //Adapter kobling for listview
        listView.adapter = MyCustomAdapter(requireActivity().getApplicationContext())


        /*val btnTest = v.findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        btnTest.setOnClickListener {
            val secondFragment = NyAnnonse_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.mainlayout,secondFragment)
            transaction.commit()
        }*/


        // list view onclick
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val Fragment = Annonse_fragment()
                val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
                transaction.replace(R.id.secondLayout,Fragment)
                transaction.commit()
            }

        return v

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