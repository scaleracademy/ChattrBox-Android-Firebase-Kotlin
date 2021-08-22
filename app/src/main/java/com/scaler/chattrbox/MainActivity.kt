package com.scaler.chattrbox

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.scaler.chattrbox.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "AUTH"
    }

    private lateinit var _binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()
    private val loginCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(this@MainActivity, "VERIFICATION COMPLETED", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@MainActivity, "VERIFICATION FAILED", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Toast.makeText(this@MainActivity, "VERIFICATION CODE SENT", Toast.LENGTH_SHORT).show()
            updateUIState(UIState.ENTER_OTP)

            _binding.verifyOtpButton.setOnClickListener {
                val credential = PhoneAuthProvider.getCredential(
                        verificationId,
                        _binding.otpEditText.text.toString()
                )
                signInWithPhoneAuthCredential(credential)

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.phoneLoginButton.setOnClickListener {
            initiatePhoneLogin(_binding.phoneNumberEditText.text.toString())
        }
        _binding.resetButton.setOnClickListener {
            updateUIState(UIState.ENTER_PHONE)
        }


    }

    fun initiatePhoneLogin(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(loginCallbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    enum class UIState {
        ENTER_PHONE,
        ENTER_OTP
    }

    fun updateUIState(uiState: UIState) = when (uiState) {
        UIState.ENTER_PHONE -> {
            _binding.phoneNumberEditText.apply {
                isEnabled = true
                setText("")
            }
            _binding.otpEditText.apply {
                isEnabled = false
                isVisible = false
            }
            _binding.phoneLoginButton.isEnabled = true
            _binding.resetButton.isEnabled = false
            _binding.verifyOtpButton.isEnabled = false
        }
        UIState.ENTER_OTP -> {
            _binding.phoneNumberEditText.isEnabled = false
            _binding.otpEditText.apply {
                isVisible = true
                isEnabled = true
                setText("")
            }
            _binding.phoneLoginButton.isEnabled = false
            _binding.resetButton.isEnabled = true
            _binding.verifyOtpButton.isEnabled = true
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show()
                        val user = task.result?.user
                        // TODO: move to the chat screen

                    } else {
                        Toast.makeText(this, "LOGIN FAILED", Toast.LENGTH_SHORT).show()
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "WRONG OTP", Toast.LENGTH_SHORT).show()
                        }
                        updateUIState(UIState.ENTER_PHONE)

                    }
                }
    }

}