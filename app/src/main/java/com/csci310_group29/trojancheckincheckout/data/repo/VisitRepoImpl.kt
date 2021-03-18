package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.data.fake.VisitFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.remote.VisitFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single

class VisitRepoImpl(fake: Boolean = false): VisitRepository {
    private val remoteDataSource = if (!fake) VisitFirebaseDataSource() else VisitFakeDataSource()
    override fun createVisit(userId: String, buildingId: String): Single<VisitEntity> {
        return remoteDataSource.createVisit(userId, buildingId)
    }

    override fun getVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }
//    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
//        return remoteDataSource.attemptCheckIn(userId, buildingName)
//    }

//    override fun checkOut(userId: String): Single<Visit> {
//        return remoteDataSource.checkOut(userId)
//    }

//    override fun isCheckedIn(userId: String): Single<Visit> {
//        return remoteDataSource.isCheckedIn(userId)
//    }

//    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
//        return remoteDataSource.queryVisits(user, visit)
//    }
}