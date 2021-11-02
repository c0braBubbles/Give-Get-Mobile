package com.simpliest.giveget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ChatMenuFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>?= null

    var liste1: MutableList<String> = ArrayList()
    var liste2: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(this.context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        liste1.add("JonKanon352")
        liste1.add("barmeren")
        liste1.add("flaskefjes")
        liste1.add("Bjørn")
        liste2.add("Gir bort kanonsikkerhetskurs")
        liste2.add("Sangtime, bare å bli med ;)")
        liste2.add("Trenger flasker")
        liste2.add("Jeg trenger en ørn")

        val v = inflater.inflate(R.layout.chat_menu_fragment, container, false)
        // Inflate the layout for this fragment

        val rView = v.findViewById<RecyclerView>(R.id.chat_menu_list)
        rView.layoutManager = layoutManager

        adapter = ChatRecyclerAdapter(liste1, liste2)
        rView.adapter = adapter

        return v
    }


}