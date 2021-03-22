package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.AuthFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class AuthRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: AuthRepository):
        AuthRepository {

    override fun getCurrentUser(): Single<AuthEntity> {
        return remoteDataSource.getCurrentUser()
    }


    override fun logoutCurrentUser(): Completable {
        return remoteDataSource.logoutCurrentUser()
    }


    override fun createUser(email: String, password: String) : Single<AuthEntity> {
        return remoteDataSource.createUser(email, password)
    }

    override fun loginUser(email: String, password: String): Completable {
        return remoteDataSource.loginUser(email, password)
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