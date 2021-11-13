package com.simpliest.giveget

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage
import kotlinx.android.synthetic.main.fragment_profil.*
import java.io.File
import java.util.*


class Profil_fragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var ImageUri : Uri
    val brukerID = FirebaseAuth.getInstance().currentUser!!.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_profil, container, false)


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

        //Henter profilbilde fra firebase storage
        val storageRef = FirebaseStorage.getInstance().reference.child("image/$brukerID")
             val localfile = File.createTempFile("tempImage", "jpg")
             storageRef.getFile(localfile).addOnSuccessListener {
             val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                 imageView.setImageBitmap(bitmap)

            }

        return v
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Laster opp bilde....")
        progressDialog.setCancelable(false)

        val fileName = brukerID
        val storageReference = FirebaseStorage.getInstance().getReference("image/$fileName")

        if (this::ImageUri.isInitialized) {
            progressDialog.show()
            storageReference.putFile(ImageUri).addOnSuccessListener {
                imageView.setImageURI(null)
                Toast.makeText(this.context, "Bildet er lastet opp", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                //Navigerer til samme fragment på nytt, for å "refreshe"
                val secondFragment = Profil_fragment()
                val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
                transaction.replace(R.id.secondLayout, secondFragment)
                transaction.commit()
            }.addOnFailureListener {
                Toast.makeText(this.context, "Bildet kunne ikke lastes opp", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this.context, "Bilde er ikke valgt", Toast.LENGTH_SHORT)
                .show()
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