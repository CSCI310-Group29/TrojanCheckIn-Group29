package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserUseCases @Inject constructor(private val authRepo: AuthRepository,
                                       private val userRepo: UserRepository,
                                       private val pictureRepo: PicturesRepository) {

    fun getCurrentlyLoggedInUser(picture: Boolean = true): Single<User> {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.getUser(authEntity.id)
                            .flatMap {userEntity ->
                                    if (picture && authEntity.photoURL != null) {
                                        pictureRepo.getProfilePicture(authEntity.photoURL)
                                                .flatMap { picture ->
                                                    Single.just(buildUser(authEntity, userEntity, null, picture))
                                                }
                                    } else {
                                        Single.just(buildUser(authEntity, userEntity, null))
                                    }
                                }
                            }
    }

    fun updateProfilePicture(picture: ByteArray): Completable {
        return pictureRepo.updateProfilePicture(picture)
                .flatMapCompletable {url ->
                    userRepo.updatePhotoURL(url)
                }
    }

    fun updateProfile(fields: HashMap<String, Any>): Completable {
        TODO("Not yet implemented")
    }

    fun searchCheckedInUsers(): Single<List<User>> {
        TODO("Not yet implemented")
    }


    private fun buildUser(authEntity: AuthEntity, userEntity: UserEntity, visitEntity: VisitEntity?, picture: ByteArray? = null): User {

        return User(authEntity.id, userEntity.isStudent,
                authEntity.email, userEntity.firstName,
                userEntity.lastName, userEntity.major,
                userEntity.isCheckedIn, userEntity.studentId, picture)
    }
}