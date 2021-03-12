package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Observable

class AuthRepoImpl: AuthRepository {
    private val remoteDataSource = AuthRemoteDataSource()

    override fun getCurrentUser(): Observable<User?> {
        TODO("Not yet implemented")
    }

    override fun getUserWithCredentials(email: String, password: String): Observable<User?> {
        return remoteDataSource.getUserWithCredentials(email, password)
    }

    override fun createUser(email: String, password: String, user: User) : Completable {
        return remoteDataSource.createUser(email, password, user)
    }

    override fun updateEmail(newEmail: String): Observable<User?> {
        TODO("Not yet implemented")
    }

    override fun resetPassword(): Observable<User?> {
        TODO("Not yet implemented")
    }

    override fun deleteCurrentUser(): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

}