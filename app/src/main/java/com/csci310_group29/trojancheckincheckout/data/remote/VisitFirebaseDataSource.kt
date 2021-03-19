package com.csci310_group29.trojancheckincheckout.data.remote

import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Single

class VisitFirebaseDataSource: VisitRepository {
    private val db = Firebase.firestore

    override fun createVisit(userId: String, buildingId: String): Single<VisitEntity> {
        return Single.create { emitter ->

        }
    }

    override fun getVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun checkOutVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }
}