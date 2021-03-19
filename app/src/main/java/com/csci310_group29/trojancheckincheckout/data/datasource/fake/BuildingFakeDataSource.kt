package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single

class BuildingFakeDataSource: BuildingRepository {
    override fun getBuilding(buildingId: String): Single<BuildingEntity> {
        return Single.just(BuildingEntity(buildingId, "building", "mayflow dr", 10, 5, "qrCodeRef"))
    }

    override fun getBuildingInfo(buildingName: String): Single<BuildingEntity> {
        return Single.just(BuildingEntity("1234", buildingName, "mayflow dr", 10, 5, "qrCodeRef"))
    }

    override fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        return Single.just(BuildingEntity(buildingId, "building", "mayflow dr", 10, 5, "qrCodeRef"))
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        return Completable.complete()
    }

    override fun getQRCode(buildingName: String): Single<ByteArray> {
        return Single.just(ByteArray(1024))
    }
}