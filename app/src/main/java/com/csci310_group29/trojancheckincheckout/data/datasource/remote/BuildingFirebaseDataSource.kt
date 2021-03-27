package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BuildingFirebaseDataSource @Inject constructor(private val db: FirebaseFirestore): BuildingRepository {

    companion object {
        private val TAG = "BuildingFirebaseDataSource"
    }

    override fun get(id: String): Single<BuildingEntity> {
        return Single.create { emitter ->
            val buildingRef = db.collection("buildings").document(id)
            buildingRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    Log.d(TAG, "building info id success")
                    emitter.onSuccess(documentSnapshot.toObject<BuildingEntity>()!!)
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun getAll(): Single<List<BuildingEntity>> {
        return Single.create { emitter ->
            db.collection("buildings").get()
                .addOnSuccessListener { snapshots ->
                    emitter.onSuccess(snapshots.toObjects<BuildingEntity>())
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun observe(id: String): Observable<BuildingEntity> {
        return Observable.create { emitter ->
            val buildingRef = db.collection("buildings").document(id)
            buildingRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        emitter.onNext(snapshot.toObject<BuildingEntity>()!!)
                    } else {
                        emitter.onError(FirebaseFirestoreException("building does not exist", FirebaseFirestoreException.Code.UNAVAILABLE))
                    }
                }
            }
        }
    }

    override fun observeByName(buildingName: String): Observable<BuildingEntity> {
        return Observable.create { emitter ->
            val buildingRef = db.collection("buildings").whereEqualTo("buildingName", buildingName)
            buildingRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    if (snapshot != null) {
                        val buildingEntities = snapshot.toObjects<BuildingEntity>()
                        if (buildingEntities.isEmpty()) {
                            emitter.onError(Exception("no such building exists"))
                        }
                        val buildingEntity = buildingEntities[0]
                        emitter.onNext(buildingEntity)
                    }
                }
            }
        }
    }

    override fun getByName(buildingName: String): Single<BuildingEntity> {
        return Single.create { emitter ->
            val buildingRef = db.collection("buildings").whereEqualTo("buildingName", buildingName)
            buildingRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val buildingEntities = documentSnapshot.toObjects<BuildingEntity>()
                    if (buildingEntities.isEmpty()) {
                        emitter.onError(FirebaseFirestoreException("no building found", FirebaseFirestoreException.Code.UNAVAILABLE))
                    } else {
                        emitter.onSuccess(buildingEntities[0])
                    }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun incrementNumPeople(buildingId: String, incrementCount: Double): Single<BuildingEntity> {
        return Single.create { emitter ->
            val buildingRef = db.collection("buildings").document(buildingId)
            Log.d(TAG, "incrementing num people")
            db.runTransaction { transaction ->
                val documentSnapshot = transaction.get(buildingRef)
                if ((documentSnapshot.getDouble("numPeople")!!) < (documentSnapshot.getDouble("capacity")!!)) {
                    transaction.update(buildingRef, "numPeople", FieldValue.increment(incrementCount))
                    Log.d(TAG,"transaction successfull")
                } else {
                    throw FirebaseFirestoreException("capacity is full", FirebaseFirestoreException.Code.ABORTED)
                }
            }
                .addOnSuccessListener {
                    buildingRef.get()
                        .addOnSuccessListener { snap ->
                            emitter.onSuccess(snap.toObject<BuildingEntity>()!!)
                        }
                        .addOnFailureListener { e -> emitter.onError(e) }
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.message.toString())
                    emitter.onError(e)
                }
        }
    }



    override fun updateCapacities(buildingCapacities: HashMap<String, Double>): Completable {
        return Completable.create { emitter ->
            db.runTransaction { transaction ->
                val snapshots: HashMap<DocumentSnapshot, Double> = hashMapOf()
                buildingCapacities.forEach { (buildingId, capacity) ->
                    val buildingRef = db.collection("buildings").document(buildingId)
                    val snapshot = transaction.get(buildingRef)
                    snapshots[snapshot] = capacity
                }
                snapshots.forEach { (snapshot, capacity) ->
                    val ref = snapshot.reference
                    if (snapshot.getDouble("numPeople")!! > capacity) {
                        throw FirebaseFirestoreException("capacity is too low", FirebaseFirestoreException.Code.ABORTED)
                    }
                    transaction.update(ref, "capaacity", capacity)
                }
            }
                .addOnSuccessListener { transaction ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun updateSingleCapacity(buildingId: String, capacity: Double): Completable {
        return Completable.create { emitter ->
            val buildingRef = db.collection("buildings").document(buildingId)
            db.runTransaction { transaction ->
                val snap = transaction.get(buildingRef)
                if (capacity >= snap.getDouble("numPeople")!!) {
                    transaction.update(buildingRef, "capacity", capacity)
                } else {
                    throw FirebaseFirestoreException("capacity is too low", FirebaseFirestoreException.Code.ABORTED)
                }
            }
                .addOnSuccessListener { transaction ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }
}