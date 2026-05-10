package com.example.sit305_81c

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages = listOf<ChatMessage>()
    private val TYPE_USER = 1
    private val TYPE_AI = 2

    fun submitList(newList: List<ChatMessage>) {
        messages = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == "USER") TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_ai, parent, false)
            AiViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))

        if (holder is UserViewHolder) {
            holder.textBody.text = message.text
            holder.textTime.text = time
        } else if (holder is AiViewHolder) {
            holder.textBody.text = message.text
            holder.textTime.text = time
        }
    }

    override fun getItemCount() = messages.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBody: TextView = view.findViewById(R.id.textMessageBody)
        val textTime: TextView = view.findViewById(R.id.textTimestamp)
    }

    class AiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBody: TextView = view.findViewById(R.id.textMessageBody)
        val textTime: TextView = view.findViewById(R.id.textTimestamp)
    }
}