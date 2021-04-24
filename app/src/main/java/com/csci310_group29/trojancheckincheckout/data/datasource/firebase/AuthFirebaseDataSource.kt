package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthFirebaseDataSource @Inject constructor(private val auth: FirebaseAuth): AuthRepository {

    companion object {
        val TAG = "AuthRemoteDataSource"
    }

    override fun getCurrentUser(): Single<AuthEntity> {
        return Single.create { emitter ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val uid = currentUser.uid
                emitter.onSuccess(AuthEntity(uid, currentUser.email))
            } else {
                emitter.onError(Exception("not logged in"))
            }
        }
    }

    override fun logoutCurrentUser(): Completable {
        Log.d(TAG, "logging out user")
        return Completable.create { emitter ->
            Log.d(TAG, "logging out user")
            if (auth.currentUser == null) {
                emitter.onError(Exception("User is not signed in"))
            } else {
                auth.signOut()
                auth.addAuthStateListener(object: FirebaseAuth.AuthStateListener {
                    override fun onAuthStateChanged(p0: FirebaseAuth) {
                        if (p0.currentUser == null) {
                            Log.d(TAG, "user was logged out")
                            emitter.onComplete()
                        }
                    }

                })

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

    override fun createUser(email: String, password: String) : Single<AuthEntity> {
        return Single.create { emitter ->
            Log.d(TAG, "creating user")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val user = authResult.user!!
                    emitter.onSuccess(AuthEntity(user.uid, user.email))
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
            val user = auth.currentUser
            if(user == null) {
                Log.d(TAG, "Cannot reset password, user is not signed in")
                emitter.onError(Exception("Cannot reset password, user is not signed in"))
            }
            else {
                Firebase.auth.sendPasswordResetEmail(user.email)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful) {
                            Log.d(TAG, "Reset password email sent")
                        }
                    }
            }
        }
    }

    override fun deleteCurrentUser(): Completable {
        return Completable.create { emitter ->
            val user = auth.currentUser
            if (user == null) {
                Log.d(TAG, "user not signed in")
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
