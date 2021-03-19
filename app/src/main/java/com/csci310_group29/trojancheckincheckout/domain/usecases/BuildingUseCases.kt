package com.csci310_group29.trojancheckincheckout.domain.usecases

import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class BuildingUseCases @Inject constructor(@Named("Repo") private val buildingRepo: BuildingRepository,
                                           @Named("Repo") private val pictureRepo: PicturesRepository) {

    fun getBuildingInfo(buildingName: String): Single<Building> {
        return buildingRepo.getByName(buildingName)
                .flatMap {building -> Single.just(buildModel(building))}
    }

    fun updateSingleBuildingCapacity(buildingName: String, newCapacity: Int): Completable {
        return buildingRepo.updateCapacities(hashMapOf(buildingName to newCapacity))
    }

    fun updateMultipleBuildingCapacities(buildings: HashMap<String, Int>): Completable {
        return buildingRepo.updateCapacities(buildings)
    }

    fun getQrCode(buildingName: String): Single<ByteArray> {
        return getBuildingInfo(buildingName)
                .flatMap { building ->
                    pictureRepo.get(building.qrCodeRef)
                }
    }

    private fun buildModel(building: BuildingEntity): Building {
        return Building(building.id, building.buildingName, building.address, building.capacity, building.numPeople, building.qrCodeRef)
    }
}