package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.data.models.Building
import io.reactivex.Completable
import io.reactivex.Single

interface BuildingRepository {
    /*
    Given a building name, returns the info of the building

    Params
        buildingName: String specifying the name of the building

    Returns
        Single that emits a Building object on success. The Building object contains the info of
            the building
     */
    fun getBuildingInfo(buildingName: String): Single<Building>

    /*
    Given building names and their corresponding building capacities, updates the building capacities

    Params:
        buildingCapacities: HashMap of buildingNames and their corresponding capacities

    Returns:
        Completable that emits completion if the building capacities were successfully updated,
        or an error if the capacities could not be updated
     */
    fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable

    /*
    Given a building name, gets the corresponding QR code of the building

    Params:
        buildingName: String that specifies the name of the building

    Returns:
        Bitmap containing the QR code
     */
    fun getQRCode(buildingName: String): Single<Bitmap>
}