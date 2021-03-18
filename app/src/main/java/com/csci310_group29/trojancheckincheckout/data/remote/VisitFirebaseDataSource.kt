package com.csci310_group29.trojancheckincheckout.data.remote

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Single

class VisitFirebaseDataSource: VisitRepository {
    private val db = Firebase.firestore
    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
        return Single.create { emitter ->

        }
    }


    override fun checkOut(userId: String): Single<Visit> {
        TODO("Not yet implemented")
    }

    override fun isCheckedIn(userId: String): Single<Visit> {
        TODO("Not yet implemented")
    }

    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
        TODO("Not yet implemented")
    }
}