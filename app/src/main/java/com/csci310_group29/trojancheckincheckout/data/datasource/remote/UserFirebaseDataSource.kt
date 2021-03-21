package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
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
        Log.d(TAG, "user firebase remote data source: $id")
        return Single.create { emitter ->
            Log.d(TAG, "getting user")
            val userRef = db.collection("users").document(id)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "successfully read user")
                    val userEntity = documentSnapshot.toObject<UserEntity>()
                    Log.d(TAG, userEntity.toString())
                    emitter.onSuccess(userEntity!!)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.localizedMessage!!)
                    emitter.onError(exception)
                }
        }
    }

    override fun update(userEntity: UserEntity): Completable {
        return Completable.create { emitter ->
            val userRef = db.collection("users").document(userEntity.id!!)
            userRef.set(userEntity)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.localizedMessage!!)
                    emitter.onError(exception)
                }
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

    override fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity> {
        return Single.create { emitter ->
            val userRef = db.collection("users").document(userId)
            userRef.update("checkedInBuildingId", buildingId)
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

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<UserEntity>> {
        var query = db.collectionGroup("visits")
        if (visitQuery.startCheckIn != null) query =
            query.whereGreaterThanOrEqualTo("checkIn", visitQuery.startCheckIn!!)
        if (visitQuery.endCheckIn != null) query =
            query.whereLessThanOrEqualTo("checkIn", visitQuery.endCheckIn!!)
        if (visitQuery.startCheckOut != null) query =
            query.whereGreaterThanOrEqualTo("checkOut", visitQuery.startCheckOut!!)
        if (visitQuery.endCheckOut != null) query =
            query.whereLessThanOrEqualTo("checkOut", visitQuery.endCheckOut!!)
        if (visitQuery.buildingId != null) query =
            query.whereEqualTo("buildingId", visitQuery.buildingId)
        return Single.create { emitter ->
            query.get()
                .addOnSuccessListener { snapshots ->
                    val usersList: MutableList<UserEntity> = mutableListOf()
                    snapshots.forEach { snap ->
                        val userRef = snap.reference.parent
                        userRef.get()
                            .addOnSuccessListener checkingUser@ { userSnap ->
                                val userEntities = userSnap.toObjects<UserEntity>()
                                if (userEntities.isEmpty()) {
                                    val userEntity = userEntities[0]
                                    if (userQuery.firstName != null && userQuery.firstName != userEntity.firstName)
                                        return@checkingUser
                                    if (userQuery.lastName != null && userQuery.lastName != userEntity.lastName)
                                        return@checkingUser
                                    if (userQuery.isCheckedIn != null) {
                                        if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn)
                                            return@checkingUser
                                        if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn)
                                            return@checkingUser
                                    }
                                    if (userQuery.major != null && userQuery.major == userEntity.major)
                                        return@checkingUser
                                    if (userQuery.isStudent != null && userQuery.isStudent != userEntity.isStudent)
                                        return@checkingUser
                                    if (userQuery.studentId != null && userQuery.studentId != userEntity.studentId)
                                        return@checkingUser
                                    usersList.add(userEntity)
                                }
                            }
                            .addOnFailureListener { e -> emitter.onError(e) }
                    }
                    emitter.onSuccess(usersList)
                }
                .addOnFailureListener { e -> emitter.onError(e)}
        }
    }
}