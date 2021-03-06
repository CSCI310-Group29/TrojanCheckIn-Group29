package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
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
                    val buildingEntity = documentSnapshot.toObject<BuildingEntity>()
                    if (buildingEntity != null) {
                        Log.d(TAG, "building info id success")
                        emitter.onSuccess(buildingEntity)
                    } else {
                        emitter.onError(Exception("Building not found"))
                    }
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun getAll(): Single<List<BuildingEntity>> {
        return Single.create { emitter ->
            db.collection("buildings").get()
                .addOnSuccessListener { snapshots ->
                    val source = if (snapshots.metadata.isFromCache)
                        "local cache"
                    else "server"
                    Log.d(TAG,  "All buildings fetched from $source")
                    Log.d(TAG, "Found ${snapshots.size()} buildings")
                    emitter.onSuccess(snapshots.toObjects<BuildingEntity>())
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }

    override fun create(buildingEntity: BuildingEntity): Single<BuildingEntity> {
        return Single.create { emitter ->
            val ref = db.collection("buildings").document()
            ref.set(buildingEntity)
                .addOnSuccessListener {
                    ref.get()
                        .addOnSuccessListener { snap ->
                            val buildingEntity2 = snap.toObject<BuildingEntity>()
                            if (buildingEntity2 != null) {
                                emitter.onSuccess(buildingEntity2)
                            } else {
                                emitter.onError(Exception("building could not be created"))
                            }
                        }
                        .addOnFailureListener { e ->
                            emitter.onError(e)
                        }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun delete(buildingId: String): Completable {
        return Completable.create { emitter ->
            val buildingRef = db.collection("buildings").document(buildingId)
            db.runTransaction { transaction ->
                val buildingSnap = transaction.get(buildingRef)
                if (buildingSnap.getDouble("numPeople")!! > 0) {
                    throw FirebaseFirestoreException("building contains people", FirebaseFirestoreException.Code.ABORTED)
                } else {
                    transaction.delete(buildingRef)
                }
            }
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e ->  emitter.onError(e) }
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

    override fun observeAll(): Observable<List<BuildingEntity>> {
        return Observable.create { emitter ->
            val ref = db.collection("buildings")
            ref.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.d(TAG, "observe buildings failed")
                    emitter.onError(e)
                }
                if (snapshots != null) {
                    val buildingEntities: List<BuildingEntity> = snapshots.toObjects<BuildingEntity>()
                    Log.d(TAG, "Found ${snapshots.size()} Buildings")
                    emitter.onNext(buildingEntities)
                } else emitter.onError(Exception("Error occurred trying to get buildings"))
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
                        emitter.onError(FirebaseFirestoreException("building $buildingName not found", FirebaseFirestoreException.Code.UNAVAILABLE))
                    } else {
                        emitter.onSuccess(buildingEntities[0])
                    }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }
    
    override fun buildingNameExists(buildingName: String): Single<Boolean> {
        val buildingRef = db.collection("buildings").whereEqualTo("buildingName", buildingName)
        return Single.create { emitter ->
            buildingRef.get()
                .addOnSuccessListener { snap ->
                    if (snap.isEmpty) {
                        emitter.onSuccess(false)
                    } else {
                        val buildings = snap.toObjects<BuildingEntity>()
                        if (buildings.isEmpty()) {
                            emitter.onSuccess(false)
                        } else {
                            emitter.onSuccess(true)
                        }

                    }
                }
                .addOnFailureListener { e ->
                    emitter.onSuccess(false)
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
                    if (snapshot.getDouble("numPeople")!! > capacity) {
                        throw FirebaseFirestoreException("capacity is too low for building ${snapshot.getString("buildingName")}", FirebaseFirestoreException.Code.ABORTED)
                    }
                    snapshots[snapshot] = capacity
                }
                snapshots.forEach { (snapshot, capacity) ->
                    val ref = snapshot.reference
                    transaction.update(ref, "capacity", capacity)
                }
            }
                .addOnSuccessListener { transaction ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.localizedMessage)
                    emitter.onError(e)
                }
        }
    }

    override fun updateSingleCapacity(buildingId: String, capacity: Double): Completable {
        return Completable.create { emitter ->
            val buildingRef = db.collection("buildings").document(buildingId)
            db.runTransaction { transaction ->
                val snap = transaction.get(buildingRef)
                if (!snap.exists()) {
                    throw FirebaseFirestoreException("Building does not exist ${buildingId}", FirebaseFirestoreException.Code.ABORTED)
                }
                if (capacity >= snap.getDouble("numPeople")!!) {
                    transaction.update(buildingRef, "capacity", capacity)
                } else {
                    val b = transaction.get(buildingRef).getString("buildingName")
                    Log.i(TAG, "Capacity Low Building: " + b)
                    throw FirebaseFirestoreException("Capacity too low: $b", FirebaseFirestoreException.Code.ABORTED)
                }
            }
                .addOnSuccessListener { transaction ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e -> emitter.onError(e) }
        }
    }
}
