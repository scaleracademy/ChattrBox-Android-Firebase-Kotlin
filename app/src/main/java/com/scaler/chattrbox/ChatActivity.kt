package com.scaler.chattrbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.scaler.chattrbox.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityChatBinding
    private val database = FirebaseDatabase.getInstance("https://chattrbox-7afac-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val user = auth.currentUser!!
        val fbUser = User(user.providerData[0].uid, "johnDoe")

        database.child("users").child(user.providerData[0].uid).setValue(fbUser)
    }
}