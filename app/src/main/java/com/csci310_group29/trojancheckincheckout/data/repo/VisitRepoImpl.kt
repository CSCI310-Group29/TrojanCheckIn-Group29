package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import io.reactivex.Single

class VisitRepoImpl: VisitRepository {
    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
        TODO("Not yet implemented")
    }

    override fun checkOut(userId: String): Single<Visit> {
        TODO("Not yet implemented")
    }

    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
        TODO("Not yet implemented")
    }
}