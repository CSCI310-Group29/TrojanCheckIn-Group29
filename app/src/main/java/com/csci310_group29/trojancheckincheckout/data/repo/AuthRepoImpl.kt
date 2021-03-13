package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.fake.AuthFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class AuthRepoImpl(fake: Boolean = false): AuthRepository {
    private val remoteDataSource: AuthRepository = if (!fake) {
        AuthFirebaseDataSource()
    } else {
        AuthFakeDataSource()
    }

    override fun getCurrentUser(): Single<User> {
        return remoteDataSource.getCurrentUser()
    }

    override fun getUserWithCredentials(email: String, password: String): Single<User> {
        return remoteDataSource.getUserWithCredentials(email, password)
    }

    override fun logoutCurrentUser(): Completable {
        return remoteDataSource.logoutCurrentUser()
    }

    override fun createUser(email: String, password: String, user: User) : Completable {
        return remoteDataSource.createUser(email, password, user)
    }

    override fun updateEmail(newEmail: String): Completable {
        return remoteDataSource.updateEmail(newEmail)
    }

    override fun resetPassword(): Completable {
        return remoteDataSource.resetPassword()
    }

    override fun deleteCurrentUser(): Completable {
        return remoteDataSource.deleteCurrentUser()
    }

}