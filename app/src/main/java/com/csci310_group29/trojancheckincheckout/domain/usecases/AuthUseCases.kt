package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthUseCases {
    private val authRepo: AuthRepository = AuthRepoImpl()
    private val pictureRepo: PicturesRepository = PicturesRepoImpl()

    fun getUserAuth(): Single<AuthEntity> {
        return authRepo.getCurrentUser()
    }

    fun updateProfilePicture(picture: Bitmap): Completable {
        return pictureRepo.updateProfilePicture(picture)
                .flatMapCompletable {url ->
                    authRepo.updatePhotoURL(url)
                }
    }

    fun signup(email: String, password: String): Completable {
        return authRepo.createUser(email, password)
    }

    fun login(email: String, password: String): Completable {
        return authRepo.loginUser(email, password)
    }

    fun logout(): Completable {
        return authRepo.logoutCurrentUser()
    }

    fun deleteAccount(): Completable {
        return authRepo.deleteCurrentUser()
    }
}