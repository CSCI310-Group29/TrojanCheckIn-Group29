package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class VisitFirebaseDataSource @Inject constructor(): VisitRepository {

    companion object {
        private val TAG = "VisitFirebaseDataSource"
        private val EMULATOR = false
    }

    private val db = Firebase.firestore

    init {
        if (EMULATOR) db.useEmulator("10.0.2.2", 8080)
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
                            Log.d(TAG, "successfully created visit")
                            emitter.onSuccess(documentSnapshot.toObject<VisitEntity>()!!)
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
                    emitter.onSuccess(documentSnapshot.toObject<VisitEntity>()!!)
                }
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
                            emitter.onSuccess(documentSnapshot.toObject<VisitEntity>()!!)
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
        if (visitQuery.startCheckOut != null) query =
            query.whereGreaterThanOrEqualTo("checkOut", visitQuery.startCheckOut!!)
        if (visitQuery.endCheckOut != null) query =
            query.whereLessThanOrEqualTo("checkOut", visitQuery.endCheckOut!!)
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

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Observable<VisitEntity> {
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
        return Observable.create { emitter ->
            query.get()
                .addOnSuccessListener { snapshots ->
                    Observable.fromIterable(snapshots)
                        .concatMap { visitSnap ->
                            val userRef = visitSnap.reference.parent
                            val visitEntity = visitSnap.toObject<VisitEntity>()
                            Observable.create { emitter2: ObservableEmitter<Pair<VisitEntity, UserEntity>> ->
                                userRef.get()
                                    .addOnSuccessListener { userSnap ->

                                        val userEntities = userSnap.toObjects<UserEntity>()
                                        if (userEntities.isNotEmpty()) {
                                            val userEntity = userSnap.toObjects<UserEntity>()[0]
                                            emitter2.onNext(Pair(visitEntity, userEntity))
                                            emitter2.onComplete()
                                        }
                                    }
                                    .addOnFailureListener { e -> emitter2.onError(e) }
                            }
                        }
                        .filter { pair ->
                             checkUser(pair.second, userQuery)
                        }
                        .concatMap { pair ->
                            Observable.just(pair.first)
                        }
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