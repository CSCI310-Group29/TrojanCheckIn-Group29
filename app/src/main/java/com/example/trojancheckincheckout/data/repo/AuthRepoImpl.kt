package com.example.trojancheckincheckout.data.repo

import com.example.trojancheckincheckout.data.models.User

class AuthRepoImpl: AuthRepository {
    override fun getCurrentUser(): User {
        TODO("Not yet implemented")
    }

    override fun getUserWithCredentials(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override fun createUser(user: User): User? {
        TODO("Not yet implemented")
    }

    override fun updateEmail(newEmail: String): User? {
        TODO("Not yet implemented")
    }

    override fun resetPassword(): User? {
        TODO("Not yet implemented")
    }

    override fun deleteCurrentUser() {
        TODO("Not yet implemented")
    }

}