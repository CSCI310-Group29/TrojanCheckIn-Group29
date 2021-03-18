package com.csci310_group29.trojancheckincheckout.data.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Single

class AuthFirebaseDataSource: AuthRepository {

    private var TAG = "AuthRemoteDataSource"
    private var auth = Firebase.auth

    override fun getCurrentUser(): Single<AuthEntity> {
        return Single.create { emitter ->
//            val currentUser = auth.currentUser
//            if (currentUser != null) {
//                val uid = currentUser.uid
//                val userRef = db.collection("users").document(uid)
//                userRef.get()
//                    .addOnSuccessListener { userDocument ->
//                        val firstName = userDocument.get("firstName") as String
//                        val lastName = userDocument.get("lastName") as String
//                        val isStudent = userDocument.get("isStudent") as Boolean
//                        val major = userDocument.get("major") as String
//                        val studentId = userDocument.get("studentId") as String
//                        val isCheckedIn = userDocument.get("isCheckedIn") as Boolean
//
//                        emitter.onSuccess(User(uid, isStudent, firstName, lastName, major, isCheckedIn, studentId))
//                    }
//                    .addOnFailureListener { exception ->
//                        emitter.onError(exception)
//                    }
//            }
        }
    }

    override fun logoutCurrentUser(): Completable {
        return Completable.create { emitter ->
            if (auth.currentUser == null) {
                emitter.onError(Exception("User is not signed in"))
            } else {
                auth.signOut()
                emitter.onComplete()
            }

        }
    }

    override fun loginUser(email: String, password: String): Completable {
        return Completable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        emitter.onComplete()
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
        }
    }

    override fun createUser(email: String, password: String) : Completable {
        return Completable.create { emitter ->
            Log.d(TAG, "creating user")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { documentReference ->
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.message.toString())
                    emitter.onError(exception)
                }
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
            val user = auth.currentUser
            if (user == null) {
                emitter.onError(Exception("user is not currently signed in"))
            }
            else {
                user.delete()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            }
        }
    }


}