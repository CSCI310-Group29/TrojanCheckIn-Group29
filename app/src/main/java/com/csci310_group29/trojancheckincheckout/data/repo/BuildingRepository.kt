package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.Building
import io.reactivex.Completable
import io.reactivex.Single

interface BuildingRepository {
    fun getBuildingInfo(buildingName: String): Single<Building>

    fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable

    fun getQRCode(buildingName: String): Single<Bitmap>
}