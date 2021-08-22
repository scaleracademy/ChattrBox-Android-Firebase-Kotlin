package com.scaler.chattrbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.scaler.chattrbox.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityChatBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val user = auth.currentUser
    }
}