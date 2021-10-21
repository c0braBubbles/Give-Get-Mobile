package com.simpliest.giveget

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.OnCompleteListener


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


        val emailFelt = v.findViewById<EditText>(R.id.loginEmail)
        val passordFelt = v.findViewById<EditText>(R.id.loginPassword)
        val btnLogin = v.findViewById<Button>(R.id.loginBtn)

        btnLogin.setOnClickListener {
            when {
                // Dette sjekker om du IKKE har fylt inn email. Da vil det komme en Toast melding
                TextUtils.isEmpty(emailFelt.text.toString().trim(){ it <= ' '}) -> {
                    Toast.makeText(
                        this.context,
                        "Plis skriv inn en email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Samme som over, bare med passord
                TextUtils.isEmpty(passordFelt.text.toString().trim(){ it <= ' '}) -> {
                    Toast.makeText(
                        this.context,
                        "Plis skriv inn et passord",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = emailFelt.text.toString().trim {it <= ' '}
                    val passord: String = passordFelt.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, passord)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if(task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this.context,
                                        "Du har logget inn",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this.context, MainActivity2::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // hindrer brukeren å klikke seg tilbake
                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    //finish()
                                } else {
                                    Toast.makeText(
                                        this.context,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }


        return v
    }


}