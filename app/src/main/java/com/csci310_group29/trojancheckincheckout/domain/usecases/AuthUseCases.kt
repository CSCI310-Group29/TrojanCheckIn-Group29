package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

open class AuthUseCases @Inject constructor(@Named("Repo") private val authRepo: AuthRepository,
                                            @Named("Repo") private val pictureRepo: PicturesRepository,
                                            private val userUseCases: UserUseCases) {

    protected fun getUserAuth(): Single<AuthEntity> {
        return authRepo.getCurrentUser()
    }

    fun signup(email: String, password: String, userEntity: UserEntity): Completable {
        return authRepo.createUser(email, password)
    }

    fun login(email: String, password: String): Single<User> {
        return authRepo.loginUser(email, password)
                .toSingleDefault(false)
                .flatMap { userUseCases.getCurrentlyLoggedInUser() }
    }

    fun logout(): Completable {
        return authRepo.logoutCurrentUser()
    }

    fun deleteAccount(): Completable {
        return authRepo.deleteCurrentUser()
    }
}