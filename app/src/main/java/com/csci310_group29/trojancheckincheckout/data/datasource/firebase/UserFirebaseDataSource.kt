package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserFirebaseDataSource @Inject constructor(private val db: FirebaseFirestore): UserRepository {

    companion object {
        private val TAG = "UserFirebaseDataSource"
    }

    override fun get(id: String): Single<UserEntity> {
        Log.d(TAG, "user firebase remote data source: $id")
        return Single.create { emitter ->
            Log.d(TAG, "getting user")
            val userRef = db.collection("users").document(id)
            Log.d(TAG, id)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "successfully read user")
                    val userEntity = documentSnapshot.toObject<UserEntity>()
                    Log.d(TAG, "$userEntity")
                    Log.d(TAG, userEntity.toString())
                    if (userEntity == null) emitter.onError(Exception("user does not exist"))
                    else emitter.onSuccess(userEntity)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.localizedMessage!!)
                    emitter.onError(exception)
                }
        }
    }

    override fun observeUsersInBuilding(buildingId: String): Observable<List<UserEntity>> {
        Log.d(TAG, "observe users in building executed")
        return Observable.create { emitter ->
            val query = db.collection("users").whereEqualTo("checkedInBuildingId", buildingId)
            query.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.d(TAG, "get users in building failed")
                    emitter.onError(e)
                }
                if (snapshots != null) {
                    val userEntities: List<UserEntity> = snapshots.toObjects<UserEntity>()
                    Log.d(TAG, "Found ${snapshots.size()} Users")
                    emitter.onNext(userEntities)
                } else emitter.onError(Exception("no users found in building"))
            }
        }
    }

    override fun observeUserById(userId: String): Observable<UserEntity> {
        return Observable.create { emitter ->
            val userRef = db.collection("users").document(userId)
            userRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        val userEntity = snapshot.toObject<UserEntity>()
                        if (userEntity == null) {
                            emitter.onError(FirebaseFirestoreException("user does not exist", FirebaseFirestoreException.Code.UNAVAILABLE))
                        } else {
                            emitter.onNext(userEntity)
                        }
                    } else {
                        emitter.onError(FirebaseFirestoreException("user does not exist", FirebaseFirestoreException.Code.UNAVAILABLE))
                    }
                }
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
            val visitsRef = userRef.collection("visits")
            visitsRef.get()
                .addOnSuccessListener { snapshots ->
                    db.runBatch { batch ->
                        snapshots.forEach { snap ->
                            batch.delete(snap.reference)
                        }
                        batch.delete(userRef)
                    }
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun getAll(): Single<List<UserEntity>> {
        return Single.create { emitter ->
            val coll = db.collection("users")
            coll.get()
                .addOnSuccessListener { snapshots ->
                    val userEntities = snapshots.toObjects<UserEntity>()
                    emitter.onSuccess(userEntities)
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun addDeleteField(id: String): Completable {
        return Completable.create { emitter ->
            val userRef = db.collection("users").document(id)
            userRef.update("deleted", true)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
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
                            val userEntity = documentSnapshot.toObject<UserEntity>()
                            if (userEntity == null) emitter.onError(Exception("User was not able to be created"))
                            else emitter.onSuccess(userEntity)
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
                            val userEntity = documentSnapshot.toObject<UserEntity>()
                            if (userEntity == null) emitter.onError(Exception("User was not able to be created"))
                            else emitter.onSuccess(userEntity)
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
}