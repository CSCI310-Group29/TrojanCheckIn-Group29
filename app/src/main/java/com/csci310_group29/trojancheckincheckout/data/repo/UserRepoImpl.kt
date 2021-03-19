package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class UserRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: UserRepository): UserRepository {
    override fun getUser(userId: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun setCheckedIn(userId: String, checkIn: Boolean): Single<UserEntity> {
        return remoteDataSource.setCheckedIn(userId, checkIn)
    }

    override fun updateProfile(firstName: String?, lastName: String?, major: String?, studentId: String?): Completable {
        return remoteDataSource.updateProfile(firstName, lastName, major, studentId)
    }

    override fun updatePhotoURL(url: String): Completable {
        return remoteDataSource.updatePhotoURL(url)
    }

    override fun updateProfilePicture(profilePicture: Bitmap): Completable {
        return remoteDataSource.updateProfilePicture(profilePicture)
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        return remoteDataSource.queryCheckedInUsers(buildingName, user)
    }
}