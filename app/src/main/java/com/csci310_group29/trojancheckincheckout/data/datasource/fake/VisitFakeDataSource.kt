package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
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

    override fun delete(userId: String, visitId: String): Completable {
        return Completable.complete()
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<VisitEntity>> {
        return Single.just(listOf(visitEntity))
    }

    override fun runCheckInTransaction(userId: String, buildingId: String): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun runCheckOutTransaction(userId: String, visitId: String, buildingId: String, managerId: String?): Single<VisitEntity> {
        return Single.just(visitEntity)
    }

    override fun query(visitQuery: VisitQuery): Single<List<VisitEntity>> {
        return Single.just(listOf(visitEntity))
    }
}