package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.*
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

class VisitFirebaseDataSource @Inject constructor(private val db: FirebaseFirestore): VisitRepository {

    companion object {
        private val TAG = "VisitFirebaseDataSource"
    }

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        Log.d(TAG, "create visit called")
        return Single.create { emitter ->
            val visitRef = db.collection("users")
                .document(userId)
                .collection("visits")
                .document()
            val visitEntity = VisitEntity(visitRef.id, userId, buildingId, Date(), null)
            visitRef.set(visitEntity)
                .addOnSuccessListener {
                    visitRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val visitEntity2 = documentSnapshot.toObject<VisitEntity>()
                            if (visitEntity2 != null) {
                                Log.d(TAG, "successfully created visit")
                                emitter.onSuccess(visitEntity2)
                            } else {
                                emitter.onError(Exception("visit unable to be created"))
                            }
                        }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun get(userId: String, visitId: String): Single<VisitEntity> {
        return Single.create { emitter ->
            val visitRef = db.collection("users")
                .document(userId)
                .collection("visits")
                .document(visitId)
            visitRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val visitEntity = documentSnapshot.toObject<VisitEntity>()
                    if (visitEntity != null) {
                        Log.d(TAG, "successfully created visit")
                        emitter.onSuccess(visitEntity)
                    } else {
                        emitter.onError(Exception("visit unable to be created"))
                    }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun delete(userId: String, visitId: String): Completable {
        return Completable.create { emitter ->
            val ref = db.collection("users").document(userId).collection("visits").document(visitId)
            ref.delete()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        return Single.create { emitter ->
            val visitRef = db.collection("users")
                .document(userId)
                .collection("visits")
                .orderBy("checkIn")
                .limitToLast(1)
            visitRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val visitEntities = documentSnapshot.toObjects<VisitEntity>()
                    if (visitEntities.isEmpty()) emitter.onError(Exception("No visits found"))
                    else emitter.onSuccess(visitEntities[0])
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity> {
        return Single.create { emitter ->
            val visitRef = db.collection("users")
                .document(userId)
                .collection("visits")
                .document(visitId)
            visitRef.update("checkOut", Date())
                .addOnSuccessListener {
                    visitRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val visitEntity = documentSnapshot.toObject<VisitEntity>()
                            if (visitEntity != null) {
                                Log.d(TAG, "successfully created visit")
                                emitter.onSuccess(visitEntity)
                            } else {
                                emitter.onError(Exception("cannot get visit"))
                            }
                        }
                        .addOnFailureListener { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<VisitEntity>> {
        Log.d(TAG, "getting user visit history")
        var query = db.collection("users")
            .document(userId)
            .collection("visits")
            .orderBy("checkOut", Query.Direction.DESCENDING)
        if (visitQuery.startCheckIn != null) query =
            query.whereGreaterThanOrEqualTo("checkIn", visitQuery.startCheckIn!!)
        if (visitQuery.endCheckIn != null) query =
            query.whereLessThanOrEqualTo("checkIn", visitQuery.endCheckIn!!)
        if (visitQuery.buildingId != null) query =
            query.whereEqualTo("buildingId", visitQuery.buildingId)
        return Single.create { emitter ->
            query.get()
                .addOnSuccessListener { snapshots ->
                    Log.d(TAG, "visit history success")
                    val visitEntities = snapshots.toObjects<VisitEntity>()
                    Log.d(TAG, "data returned: ${visitEntities.size}")
                    Log.d(TAG, "$visitEntities")
                    emitter.onSuccess(snapshots.toObjects<VisitEntity>())
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun runCheckInTransaction(userId: String, buildingId: String): Single<VisitEntity> {
        return Single.create { emitter ->
            val buildingRef = db.collection("buildings").document(buildingId)
            val userRef = db.collection("users").document(userId)
            val visitRef = userRef.collection("visits").document()
            db.runTransaction { transaction ->
                val buildingSnap = transaction.get(buildingRef)
                val numPeople = buildingSnap.getDouble("numPeople")!!
                val capacity = buildingSnap.getDouble("capacity")!!
                if (numPeople < capacity) {
                    transaction.update(buildingRef, "numPeople", FieldValue.increment(1.0))
                    transaction.update(userRef, "checkedInBuildingId", buildingId)
                    val visitEntity = VisitEntity(null, userId, buildingId, Date(), null)
                    transaction.set(visitRef, visitEntity)
                } else {
                    throw FirebaseFirestoreException("capacity is full", FirebaseFirestoreException.Code.ABORTED)
                }
            }
                .addOnSuccessListener {
                    visitRef.get()
                        .addOnSuccessListener { snap ->
                            val visitEntity = snap.toObject<VisitEntity>()
                            if (visitEntity != null) {
                                emitter.onSuccess(visitEntity)
                            } else {
                                emitter.onError(Exception("visit unable to be created"))
                            }
                        }
                        .addOnFailureListener { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun runCheckOutTransaction(userId: String, visitId: String, buildingId: String): Single<VisitEntity> {
        return Single.create { emitter ->
            val userRef = db.collection("users").document(userId)
            val visitRef = userRef.collection("visits").document(visitId)
            val buildingRef = db.collection("buildings").document(buildingId)
            db.runBatch { batch ->
                batch.update(buildingRef, "numPeople", FieldValue.increment(-1.0))
                batch.update(userRef, "checkedInBuildingId", null)
                batch.update(visitRef, "checkOut", Date())
            }
                .addOnSuccessListener {
                    visitRef.get()
                        .addOnSuccessListener { visitSnap ->
                            val visitEntity = visitSnap.toObject<VisitEntity>()
                            if (visitEntity != null) {
                                emitter.onSuccess(visitEntity)
                            } else {
                                emitter.onError(Exception("visit could be retrieved"))
                            }
                        }
                        .addOnFailureListener { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun query(visitQuery: VisitQuery): Single<List<VisitEntity>> {
        Log.d(TAG, "querying for visits")
        var query = db.collectionGroup("visits")
        if (visitQuery.startCheckIn != null) query =
            query.whereGreaterThanOrEqualTo("checkIn", visitQuery.startCheckIn!!)
        if (visitQuery.endCheckIn != null) query =
            query.whereLessThanOrEqualTo("checkIn", visitQuery.endCheckIn!!)
        if (visitQuery.buildingId != null) query =
            query.whereEqualTo("buildingId", visitQuery.buildingId)
        return Single.create { emitter ->
            query.get()
                .addOnSuccessListener { snapshots ->
                    val visitEntities = snapshots.toObjects<VisitEntity>()
                    emitter.onSuccess(visitEntities)
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.localizedMessage)
                    emitter.onError(e)
                }
            }
        }

    private fun checkUser(userEntity: UserEntity, userQuery: UserQuery): Boolean {
        if (userQuery.firstName != null && userQuery.firstName != userEntity.firstName)
            return false
        if (userQuery.lastName != null && userQuery.lastName != userEntity.lastName)
            return false
        if (userQuery.isCheckedIn != null) {
            if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn!!)
                return false
            if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn!!)
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