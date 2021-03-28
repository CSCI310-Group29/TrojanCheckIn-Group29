package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.BuildingFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class BuildingRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: BuildingRepository):
        BuildingRepository {

    override fun get(id: String): Single<BuildingEntity> {
        return remoteDataSource.get(id)
    }

    override fun getAll(): Single<List<BuildingEntity>> {
        return remoteDataSource.getAll()
    }

    override fun create(buildingEntity: BuildingEntity): Single<BuildingEntity> {
        return remoteDataSource.create(buildingEntity)
    }

    override fun delete(buildingId: String): Completable {
        return remoteDataSource.delete(buildingId)
    }

    override fun observe(id: String): Observable<BuildingEntity> {
        return remoteDataSource.observe(id)
    }

    override fun observeByName(buildingName: String): Observable<BuildingEntity> {
        return remoteDataSource.observeByName(buildingName)
    }

    override fun getByName(buildingName: String): Single<BuildingEntity> {
        return remoteDataSource.getByName(buildingName)
    }

    override fun incrementNumPeople(buildingId: String, incrementCount: Double): Single<BuildingEntity> {
        return remoteDataSource.incrementNumPeople(buildingId, incrementCount)
    }

    override fun updateCapacities(buildingCapacities: HashMap<String, Double>): Completable {
        return remoteDataSource.updateCapacities(buildingCapacities)
    }

    override fun updateSingleCapacity(buildingId: String, capacity: Double): Completable {
        return remoteDataSource.updateSingleCapacity(buildingId, capacity)
    }
}