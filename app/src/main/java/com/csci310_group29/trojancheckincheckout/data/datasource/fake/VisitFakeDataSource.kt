package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

private val visitEntity = VisitEntity("12", "12", "12", Date(), null)

class VisitFakeDataSource @Inject constructor(): VisitRepository {

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun get(userId: String, visitId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }
}