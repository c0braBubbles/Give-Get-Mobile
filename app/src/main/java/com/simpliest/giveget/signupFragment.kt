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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.simpliest.giveget.databinding.SignupFragmentBinding

class signupFragment : Fragment() {

    private lateinit var binding: SignupFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.signup_fragment, container, false)
        //Dette er for at brukeren skal kunne gå fra signup-fragmentet til login-fragmentet
        val bt = v.findViewById<Button>(R.id.mLoginBtn)
        bt.setOnClickListener {
            val secondFragment = loginFragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.mainlayout,secondFragment)
            transaction.commit()
        }

        val bt2 = v.findViewById<Button>(R.id.createAccBtn)
        /*bt2.setOnClickListener {
            val thirdFragment = chatFragment()
            val transaction: FragmentTransaction = parentFragmentManager!!.beginTransaction()
            transaction.replace(R.id.mainlayout,thirdFragment)
            transaction.commit()
        }*/



        val emailFelt = v.findViewById<EditText>(R.id.editEmailField)
        val nameFelt = v.findViewById<EditText>(R.id.editNameField)
        val usernameFelt = v.findViewById<EditText>(R.id.editUsernameField)
        val passordFelt = v.findViewById<EditText>(R.id.editPasswordField)

        bt2.setOnClickListener {
            when {
                // Dette sjekker om du IKKE har fylt inn email. Da vil det komme en Toast melding
                TextUtils.isEmpty(emailFelt.text.toString().trim(){ it <= ' '}) -> {
                    Toast.makeText(
                        this.context,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Samme som over, bare med passord
                TextUtils.isEmpty(passordFelt.text.toString().trim(){ it <= ' '}) -> {
                    Toast.makeText(
                        this.context,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = emailFelt.text.toString().trim {it <= ' '}
                    val passord: String = passordFelt.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passord)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if(task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this.context,
                                        "Du har registrert bruker",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this.context, MainActivity2::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // hindrer brukeren å klikke seg tilbake
                                    intent.putExtra("user_id", firebaseUser.uid)
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