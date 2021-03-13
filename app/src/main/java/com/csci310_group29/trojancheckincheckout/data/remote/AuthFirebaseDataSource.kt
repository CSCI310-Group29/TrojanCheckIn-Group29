package com.csci310_group29.trojancheckincheckout.data.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

class AuthFirebaseDataSource: AuthRepository {

    private var TAG = "AuthRemoteDataSource"
    private var auth = Firebase.auth
    private var db = Firebase.firestore

    override fun getCurrentUser(): Single<User> {
        return Single.create { emitter ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val uid = currentUser.uid
                val userRef = db.collection("users").document(uid)
                userRef.get()
                    .addOnSuccessListener { userDocument ->
                        val firstName = userDocument.get("firstName") as String
                        val lastName = userDocument.get("lastName") as String
                        val isStudent = userDocument.get("isStudent") as Boolean
                        val major = userDocument.get("major") as String
                        val studentId = userDocument.get("studentId") as String

                        emitter.onSuccess(User(uid, isStudent, firstName, lastName, major, studentId))
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            }
        }
    }

    override fun getUserWithCredentials(email: String, password: String, ): Single<User> {
        return Single.create { emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    if (user != null) {
                        val userRef = db.collection("users").document(user.uid)
                        userRef.get()
                            .addOnSuccessListener { userDocument ->
                                val firstName = userDocument.get("firstName") as String
                                val lastName = userDocument.get("lastName") as String
                                val isStudent = userDocument.get("isStudent") as Boolean
                                val major = userDocument.get("major") as String
                                val studentId = userDocument.get("studentId") as String
                                emitter.onSuccess(User(user.uid, isStudent, firstName, lastName, major, studentId))
                            }
                    }
                    else {
                        emitter.onError(Exception("Error with getting user from firestore"))
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
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

    override fun createUser(email: String, password: String, user: User) : Completable {
        return Completable.create { emitter ->
            Log.d(TAG, "creating user")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "user was created")
                    val authUser = auth.currentUser
                    if (authUser != null) {
                        val userData = hashMapOf(
                            "firstName" to user.firstName,
                            "lastName" to user.lastName,
                            "major" to user.major,
                            "studentId" to user.studentId,
                            "isStudent" to user.isStudent
                        )
                        db.collection("users").document(authUser.uid).set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "firestore user was created")
                                emitter.onComplete()
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "failed to create firestore user")
                                emitter.onError(exception)
                            }
                    }
                    else {
                        emitter.onError(Exception("Sign up not successful"))
                    }
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