package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.VisitFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class VisitRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: VisitRepository): VisitRepository {

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        return remoteDataSource.create(userId, buildingId)
    }

    override fun get(userId: String, visitId: String): Single<VisitEntity> {
        return remoteDataSource.get(userId, visitId)
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

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<VisitEntity>> {
        return remoteDataSource.query(userQuery, visitQuery)
    }
}