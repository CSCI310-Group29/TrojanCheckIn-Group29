package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(private val remoteDataSource: AuthFirebaseDataSource):
        AuthRepository {

    override fun getCurrentUser(): Single<AuthEntity> {
        return remoteDataSource.getCurrentUser()
    }

    override fun logoutCurrentUser(): Completable {
        return remoteDataSource.logoutCurrentUser()
    }

    override fun updatePhotoURL(url: String): Completable {
        TODO("Not yet implemented")
    }


    override fun createUser(email: String, password: String) : Completable {
        return remoteDataSource.createUser(email, password)
    }

    override fun loginUser(email: String, password: String): Completable {
        TODO("Not yet implemented")
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