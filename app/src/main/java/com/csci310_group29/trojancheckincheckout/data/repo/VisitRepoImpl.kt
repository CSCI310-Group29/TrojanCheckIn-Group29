package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class VisitRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: VisitRepository): VisitRepository {

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        return remoteDataSource.create(userId, buildingId)
    }

    override fun get(userId: String, visitId: String): Single<VisitEntity> {
        return remoteDataSource.get(userId, visitId)
    }

    override fun delete(userId: String, visitId: String): Completable {
        return remoteDataSource.delete(userId, visitId)
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        return remoteDataSource.getLatestVisit(userId)
    }

    override fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity> {
        return remoteDataSource.checkOutVisit(userId, visitId)
    }

    override fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<VisitEntity>> {
        return remoteDataSource.getUserVisitHistory(userId, visitQuery)
    }

    override fun runCheckInTransaction(userId: String, buildingId: String): Single<VisitEntity> {
        return remoteDataSource.runCheckInTransaction(userId, buildingId)
    }

    override fun runCheckOutTransaction(userId: String, visitId: String, buildingId: String, managerId: String?): Single<VisitEntity> {
        return remoteDataSource.runCheckOutTransaction(userId, visitId, buildingId, managerId)
    }

    override fun query(visitQuery: VisitQuery): Single<List<VisitEntity>> {
        return remoteDataSource.query(visitQuery)
    }
}