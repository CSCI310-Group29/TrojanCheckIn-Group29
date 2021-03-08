package com.example.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.example.trojancheckincheckout.data.models.User

class UserRepoImpl: UserRepository {
    override fun updateProfile(firstName: String?, lastName: String?, major: String?, studentId: String?): User {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(profilePicture: Bitmap) {
        TODO("Not yet implemented")
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): List<User> {
        TODO("Not yet implemented")
    }
}