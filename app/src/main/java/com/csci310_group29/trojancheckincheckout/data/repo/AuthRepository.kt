package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User

interface AuthRepository {

    interface Callback {
        fun onSuccess(user: User?)
        fun onFailure()
    }
    fun getCurrentUser(callback: Callback): User?

    fun getUserWithCredentials(email: String, password: String, callback: Callback): User?

    fun createUser(email: String, password: String, user: User, callback: Callback)

    fun updateEmail(newEmail: String): User?

    fun resetPassword(): User?

    fun deleteCurrentUser()
}