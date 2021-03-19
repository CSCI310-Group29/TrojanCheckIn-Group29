package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Single
import javax.inject.Inject

class VisitFirebaseDataSource @Inject constructor(): VisitRepository {

    companion object {
        private val TAG = "VisitFirebaseDataSource"
        private val EMULATOR = true
    }

    private val db = Firebase.firestore

    init {
        if (EMULATOR) db.useEmulator("10.0.2.2", 8080)
    }

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun get(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun checkOutVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }
}