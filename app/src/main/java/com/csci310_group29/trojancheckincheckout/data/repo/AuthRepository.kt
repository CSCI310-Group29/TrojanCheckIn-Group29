package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import io.reactivex.Completable
import io.reactivex.Observable

interface AuthRepository {

    fun getCurrentUser(): Observable<User?>

    fun getUserWithCredentials(email: String, password: String): Observable<User?>

    fun createUser(email: String, password: String, user: User): Completable

    fun updateEmail(newEmail: String): Observable<User?>

    fun resetPassword(): Observable<User?>

    fun deleteCurrentUser(): Completable
}