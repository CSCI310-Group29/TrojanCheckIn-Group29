package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface AuthRepository {

    fun getCurrentUser(): Single<User>

    fun getUserWithCredentials(email: String, password: String): Single<User>

    fun logoutCurrentUser(): Completable

    fun createUser(email: String, password: String, user: User): Completable

    fun updateEmail(newEmail: String): Completable

    fun resetPassword(): Completable

    fun deleteCurrentUser(): Completable
}