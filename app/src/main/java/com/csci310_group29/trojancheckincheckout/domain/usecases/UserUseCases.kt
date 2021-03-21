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
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.rpc.context.AttributeContext
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

open class UserUseCases @Inject constructor(
    @Named("Repo") private val authRepo: AuthRepository,
    @Named("Repo") private val userRepo: UserRepository,
    @Named("Repo") private val pictureRepo: PicturesRepository,
    private val buildingUseCases: BuildingUseCases) {

    fun getCurrentlyLoggedInUser(picture: Boolean = true): Single<User> {
        return authRepo.getCurrentUser()
            .flatMap { authEntity ->
                userRepo.get(authEntity.id)
                    .flatMap { userEntity ->
                        if (userEntity.checkedInBuildingId != null) {
                            buildingUseCases.getBuildingInfoById(userEntity.checkedInBuildingId!!)
                                .flatMap { building ->
                                    getPictureAndUser(picture, authEntity, building, userEntity)
                                }
                        } else {
                            getPictureAndUser(picture, authEntity, null, userEntity)
                        }
                    }
            }
    }

    fun updateProfilePicture(picture: ByteArray): Single<User> {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable {user ->
                    if (user.photoUrl == null) {
                        user.photoUrl = "profilePictures/" + user.id + ".jpg"
                        userRepo.update(user).toSingleDefault(false)
                            .flatMapCompletable { pictureRepo.create(user.photoUrl!!, picture)}
                    } else {
                        pictureRepo.create(user.photoUrl!!, picture)
                    }
                }
            .toSingleDefault(false)
            .flatMap { getCurrentlyLoggedInUser()}
    }

    fun updateProfile(userEntity: UserEntity): Single<User> {
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable { user ->
                    userRepo.update(overwrite(user, userEntity))
                }
            .toSingleDefault(false)
            .flatMap { getCurrentlyLoggedInUser() }
    }

    fun searchCheckedInUsers(): Single<List<User>> {
        TODO("Not yet implemented")
    }

    private fun getPictureAndUser(picture: Boolean, authEntity: AuthEntity, building: Building?, userEntity: UserEntity): Single<User> {
        return if (picture && userEntity.photoUrl != null) {
            pictureRepo.get(userEntity.photoUrl!!)
                .flatMap { pictureByteArray ->
                    Single.just(buildUser(authEntity, userEntity, building, pictureByteArray))
                }
        } else {
            Single.just(buildUser(authEntity, userEntity, building, null))
        }
    }

    private fun buildUser(authEntity: AuthEntity, userEntity: UserEntity, building: Building?, picture: ByteArray? = null): User {

        return User(authEntity.id, userEntity.isStudent,
                authEntity.email, userEntity.firstName,
                userEntity.lastName, userEntity.major,
            building, userEntity.studentId, picture)
    }

    private fun overwrite(curr: UserEntity, truth: UserEntity): UserEntity {
        return UserEntity(
            truth.id ?: curr.id,
            truth.isStudent ?: curr.isStudent,
            truth.firstName ?: curr.firstName,
            truth.lastName ?: curr.lastName,
            truth.major ?: curr.major,
            truth.checkedInBuildingId ?: curr.checkedInBuildingId,
            truth.studentId ?: curr.studentId,
            truth.photoUrl ?: curr.photoUrl
        )
    }
}