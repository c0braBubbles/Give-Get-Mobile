package com.simpliest.giveget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

//Konstruktør, tar en liste som innparameter slik at vi kan fylle den fra Firebase
//Denne klassen er en modifisert versjon av en funnet på youtube, link ligger i kilder
class RecyclerAdapter(private val titleList : MutableList<String>, private val detailsList : MutableList<String>
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var titles = titleList
    private var details = detailsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detailed)
        }
    }

}