package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.fake.BuildingFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.Building
import com.csci310_group29.trojancheckincheckout.data.remote.BuildingFirebaseDataSource
import io.reactivex.Completable
import io.reactivex.Single

class BuildingRepoImpl(fake: Boolean = false): BuildingRepository {
    private val remoteDataSource = if (!fake) BuildingFirebaseDataSource() else BuildingFakeDataSource()

    override fun getBuildingInfo(buildingName: String): Single<Building> {
        return remoteDataSource.getBuildingInfo(buildingName)
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        return remoteDataSource.updateBuildingCapacities(buildingCapacities)
    }

    override fun getQRCode(buildingName: String): Single<Bitmap> {
        return remoteDataSource.getQRCode(buildingName)
    }
}