package com.csci310_group29.trojancheckincheckout.domain.models

import android.graphics.Bitmap

// model used for interacting with

data class User(val id: String,
                @field:JvmField
                val isStudent: Boolean? = null,
                val email: String? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val major: String? = null,
                val isCheckedIn: Boolean = false,
                val studentId: String? = null,
                val profilePicture: Bitmap? = null)
