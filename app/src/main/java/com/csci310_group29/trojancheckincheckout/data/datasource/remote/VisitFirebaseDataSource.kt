package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
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
}