package com.csci310_group29.trojancheckincheckout.data.remote

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepository
import io.reactivex.Single

class VisitFirebaseDataSource: VisitRepository {
    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
        TODO("Not yet implemented")
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