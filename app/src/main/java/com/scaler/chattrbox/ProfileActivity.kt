package com.scaler.chattrbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.scaler.chattrbox.databinding.ActivityProfileBinding
import com.scaler.chattrbox.model.ChatUser

class ProfileActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val datRef = Firebase.database("https://chattrbox-5a109-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private lateinit var user: ChatUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)

        user = ChatUser.createFromFirebaseAuth(auth.currentUser!!) // TODO: send back to login if this is null

        _binding.saveProfileButton.setOnClickListener {
            saveUserProfile()
        }

        setContentView(_binding.root)
    }

    fun saveUserProfile() {
        user.username = _binding.usernameEditText.text.toString()

        datRef.child("users").child(user.userId).setValue(user)

        Toast.makeText(this, "User Profile Saved", Toast.LENGTH_SHORT).show()
    }
}