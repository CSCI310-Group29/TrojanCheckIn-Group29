package com.csci310_group29.trojancheckincheckout.data.models

// model used for interacting with

data class User(val id: String? = null,
                @field:JvmField
                val isStudent: Boolean? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val major: String? = null,
                val studentId: String? = null)
