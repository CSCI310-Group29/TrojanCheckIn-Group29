package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import javax.inject.Inject

class VisitFakeDataSource @Inject constructor(): VisitRepository {
    override fun createVisit(userId: String, buildingId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
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