package com.simpliest.giveget

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MsgRecyclerAdapter(private val senderList : MutableList<String>, private val receiverList: MutableList<String>)
    : RecyclerView.Adapter<MsgRecyclerAdapter.ViewHolder>() {
    //"Mal" for klassen har blitt hentet fra nettet, se mer om dette i kildehenvisning i dokumentasjon
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MsgRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.msg_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MsgRecyclerAdapter.ViewHolder, position: Int) {
        holder.itemSender.text = senderList[position]
        holder.itemReceiver.text = receiverList[position]
        if (senderList[position] != "")
            holder.itemSender.setPadding(15,15,15,15)
        else if (receiverList[position] != "")
            holder.itemReceiver.setPadding(15,15,15,15)
    }

    override fun getItemCount(): Int {
        return senderList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemSender: TextView
        var itemReceiver: TextView

        init {
            itemSender = itemView.findViewById(R.id.sender_msg)
            itemReceiver = itemView.findViewById(R.id.receiver_msg)
        }
    }


}