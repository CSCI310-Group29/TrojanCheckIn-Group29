package com.csci310_group29.trojancheckincheckout.data.fake

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Single

class AuthFakeDataSource: AuthRepository {

    override fun getCurrentUser(): Single<User> {
        return Single.create { emitter ->
            emitter.onSuccess(User(
                id = "1234",
                isStudent = true,
                firstName = "Tommy",
                lastName = "Trojan",
                major = "Computer Science",
                studentId = "1234"
            ))
        }
    }

    override fun getUserWithCredentials(email: String, password: String, ): Single<User> {
        return Single.create { emitter ->
            emitter.onSuccess(User(
                id = "1234",
                isStudent = true,
                firstName = "Tommy",
                lastName = "Trojan",
                major = "Computer Science",
                studentId = "1234"
            ))
        }
    }

    override fun logoutCurrentUser(): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

    override fun createUser(email: String, password: String, user: User) : Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }

    }

    override fun updateEmail(newEmail: String): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

    override fun resetPassword(): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }
    }

    override fun deleteCurrentUser(): Completable {
        return Completable.create { emitter ->
            emitter.onComplete()
        }

    }
}