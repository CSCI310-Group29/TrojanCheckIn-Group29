package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.User

interface UserRepository {
    fun updateProfile(firstName: String? = null,
                      lastName: String? = null,
                      major: String? = null,
                      studentId: String? = null): User

    fun updateProfilePicture(profilePicture: Bitmap)

    fun queryCheckedInUsers(buildingName: String? = null, user: User): List<User>
}