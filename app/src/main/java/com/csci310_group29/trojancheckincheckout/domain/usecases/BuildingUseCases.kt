package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.os.Build
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class BuildingUseCases @Inject constructor(@Named("Repo") private val buildingRepo: BuildingRepository,
                                           @Named("Repo") private val pictureRepo: PicturesRepository) {

    fun getBuildingInfo(buildingName: String): Single<Building> {
        return buildingRepo.getByName(buildingName)
                .flatMap {building -> Single.just(buildModel(building))}
    }

    fun getBuildingInfoById(buildingId: String): Single<Building> {
        return buildingRepo.get(buildingId)
            .flatMap { building -> Single.just(buildModel(building)) }
    }

    fun getAllBuildings(): Single<List<Building>> {
        return buildingRepo.getAll()
            .flatMap { buildings ->
                buildModels(buildings)
            }
    }

    fun observeBuilding(buildingName: String): Observable<Building> {
        return buildingRepo.observeByName(buildingName)
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    fun observeBuildingById(buildingId: String): Observable<Building> {
        return buildingRepo.observe(buildingId)
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    fun updateSingleBuildingCapacity(buildingName: String, newCapacity: Double): Completable {
        return getBuildingInfo(buildingName)
            .flatMapCompletable { building -> buildingRepo.updateSingleCapacity(building.id, newCapacity) }
    }

    fun updateMultipleBuildingCapacities(buildings: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
        return buildingRepo.updateCapacities(buildings)
    }

    fun getQrCode(buildingName: String): Single<ByteArray> {
        return getBuildingInfo(buildingName)
                .flatMap { building ->
                    pictureRepo.get(building.qrCodeRef)
                }
    }

    private fun buildModels(buildings: List<BuildingEntity>): Single<List<Building>> {
        return Single.just(buildings.map { buildingEntity ->
            buildModel(buildingEntity)
        })
    }

    private fun buildModel(building: BuildingEntity): Building {
        return Building(building.id!!, building.buildingName!!, building.address, building.capacity!!, building.numPeople!!, building.qrCodeRef!!)
    }
}