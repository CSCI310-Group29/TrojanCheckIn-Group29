package com.csci310_group29.trojancheckincheckout.data.remote

import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Single

class VisitFirebaseDataSource: VisitRepository {
    private val db = Firebase.firestore
//    override fun attemptCheckIn(userId: String, buildingId: String): Single<Visit> {
//        var building: Building?
//        return Single.create { emitter ->
//            db.runTransaction { transaction ->
//                val buildingRef = db.collection("buildings").document(buildingId)
//                val buildingSnapshot = transaction.get(buildingRef)
//                val numStudents = buildingSnapshot.get("numStudents")
//                val buildingCapacity = buildingSnapshot.get("capacity")
//                if (numStudents < buildingCapacity) {
//                    transaction.update(buildingRef, "numStudents", FieldValue.increment(1))
//                    building = buildingSnapshot.toObject<Building>()
//                } else {
//                    throw FirebaseFirestoreException("building is full",
//                            FirebaseFirestoreException.Code.ABORTED)
//                }
//            }.addOnSuccessListener { transaction ->
//
//            }
//        }
//    }

    override fun createVisit(userId: String, buildingId: String): Single<VisitEntity> {
        return Single.create { emitter ->

        }
    }

//    private fun getUser(userId: String): Single<User> {
//        return Single.create { emitter ->
//            val userRef = db.collection("users")
//        }
//    }


//    override fun checkOut(userId: String): Single<Visit> {
//        TODO("Not yet implemented")
//    }
//
//    override fun isCheckedIn(userId: String): Single<Visit> {
//        TODO("Not yet implemented")
//    }
//
//    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
//        TODO("Not yet implemented")
//    }
}