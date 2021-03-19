package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.data.remote.BuildingFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BuildingRepoImpl @Inject constructor(private val remoteDataSource: BuildingFirebaseDataSource):
        BuildingRepository {

    override fun getBuildingInfo(buildingName: String): Single<BuildingEntity> {
        return remoteDataSource.getBuildingInfo(buildingName)
    }

    override fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        return remoteDataSource.incrementNumStudents(buildingId, incrementCount)
    }

    override fun getBuilding(buildingId: String): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        return remoteDataSource.updateBuildingCapacities(buildingCapacities)
    }

    override fun getQRCode(buildingName: String): Single<Bitmap> {
        return remoteDataSource.getQRCode(buildingName)
    }
}