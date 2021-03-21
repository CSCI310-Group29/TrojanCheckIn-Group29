package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BuildingFirebaseDataSource @Inject constructor(): BuildingRepository {

    companion object {
        private val TAG = "BuildingFirebaseDataSource"
        private val EMULATOR = false
    }

    private val db = Firebase.firestore

    init {
        if (EMULATOR) db.useEmulator("10.0.2.2", 8080)
    }

    override fun get(id: String): Single<BuildingEntity> {
        return Single.create { emitter ->
            val buildingRef = db.collection("buildings").document(id)
            buildingRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    emitter.onSuccess(documentSnapshot.toObject<BuildingEntity>()!!)
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
        TODO("Not yet implemented")
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

    override fun incrementNumPeople(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        return Single.create { emitter ->
            var building: BuildingEntity? = null
            val buildingRef = db.collection("buildings").document(buildingId)
            db.runTransaction { transaction ->
                val documentSnapshot = transaction.get(buildingRef)
                if ((documentSnapshot.get("numPeople") as Long) < (documentSnapshot.get("capacity") as Long)) {
                    transaction.update(buildingRef, "numPeople", FieldValue.increment(incrementCount.toDouble()))
                    building = transaction.get(buildingRef).toObject<BuildingEntity>()!!
                } else {
                    throw FirebaseFirestoreException("capacity is full", FirebaseFirestoreException.Code.ABORTED)
                }
            }
                .addOnSuccessListener { emitter.onSuccess(building!!) }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.message.toString())
                    emitter.onError(e)
                }
        }
    }

    override fun updateCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
    }
}