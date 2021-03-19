package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.rpc.context.AttributeContext
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

open class UserUseCases @Inject constructor(@Named("Repo") private val authRepo: AuthRepository,
                                       @Named("Repo") private val userRepo: UserRepository,
                                       @Named("Repo") private val pictureRepo: PicturesRepository) {

    fun getCurrentlyLoggedInUser(picture: Boolean = true): Single<User> {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                            .flatMap {userEntity ->
                                    if (picture && userEntity.photoUrl != null) {
                                        pictureRepo.get(userEntity.photoUrl!!)
                                                .flatMap { picture ->
                                                    Single.just(buildUser(authEntity, userEntity, picture))
                                                }
                                    } else {
                                        Single.just(buildUser(authEntity, userEntity, null))
                                    }
                                }
                            }
    }

    fun updateProfilePicture(picture: ByteArray): Completable {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable {user ->
                    if (user.photoUrl == null) {
                        pictureRepo.create(picture)
                                .flatMapCompletable { url ->
                                    userRepo.updatePhotoUrl(url)
                                }
                    } else {
                        pictureRepo.update(user.photoUrl!!, picture)
                    }
                }
    }

    fun updateProfile(userEntity: UserEntity): Completable {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable { user ->
                    user.merge(userEntity)
                    userRepo.update(userEntity)
                }
    }

    fun searchCheckedInUsers(): Single<List<User>> {
        TODO("Not yet implemented")
    }


    private fun buildUser(authEntity: AuthEntity, userEntity: UserEntity, picture: ByteArray? = null): User {

        return User(authEntity.id, userEntity.isStudent,
                authEntity.email, userEntity.firstName,
                userEntity.lastName, userEntity.major,
                userEntity.isCheckedIn ?: false, userEntity.studentId, picture)
    }
}