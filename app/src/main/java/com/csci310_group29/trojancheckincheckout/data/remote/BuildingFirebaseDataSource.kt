package com.csci310_group29.trojancheckincheckout.data.remote

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.Building
import com.csci310_group29.trojancheckincheckout.data.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single

class BuildingFirebaseDataSource: BuildingRepository {
    override fun getBuildingInfo(buildingName: String): Single<Building> {
        TODO("Not yet implemented")
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
    }

    override fun getQRCode(buildingName: String): Single<Bitmap> {
        TODO("Not yet implemented")
    }
}