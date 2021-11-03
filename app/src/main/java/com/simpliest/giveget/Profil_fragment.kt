package com.simpliest.giveget

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage
import kotlinx.android.synthetic.main.activity_profil.*
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class Profil_fragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var ImageUri : Uri
    //lateinit var binding: Profil_fragment
    val brukerID = FirebaseAuth.getInstance().currentUser!!.uid.toString()

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

        v.findViewById<ImageButton>(R.id.findPic).setOnClickListener{
            selectImage()
        }

        v.findViewById<Button>(R.id.uploadBtn).setOnClickListener {
            uploadImage()
        }


        return v
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Laster opp bilde....")
        progressDialog.setCancelable(false)
        progressDialog.show()

       // val formatter = SimpleDateFormat("yyyy_mm_dd_HH_mm_ss", Locale.getDefault())
       // val now = Date()
       // val fileName = formatter.format(now)
        val fileName = brukerID
        val storageReference = FirebaseStorage.getInstance().getReference("image/$fileName")

           storageReference.putFile(ImageUri).addOnSuccessListener {
               imageView.setImageURI(null)
               Toast.makeText(this.context, "Bildet er lastet opp", Toast.LENGTH_SHORT).show()
               if (progressDialog.isShowing) progressDialog.dismiss()
           }.addOnFailureListener {
             Toast.makeText(this.context, "Bildet kunne ikke lastes opp", Toast.LENGTH_SHORT).show()
           }

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)

          if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!                                                        
              imageView.setImageURI(ImageUri)
          }
    }


}