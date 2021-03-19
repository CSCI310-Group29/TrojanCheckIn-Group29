package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.VisitFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class VisitRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: VisitRepository): VisitRepository {

    override fun create(userId: String, buildingId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun get(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun getLatestVisit(userId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }

    override fun checkOutVisit(visitId: String): Single<VisitEntity> {
        TODO("Not yet implemented")
    }
}