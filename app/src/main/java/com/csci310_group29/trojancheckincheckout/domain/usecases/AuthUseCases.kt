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
        return authRepo.getCurrentUser()
    }


    open fun signup(email: String, password: String, userEntity: UserEntity): Completable {
        return authRepo.createUser(email, password)
            .flatMapCompletable {authEntity ->
                userEntity.id = authEntity.id
                userRepo.create(userEntity).toCompletable()
            }
    }

    open fun login(email: String, password: String): Single<User> {
        return authRepo.loginUser(email, password)
                .toSingleDefault(false)
                .flatMap { userUseCases.getCurrentlyLoggedInUser() }
    }

    open fun logout(): Completable {
        return authRepo.logoutCurrentUser()
    }

    open fun deleteAccount(): Completable {
        return userUseCases.getCurrentlyLoggedInUser()
            .flatMapCompletable { user ->
                userRepo.delete(user.id)
            }
            .andThen(authRepo.deleteCurrentUser())
    }
}