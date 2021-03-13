package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import io.reactivex.Observable
import io.reactivex.Single

interface VisitRepository {
    fun attemptCheckIn(userId: String, buildingName: String): Single<Visit>

    fun checkOut(userId: String): Single<Visit>

    fun queryVisits(user: User, visit: Visit): Single<List<Visit>>
}