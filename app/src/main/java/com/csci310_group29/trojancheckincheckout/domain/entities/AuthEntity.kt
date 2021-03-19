package com.csci310_group29.trojancheckincheckout.domain.entities

import android.graphics.Bitmap
import com.google.firebase.firestore.DocumentId

data class AuthEntity(
        @DocumentId
        val id: String,
        val email: String?)
