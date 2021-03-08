package com.example.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.example.trojancheckincheckout.data.models.User

interface UserRepository {
    fun updateProfile(firstName: String? = null,
                      lastName: String? = null,
                      major: String? = null,
                      studentId: String? = null): User

    fun updateProfilePicture(profilePicture: Bitmap)

    fun queryCheckedInUsers(buildingName: String? = null, user: User): List<User>
}