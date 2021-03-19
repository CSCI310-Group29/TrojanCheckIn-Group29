package com.csci310_group29.trojancheckincheckout.domain.usecases

import com.csci310_group29.trojancheckincheckout.data.repo.BuildingRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BuildingUseCases @Inject constructor(private val buildingRepo: BuildingRepository,
                                           private val pictureRepo: PicturesRepository) {

    fun getBuildingInfo(buildingName: String): Single<Building> {
        TODO("Not yet implemented")
    }

    fun updateSingleBuildingCapacity(buildingName: String, newCapacity: Int): Completable {
        TODO("Not yet implemented")
    }

    fun updateMultipleBuildingCapacities(buildings: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
    }
}