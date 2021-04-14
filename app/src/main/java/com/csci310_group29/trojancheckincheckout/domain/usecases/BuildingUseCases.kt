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
        /*
        Gets the info of a building using the building name.

            Params:
                buildingName: String representing the name of the building to be retrieved

            Returns:
                Single that emits a Building object on success, or an error otherwise
         */

        // call BuildingRepository to get the building by name
        return buildingRepo.getByName(buildingName)
                // convert the buildingEntity returned to a Building object
                .flatMap {buildingEntity -> Single.just(buildModel(buildingEntity))}
    }

    open fun getBuildingInfoById(buildingId: String): Single<Building> {
        /*
        Gets the info of a building using the building id.

            Params:
                buildingId: String representing the id of the building

            Returns:
                Single that emits a Building object on success, or an error otherwise
         */

        // call BuildingRepository to get the building by nid
        return buildingRepo.get(buildingId)
            // convert the buildingEntity returned to a Building object
            .flatMap { building -> Single.just(buildModel(building)) }
    }

    open fun getAllBuildings(): Single<List<Building>> {
        /*
        Retrieves all of the buildings from the database.

            Returns:
                Single that emits a List<Building> on success, or an error otherwise
         */

        // call BuildingRepository to retrieve all buildings
        return buildingRepo.getAll()
                // convert buildingEntities to buildings
            .flatMap { buildingEntities ->
                buildModels(buildingEntities)
            }
    }

    open fun observeBuilding(buildingName: String): Observable<Building> {
        /*
        Retrieves a building by its name and notifies of any changes to the building to retrieving
        the new building

            Params:
                buildingName: String representing the name of the building

            Returns:
                Observable that emits a Building every time the building's data changes
         */

        // call BuildingRepository to observe the building by name
        return buildingRepo.observeByName(buildingName)
            // converts the next emitted buildingEntity to a building
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    open fun observeBuildingById(buildingId: String): Observable<Building> {
        /*
        Retrieves a building by its id and notifies of any changes to the building by retrieving
        the new building.

            Params:
                buildingName: String representing the name of the building

            Returns:
                Observable that emits a Building every time the building's data changes
         */

        // call BuildingRepository to observe the building by id
        return buildingRepo.observe(buildingId)
            // converts the next emitted buildingEntity to a building
            .flatMap { buildingEntity ->
                Observable.just(buildModel(buildingEntity))
            }
    }

    open fun updateSingleBuildingCapacity(buildingName: String, newCapacity: Double): Completable {
        /*
        Updates the capacity of a single building. If the number of students in the building
        is less than the new capacity, this function will fail.

            Params:
                buildingName: String representing the name of the building
                newCapacity: Double representing the new capacity of the building

            Returns:
                Completable that emits completion if the capacity was successfully updated, or an
                error if the capacity was not able to be updated.
         */

        // retrieves the building info to get the building id
        return getBuildingInfo(buildingName)
                // calls BuildingRepository to update the capacity of a single building using the id
            .flatMapCompletable { building -> buildingRepo.updateSingleCapacity(building.id, newCapacity) }
    }

    open fun updateMultipleBuildingCapacities(buildings: HashMap<String, Double>): Completable {
        /*
        Updates the capacity of multiple buildings. If the number of students in any of the
        requested buildings is less than the building's new respective capacity, then that building's
        capacity will not have been updated.

            Params:
                buildings: HashMap<String, Double> of buildings corresponding to the building name
                    and its respective capacity

            Returns:
                Completable that emits completion if the capacities were successfully updated,
                or an error if at least one of the capacities were not updated.
         */
        val buildingNames = buildings.keys
        return Observable.fromIterable(buildingNames)
            .flatMap { buildingName ->
                buildingRepo.getByName(buildingName).toObservable()
            }
            .collectInto(hashMapOf<String, Double>(), { map, buildingEntity ->
                map[buildingEntity.id!!] = buildings[buildingEntity.buildingName]!!
            })
            .flatMapCompletable { buildingIdCapacities ->  buildingRepo.updateCapacities(buildingIdCapacities) }
    }

    open fun getQrCode(buildingName: String): Single<ByteArray> {
        /*
            Retrieves the QR code a building.

            Params:
                buildingName: String representing the name of the building

            Returns:
                Single that emits a ByteArray on success, or an error otherwise. The ByteArray
                represents the QR code.
         */

        // gets building info
        return getBuildingInfo(buildingName)
                // calls PicturesRepository to get the QR ccode byte array of the building
                .flatMap { building ->
                    pictureRepo.get(building.qrCodeRef)
                }
    }

    private fun buildModels(buildings: List<BuildingEntity>): Single<List<Building>> {
        /*
        Converts a list of building entities to a list of buildings

            Params:
                buildings: list of building entities that need to be converted

            Returns:
                Single that emits a list of Buildings
         */
        return Single.just(buildings.map { buildingEntity ->
            buildModel(buildingEntity)
        })
    }

    private fun buildModel(building: BuildingEntity): Building {
        /*
        Converts a buildingEntity to a building

            Params:
                building: BuildingEntity to be converted

            Returns:
                Building
         */
        return Building(building.id!!, building.buildingName!!, building.address, building.capacity!!, building.numPeople!!, building.qrCodeRef!!)
    }
}