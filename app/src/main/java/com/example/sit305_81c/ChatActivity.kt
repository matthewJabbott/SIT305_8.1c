package com.example.sit305_81c

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: ChatAdapter
    private var currentUsername: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        currentUsername = intent.getStringExtra("USER_NAME") ?: "User"

        db = AppDatabase.getDatabase(this)
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        adapter = ChatAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            db.chatDao().getAllMessages().collect { messages ->
                adapter.submitList(messages)
                if (messages.isNotEmpty()) {
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            }
        }

        sendButton.setOnClickListener {
            val text = messageInput.text.toString()
            if (text.isNotEmpty()) {
                sendMessage(text)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessage(text: String) {
        lifecycleScope.launch {
            val userMsg = ChatMessage(
                text = text,
                sender = "USER",
                timestamp = System.currentTimeMillis()
            )
            db.chatDao().insertMessage(userMsg)

            try {
                val apiResult = RetrofitInstance.api.sendMessage(
                    ChatRequest(currentUsername, text)
                )

                val aiMsg = ChatMessage(
                    text = apiResult.response,
                    sender = "AI",
                    timestamp = System.currentTimeMillis()
                )
                db.chatDao().insertMessage(aiMsg)

            } catch (e: Exception) {
                db.chatDao().insertMessage(
                    ChatMessage(
                        text = "Error: ${e.message}",
                        sender = "AI",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}