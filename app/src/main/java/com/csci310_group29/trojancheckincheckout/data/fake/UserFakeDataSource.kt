package com.csci310_group29.trojancheckincheckout.data.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

class UserFakeDataSource: UserRepository {
    override fun updateProfile(firstName: String?, lastName: String?, major: String?, studentId: String?): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

    override fun updateProfilePicture(profilePicture: Bitmap): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        return Single.create { emitter ->
            val user1 = User(
                    id = "1234",
                    isStudent = true,
                    firstName = "Tommy",
                    lastName = "Trojan",
                    major = "Computer Science",
                    studentId = "1234"
            )
            val user2 = User(
                    id = "5678",
                    isStudent = true,
                    firstName = "Trojan",
                    lastName = "Tommy",
                    major = "Computer Science",
                    studentId = "5678"
            )
            val users = listOf(user1, user2)
            emitter.onSuccess(users)
        }
    }
}