package com.simpliest.giveget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatRecyclerAdapter(private val nameList : MutableList<String>, private val adList : MutableList<String>)
    : RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatRecyclerAdapter.ViewHolder, position: Int) {
        holder.itemName.text = nameList[position]
        holder.itemAd.text = adList[position]
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemName: TextView
        var itemAd: TextView

        init {
            itemImage = itemView.findViewById(R.id.chat_card_image)
            itemName = itemView.findViewById(R.id.chat_card_name)
            itemAd = itemView.findViewById(R.id.chat_card_ad)
        }
    }

}