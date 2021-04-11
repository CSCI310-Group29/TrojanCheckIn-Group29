package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import java.util.*
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
        return Observable.create { emitter ->
            val query = db.collection("users").whereEqualTo("checkedInBuildingId", buildingId)
            query.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.d(TAG, "get users in building failed")
                    emitter.onError(e)
                }
                if (snapshots != null) {
                    val userEntities: List<UserEntity> = snapshots.toObjects<UserEntity>()
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

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Observable<UserEntity> {
        var query = db.collectionGroup("visits")
        if (visitQuery.startCheckIn != null) query =
            query.whereGreaterThanOrEqualTo("checkIn", visitQuery.startCheckIn!!)
        if (visitQuery.endCheckIn != null) query =
            query.whereLessThanOrEqualTo("checkIn", visitQuery.endCheckIn!!)
        if (visitQuery.buildingId != null) query =
            query.whereEqualTo("buildingId", visitQuery.buildingId)
        return Observable.create { emitter ->
            query.get()
                .addOnSuccessListener { snapshots ->
                    Observable.fromIterable(snapshots)
                        .concatMap { visitSnap ->
                            val userRef = visitSnap.reference.parent
                            Observable.create { emitter2: ObservableEmitter<UserEntity> ->
                                userRef.get()
                                    .addOnSuccessListener { userSnap ->
                                        val userEntities = userSnap.toObjects<UserEntity>()
                                        if (userEntities.isNotEmpty()) {
                                            val userEntity = userSnap.toObjects<UserEntity>()[0]
                                            emitter2.onNext(userEntity)
                                            emitter2.onComplete()
                                        }
                                    }
                                    .addOnFailureListener { e -> emitter.onError(e) }
                            }
                        }
                        .filter { userEntity ->
                            checkUser(userEntity, userQuery)
                        }
                        .flatMapCompletable { userEntity ->
                            emitter.onNext(userEntity)
                            Completable.complete()
                        }.doOnComplete { emitter.onComplete() }
                        .doOnError { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    private fun checkUser(userEntity: UserEntity, userQuery: UserQuery): Boolean {
        if (userQuery.firstName != null && userQuery.firstName != userEntity.firstName)
            return false
        if (userQuery.lastName != null && userQuery.lastName != userEntity.lastName)
            return false
        if (userQuery.isCheckedIn != null) {
            if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn)
                return false
            if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn)
                return false
        }
        if (userQuery.major != null && userQuery.major == userEntity.major)
            return false
        if (userQuery.isStudent != null && userQuery.isStudent != userEntity.isStudent)
            return false
        if (userQuery.studentId != null && userQuery.studentId != userEntity.studentId)
            return false
        return true
    }
}