package com.csci310_group29.trojancheckincheckout.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = "MyFirebaseMessagingService"
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, p0)
    }
}