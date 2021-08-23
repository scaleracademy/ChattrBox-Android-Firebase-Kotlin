package com.scaler.chattrbox.extensions

import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.getUID(): String? {
    var _uid: String? = null
    providerData.forEach {
        if (it.providerId == "firebase")
            _uid = it.uid
            return@forEach
    }
    return _uid
}