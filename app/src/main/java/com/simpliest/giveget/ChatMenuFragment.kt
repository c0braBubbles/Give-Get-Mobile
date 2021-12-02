package com.simpliest.giveget

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ChatMenuFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>?= null

    var liste1: MutableList<String> = ArrayList()
    var liste2: MutableList<String> = ArrayList()
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var currentUsername : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(this.context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Låser til portrett-modus
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        liste1.clear()
        liste2.clear()

        val v = inflater.inflate(R.layout.chat_menu_fragment, container, false)

        val rView = v.findViewById<RecyclerView>(R.id.chat_menu_list)
        rView.layoutManager = layoutManager

        FirebaseDatabase.getInstance().getReference("mobilBruker/"+currentUserUid.toString()).get().addOnSuccessListener {
            currentUsername = it.child("username").value.toString()
            //Listener som henter ned data fra realtime databasen
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val eier = snapshot.child("add_eier").value.toString()
                    val kontakter = snapshot.child("kontakteren").value.toString()
                    val annonseTittel = snapshot.child("annonse_tittel").value.toString()
                    if (eier == currentUsername || kontakter == currentUsername) {
                        if (eier == currentUsername) {
                            liste1.add(kontakter)
                            liste2.add(annonseTittel)
                        } else {
                            liste1.add(eier)
                            liste2.add(annonseTittel)
                        }

                    }

                    adapter = context?.let { it1 -> ChatRecyclerAdapter(it1, liste1, liste2) }
                    rView.adapter = adapter
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            FirebaseDatabase.getInstance().getReference("Samtaler")
                .addChildEventListener(childEventListener)
        }

        return v
    }

}