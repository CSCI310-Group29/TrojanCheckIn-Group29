package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

class AuthFakeDataSource @Inject constructor(): AuthRepository {
    override fun getCurrentUser(): Single<AuthEntity> {
        return Single.just(AuthEntity("123", "tommy@usc.edu"))
    }

    override fun logoutCurrentUser(): Completable {
        return Completable.complete()
    }

    override fun createUser(email: String, password: String): Single<AuthEntity> {
        return Single.just(AuthEntity("123", "tommy@usc.edu"))
    }

    override fun loginUser(email: String, password: String): Completable {
        return Completable.complete()
    }

    override fun updateEmail(newEmail: String): Completable {
        return Completable.complete()
    }

    override fun resetPassword(): Completable {
        return Completable.complete()
    }

    override fun deleteCurrentUser(): Completable {
        return Completable.complete()
    }
}