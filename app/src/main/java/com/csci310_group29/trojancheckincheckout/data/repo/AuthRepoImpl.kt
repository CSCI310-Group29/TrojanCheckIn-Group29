package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthRemoteDataSource

class AuthRepoImpl: AuthRepository {
    private val remoteDataSource = AuthRemoteDataSource()

    override fun getCurrentUser(callback: AuthRepository.Callback): User {
        TODO("Not yet implemented")
    }

    override fun getUserWithCredentials(email: String, password: String, callback: AuthRepository.Callback): User? {
        return remoteDataSource.getUserWithCredentials(email, password, callback)
    }

    override fun createUser(email: String, password: String, user: User, callback: AuthRepository.Callback) {
        remoteDataSource.createUser(email, password, user, callback)
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