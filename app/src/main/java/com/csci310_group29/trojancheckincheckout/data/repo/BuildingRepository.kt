package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap

interface BuildingRepository {
    fun getBuildingInfo(buildingName: String)

    fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>)

    fun getQRCode(buildingName: String): Bitmap
}