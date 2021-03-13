package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun updateProfile(firstName: String? = null,
                      lastName: String? = null,
                      major: String? = null,
                      studentId: String? = null): Completable

    fun updateProfilePicture(profilePicture: Bitmap): Completable

    fun queryCheckedInUsers(buildingName: String? = null, user: User): Single<List<User>>
}