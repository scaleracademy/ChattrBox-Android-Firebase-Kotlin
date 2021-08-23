package com.scaler.chattrbox.model

import com.google.firebase.auth.FirebaseUser
import com.scaler.chattrbox.extensions.getUID

data class ChatUser(
    val userId: String,
    var username: String? = null
) {
    companion object {
        @JvmStatic
        fun createFromFirebaseAuth(fbUser: FirebaseUser): ChatUser = ChatUser(
            fbUser.getUID()!!
        )
    }
}
