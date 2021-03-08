package com.example.trojancheckincheckout.data.repo

import com.example.trojancheckincheckout.data.models.User
import com.example.trojancheckincheckout.data.models.Visit

interface VisitRepository {
    fun attemptCheckIn(userId: String, buildingName: String): Visit

    fun checkOut(userId: String): Visit

    fun queryVisits(user: User, visit: Visit): List<Visit>
}