package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit

interface VisitRepository {
    fun attemptCheckIn(userId: String, buildingName: String): Visit

    fun checkOut(userId: String): Visit

    fun queryVisits(user: User, visit: Visit): List<Visit>
}