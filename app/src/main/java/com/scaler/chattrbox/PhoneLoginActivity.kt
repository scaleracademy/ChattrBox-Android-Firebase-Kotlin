package com.scaler.chattrbox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.scaler.chattrbox.databinding.ActivityPhoneLoginBinding
import java.util.concurrent.TimeUnit

class PhoneLoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPhoneLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(this@PhoneLoginActivity, "AUTH DONE", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@PhoneLoginActivity, "AUTH FAILED", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Log.d("AUTH", "onCodeSent:$verificationId")
            loginWithCode("456789", verificationId)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPhoneLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        if (auth.currentUser != null) {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        _binding.phoneLoginButton.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+914567845678")       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@PhoneLoginActivity)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun loginWithCode(code: String, verificationId: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "signInWithCredential:success")

                    val user = task.result?.user
                    // goto next screen
                    startActivity(Intent(this, ChatActivity::class.java))
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("AUTH", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "WRONG CODE", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this@PhoneLoginActivity, "AUTH DONE", Toast.LENGTH_SHORT).show()
                    // Update UI - reset login
                }
            }

    }
}