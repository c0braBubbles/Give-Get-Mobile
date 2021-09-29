package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class loginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.login_fragment, container, false)

        val bt = v.findViewById<Button>(R.id.mCreateAccBtn)
        bt.setOnClickListener {
            val secondFragment = signupFragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.mainlayout,secondFragment)
            transaction.commit()
        }

        val bt2 = v.findViewById<Button>(R.id.loginBtn)
        bt2.setOnClickListener {
            val thirdFragment = chatFragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.mainlayout,thirdFragment)
            transaction.commit()
        }

        return v
    }


}