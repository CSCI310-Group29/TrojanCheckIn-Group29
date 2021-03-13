package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.fake.VisitFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import com.csci310_group29.trojancheckincheckout.data.remote.VisitFirebaseDataSource
import io.reactivex.Single

class VisitRepoImpl(fake: Boolean = false): VisitRepository {
    private val remoteDataSource = if (!fake) VisitFirebaseDataSource() else VisitFakeDataSource()

    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
        return remoteDataSource.attemptCheckIn(userId, buildingName)
    }

    override fun checkOut(userId: String): Single<Visit> {
        return remoteDataSource.checkOut(userId)
    }

    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
        return remoteDataSource.queryVisits(user, visit)
    }
}