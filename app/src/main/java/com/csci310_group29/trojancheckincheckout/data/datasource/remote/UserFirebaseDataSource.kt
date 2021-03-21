package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.graphics.Bitmap
import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserFirebaseDataSource @Inject constructor(): UserRepository {

    companion object {
        private val TAG = "UserFirebaseDataSource"
        private val EMULATOR = false
    }

    private val db = Firebase.firestore

    init {
        if (EMULATOR) db.useEmulator("10.0.2.2", 8080)
    }

    override fun get(id: String): Single<UserEntity> {
        return Single.create { emitter ->
            val userRef = db.collection("users").document(id)
            Log.d(TAG, "getting user")
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "successfully read user")
                    val userEntity = documentSnapshot.toObject<UserEntity>()
                    Log.d(TAG, userEntity.toString())
                    emitter.onSuccess(userEntity!!)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun update(userEntity: UserEntity): Completable {
        return Completable.create { emitter ->
            val userRef = db.collection("users").document(userEntity.id!!)
            userRef.set(userEntity)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception -> emitter.onError(exception)}
        }
    }

    override fun delete(id: String): Completable {
        return Completable.create { emitter ->
            val userRef = db.collection("users").document(id)
            userRef.delete()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception -> emitter.onError(exception)}
        }
    }

    override fun create(userEntity: UserEntity): Single<UserEntity> {
        return Single.create { emitter ->
            val userRef = db.collection("users").document(userEntity.id!!)
            userRef.set(userEntity)
                .addOnSuccessListener {
                    Log.d(TAG, "successfully created user")
                    userRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            emitter.onSuccess(documentSnapshot.toObject<UserEntity>()!!)
                        }
                        .addOnFailureListener { exception -> emitter.onError(exception)}
                }
                .addOnFailureListener { exception -> emitter.onError(exception) }
        }
    }

    override fun setCheckedIn(userId: String, checkedIn: Boolean): Single<UserEntity> {
        return Single.create { emitter ->
            val userRef = db.collection("users").document(userId)
            userRef.update("isCheckedIn", checkedIn)
                .addOnSuccessListener {
                    userRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            emitter.onSuccess(documentSnapshot.toObject<UserEntity>()!!)
                        }
                        .addOnFailureListener { e -> emitter.onError(e)}
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun updatePhotoUrl(id: String, url: String): Completable {
        return Completable.create { emitter ->
            val userRef = db.collection("users").document(id)
            userRef.update("photoUrl", url)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception -> emitter.onError(exception)}
        }
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        TODO("Not yet implemented")
    }

}