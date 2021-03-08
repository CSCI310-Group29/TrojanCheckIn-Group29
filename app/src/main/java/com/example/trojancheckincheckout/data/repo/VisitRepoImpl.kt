package com.example.trojancheckincheckout.data.repo

import com.example.trojancheckincheckout.data.models.User
import com.example.trojancheckincheckout.data.models.Visit

class VisitRepoImpl: VisitRepository {
    override fun attemptCheckIn(userId: String, buildingName: String): Visit {
        TODO("Not yet implemented")
    }

    override fun checkOut(userId: String): Visit {
        TODO("Not yet implemented")
    }

    override fun queryVisits(user: User, visit: Visit): List<Visit> {
        TODO("Not yet implemented")
    }
}