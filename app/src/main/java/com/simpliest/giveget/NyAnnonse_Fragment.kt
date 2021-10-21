package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NyAnnonse_fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ny_annonse_, container, false)
        val btn = v.findViewById<ImageButton>(R.id.backBtn2)
        btn.setOnClickListener {
            val secondFragment = Annonser_fragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.secondLayout,secondFragment)
            transaction.commit()
        }
        return v

    }
}