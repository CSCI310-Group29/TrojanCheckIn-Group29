package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

open class AuthUseCases @Inject constructor(@Named("Repo") private val authRepo: AuthRepository,
                                            @Named("Repo") private val pictureRepo: PicturesRepository,
                                            @Named("Repo") private val userRepo: UserRepository,
                                            private val userUseCases: UserUseCases) {

    open fun getUserAuth(): Single<AuthEntity> {
        /*
        Gets the currently logged in user of the app.

            Returns:
                A single that emits an AuthEntity if the user is logged in,
                and an error if the user cannot be found, which means the user is not
                logged in.
         */
        return authRepo.getCurrentUser()
    }


    open fun signup(email: String, password: String, userEntity: UserEntity): Completable {
        /*
        Signs up the user with an email and password. Also creates the user profile based on
        the fields in the userEntity object. The data in the userEntity object will be the exact
        same as the data of the user document in the database.

            Params:
                email: String representing the email of the user
                password: String representing the password of the user

            Returns:
                Completable that emits completion if the user has successfully signed up, and an
                error otherwise.
         */

        // call AuthRepository to create authentication account
        return authRepo.createUser(email, password)
            .flatMapCompletable {authEntity ->
                userEntity.id = authEntity.id
                // call UserRepository to create a user object in the database
                userRepo.create(userEntity).toCompletable()
            }
    }

    open fun login(email: String, password: String): Single<User> {
        /*
        Signs in the user using the provided email and password.

            Params:
                email: String representing the email of the user
                password: String representing the password of the user

            Returns:
                Single that emits a User object if the user has successfully signed in, or
                an error if the credentials were invalid
         */

        // call AuthRepository to login to user accountt
        return authRepo.loginUser(email, password)
                .toSingleDefault(false)
                // call UserUseCases to get the currently logged in user
                .flatMap { userUseCases.getCurrentlyLoggedInUser() }
    }

    open fun logout(): Completable {
        /*
        Signs out the currently logged in user.

            Returns:
                Completable that emits completion if the user has successfully signed out,
                and an error otherwise.
         */

        // call AuthRepository to logout the user
        return authRepo.logoutCurrentUser()
    }

    open fun deleteAccount(): Completable {
        /*
        Deletes the account of the currently logged in user. The user will also be
        removed from the database and will no longer be visible to managers.

            Returns:
                Completable that emits completion if the user has successfully deleted the
                account, or an error otherwise.
         */

        // call UserUseCases to retrieve the currently logged in user
        return userUseCases.getCurrentlyLoggedInUser()
            .flatMapCompletable { user ->
                // call userRepository to delete the user document in the database
                userRepo.delete(user.id)
            }
                // call AuthRepository to delete the user account
            .andThen(authRepo.deleteCurrentUser())
    }
}