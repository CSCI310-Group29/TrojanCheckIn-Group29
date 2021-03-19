package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single

class BuildingFirebaseDataSource: BuildingRepository {
    override fun getBuilding(buildingId: String): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun getBuildingInfo(buildingName: String): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
    }

    fun incrementNumStudents(buildingId: String): Single<Building> {
        TODO("Not yet implemented")
    }

    override fun getQRCode(buildingName: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }
}