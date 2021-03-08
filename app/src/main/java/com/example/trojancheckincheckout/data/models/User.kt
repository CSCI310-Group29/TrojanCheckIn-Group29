package com.example.trojancheckincheckout.data.models

// model used for interacting with

data class User(val id: String? = null,
                val isStudent: Boolean? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val major: String? = null,
                val studentId: String? = null,
                val email: String? = null,
                val password: String? =null,
                val profilePictureRef: String? = null)
