package com.csci310_group29.trojancheckincheckout.data.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class AuthRemoteDataSource: AuthRepository {

    private final var TAG = "AuthRemoteDataSource"
    private var auth = Firebase.auth
    private var db = Firebase.firestore

    override fun getCurrentUser(callback: AuthRepository.Callback): User? {
        val currentUser = auth.currentUser ?: return null
        val uid = currentUser.uid
        val userRef = db.collection("users").document(uid)
        val task = userRef.get()
        if (!task.isSuccessful) {
            return null
        }
        val userDocument = task.result
        val firstName = userDocument?.get("firstName") as String
        val lastName = userDocument.get("lastName") as String
        val isStudent = userDocument.get("isStudent") as Boolean
        val major = userDocument.get("major") as String
        val studentId = userDocument.get("studentId") as String
        return User(uid, isStudent, firstName, lastName, major, studentId)
    }

    override fun getUserWithCredentials(
        email: String,
        password: String,
        callback: AuthRepository.Callback
    ): User? {
        TODO("Not yet implemented")
        return null
    }

    override fun createUser(email: String, password: String, user: User, callback: AuthRepository.Callback) {
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
                        "studentId" to user.studentId
                    )
                    db.collection("users").document(authUser.uid).set(userData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "firestore user was created")
                            callback.onSuccess(null)
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "failed to create firestore user")
                            callback.onFailure()
                        }
                }
                else {
                    callback.onFailure()
                }
            }
            .addOnFailureListener { event ->
                Log.d(TAG, event.toString())
                callback.onFailure()
            }
    }

    override fun updateEmail(newEmail: String): User? {
        TODO("Not yet implemented")
        return null
    }

    override fun resetPassword(): User? {
        TODO("Not yet implemented")
        return null
    }

    override fun deleteCurrentUser() {
        TODO("Not yet implemented")
    }


}