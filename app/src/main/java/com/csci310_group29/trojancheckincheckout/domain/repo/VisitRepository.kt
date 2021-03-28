package com.csci310_group29.trojancheckincheckout.domain.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface VisitRepository {

    fun create(userId: String, buildingId: String): Single<VisitEntity>

    fun get(userId: String, visitId: String): Single<VisitEntity>

    fun delete(userId: String, visitId: String): Completable

    fun getLatestVisit(userId: String): Single<VisitEntity>

    fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity>

    fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<VisitEntity>>

    fun runCheckInTransaction(userId: String, buildingId: String): Single<VisitEntity>

    fun query(visitQuery: VisitQuery): Single<List<VisitEntity>>
}