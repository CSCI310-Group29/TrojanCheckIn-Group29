package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.fake.UserFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.UserFirebaseDataSource
import io.reactivex.Completable
import io.reactivex.Single

class UserRepoImpl(fake: Boolean = false): UserRepository {
    private val remoteDataSource = if (!fake) UserFirebaseDataSource() else UserFakeDataSource()

    override fun updateProfile(firstName: String?, lastName: String?, major: String?, studentId: String?): Completable {
        return remoteDataSource.updateProfile(firstName, lastName, major, studentId)
    }

    override fun updateProfilePicture(profilePicture: Bitmap): Completable {
        return remoteDataSource.updateProfilePicture(profilePicture)
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        return remoteDataSource.queryCheckedInUsers(buildingName, user)
    }
}