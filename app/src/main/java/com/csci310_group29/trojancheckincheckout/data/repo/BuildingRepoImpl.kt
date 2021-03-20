package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.BuildingFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class BuildingRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: BuildingRepository):
        BuildingRepository {

    override fun get(id: String): Single<BuildingEntity> {
        return remoteDataSource.get(id)
    }

    override fun getByName(buildingName: String): Single<BuildingEntity> {
        return remoteDataSource.getByName(buildingName)
    }

    override fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        return remoteDataSource.incrementNumStudents(buildingId, incrementCount)
    }

    override fun updateCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        return remoteDataSource.updateCapacities(buildingCapacities)
    }
}