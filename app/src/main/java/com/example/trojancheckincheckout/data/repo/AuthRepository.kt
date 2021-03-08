package com.example.trojancheckincheckout.data.repo

import com.example.trojancheckincheckout.data.models.User

interface AuthRepository {
    fun getCurrentUser(): User

    fun getUserWithCredentials(email: String, password: String): User?

    fun createUser(user: User): User?

    fun updateEmail(newEmail: String): User?

    fun resetPassword(): User?

    fun deleteCurrentUser()
}