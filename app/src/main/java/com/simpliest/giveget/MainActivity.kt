package com.simpliest.giveget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.simpliest.giveget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toLoginBtn.setOnClickListener {
            val toFragment = loginFragment()
            val fm: FragmentManager = supportFragmentManager
            fm.beginTransaction().replace(R.id.mainlayout, toFragment).commit()
        }

        binding.regBtn.setOnClickListener {
            val toFragment = signupFragment()
            val fm: FragmentManager = supportFragmentManager
            fm.beginTransaction().replace(R.id.mainlayout, toFragment).commit()
        }

        /*binding.toCreateAccBtn.setOnClickListener {
            binding.toLoginBtn.visibility = View.INVISIBLE
            binding.toCreateAccBtn.visibility = View.INVISIBLE

            val firstFragment = signupFragment()
            val fm: FragmentManager = supportFragmentManager
            fm.beginTransaction().replace(R.id.mainlayout,firstFragment).commit()
        }

        binding.toLoginBtn.setOnClickListener {

            binding.toLoginBtn.visibility = View.INVISIBLE
            binding.toCreateAccBtn.visibility = View.INVISIBLE

            val secondFragment = loginFragment()
            val fm: FragmentManager = supportFragmentManager
            fm.beginTransaction().replace(R.id.mainlayout,secondFragment).commit()
        }*/

    }

    fun logIn(view: android.view.View) {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}