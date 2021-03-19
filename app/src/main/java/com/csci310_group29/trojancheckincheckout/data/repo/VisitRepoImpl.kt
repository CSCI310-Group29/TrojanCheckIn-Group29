package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.data.remote.VisitFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import javax.inject.Inject

class VisitRepoImpl @Inject constructor(private val remoteDataSource: VisitFirebaseDataSource): VisitRepository {
    override fun createVisit(userId: String, buildingId: String): Single<VisitEntity> {
        return remoteDataSource.createVisit(userId, buildingId)
    }

    override fun getVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun checkOutVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }
}