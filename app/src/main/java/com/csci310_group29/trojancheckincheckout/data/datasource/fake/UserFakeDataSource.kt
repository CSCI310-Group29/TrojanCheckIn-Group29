package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserFakeDataSource @Inject constructor(): UserRepository {
    override fun getUser(userId: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun setCheckedIn(userId: String, checkIn: Boolean): Single<UserEntity> {
        return Single.just(UserEntity("123", true, "Tommy", "Trojan", "Compute Science", checkIn, "1234", "exampleURL"))
    }

    override fun updateProfile(firstName: String?, lastName: String?, major: String?, studentId: String?): Completable {
        TODO("Not yet implemented")
    }

    override fun updatePhotoURL(url: String): Completable {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(profilePicture: Bitmap): Completable {
        TODO("Not yet implemented")
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        TODO("Not yet implemented")
    }
}