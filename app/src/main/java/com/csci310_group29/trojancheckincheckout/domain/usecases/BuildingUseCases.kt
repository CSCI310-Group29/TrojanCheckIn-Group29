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

open class BuildingUseCases @Inject constructor(@Named("Repo") private val buildingRepo: BuildingRepository,
                                           @Named("Repo") private val pictureRepo: PicturesRepository) {

    open fun getBuildingInfo(buildingName: String): Single<Building> {
        return buildingRepo.getByName(buildingName)
                .flatMap {building -> Single.just(buildModel(building))}
    }

    open fun getBuildingInfoById(buildingId: String): Single<Building> {
        return buildingRepo.get(buildingId)
            .flatMap { building -> Single.just(buildModel(building)) }
    }

    open fun getAllBuildings(): Single<List<Building>> {
        return buildingRepo.getAll()
            .flatMap { buildings ->
                buildModels(buildings)
            }
    }

    open fun observeBuilding(buildingName: String): Observable<Building> {
        return buildingRepo.observeByName(buildingName)
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    open fun observeBuildingById(buildingId: String): Observable<Building> {
        return buildingRepo.observe(buildingId)
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    open fun updateSingleBuildingCapacity(buildingName: String, newCapacity: Double): Completable {
        return getBuildingInfo(buildingName)
            .flatMapCompletable { building -> buildingRepo.updateSingleCapacity(building.id, newCapacity) }
    }

    open fun updateMultipleBuildingCapacities(buildings: HashMap<String, Double>): Completable {
        val completables: MutableList<Completable> = mutableListOf()
        buildings.forEach { (buildingName, capacity) -> completables.add(updateSingleBuildingCapacity(buildingName, capacity)) }
        return Completable.merge(completables)
    }

    open fun getQrCode(buildingName: String): Single<ByteArray> {
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