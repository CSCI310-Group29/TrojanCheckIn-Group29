package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface AuthRepository {

    /*
    Gets the currently logged in user

    Returns
        Single that emits a User object if the user is logged in, or an error if the user is not
            logged in
     */
    fun getCurrentUser(): Single<User>

    /*
    Gets the user using the email and password as credentials

    Params:
        email: String specifying the email of the user
        password: String specifying the raw inputted password of the user

    Returns:
        Single that emits a user if the credentials were valid, or an error if the
            credentials were invalid
     */
    fun getUserWithCredentials(email: String, password: String): Single<User>

    /*
    Logs out the current user

    Returns:
        Completable that emits completion if the user was successfully logged out, or an error
        if the user couldn't be logged out
     */
    fun logoutCurrentUser(): Completable

    /*
    Creates a new user

    Params:
        email: String specifying the email of the user
        password: String specifying the raw input password of user
        user: User object containing extra information about the user

    Returns
        Completable that emits completion if the user was succesfully created, or an error if
            the user couldn't be created
     */
    fun createUser(email: String, password: String, user: User): Completable

    /*
    This function is TBD
     */
    fun updateEmail(newEmail: String): Completable

    /*
    This function is TBD
     */
    fun resetPassword(): Completable

    /*
    Deletes the account of the currently logged in user

    Returns:
        Completable that emits completion if the account was successfully deleted, or an error if
            the account couldn't be deleted.
     */
    fun deleteCurrentUser(): Completable
}