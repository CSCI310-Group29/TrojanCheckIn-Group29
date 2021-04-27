package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

private val buildingEntity = BuildingEntity("12", "building", "12", 10, 5, "qrRef")

class BuildingFakeDataSource @Inject constructor(): BuildingRepository {

    override fun get(id: String): Single<BuildingEntity> {
        return Single.just(buildingEntity)
    }

    override fun getAll(): Single<List<BuildingEntity>> {
        return Single.just(listOf(buildingEntity))
    }

    override fun create(buildingEntity: BuildingEntity): Single<BuildingEntity> {
        return Single.just(buildingEntity)
    }

    override fun delete(buildingId: String): Completable {
        return Completable.complete()
    }

    override fun observe(id: String): Observable<BuildingEntity> {
        return Observable.just(buildingEntity)
    }

    override fun observeByName(buildingName: String): Observable<BuildingEntity> {
        return Observable.just(buildingEntity)
    }

    override fun getByName(buildingName: String): Single<BuildingEntity> {
        return Single.just(buildingEntity)
    }

    override fun incrementNumPeople(buildingId: String, incrementCount: Double): Single<BuildingEntity> {
        return Single.just(buildingEntity)
    }

    override fun updateCapacities(buildingCapacities: HashMap<String, Double>): Completable {
        return Completable.complete()
    }

    override fun updateSingleCapacity(buildingId: String, capacity: Double): Completable {
        return Completable.complete()
    }

    override fun buildingExists(buildingName: String): Boolean {
        return true
    }

}