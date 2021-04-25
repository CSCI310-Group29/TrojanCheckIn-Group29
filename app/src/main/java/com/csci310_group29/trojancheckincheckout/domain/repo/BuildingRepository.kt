package com.csci310_group29.trojancheckincheckout.domain.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface BuildingRepository {

    /*
    Retrieves a building based on its id

        Params:
            id: String specifying the id of the building to be retrieved

        Returns:
            Single that emits a BuildingEntity if the building was found, or an error if no building
                was found
     */
    fun get(id: String): Single<BuildingEntity>

    /*
    Retrieves all of the buildings

        Returns:
            Single that emits a list of buildings on success, or an error otherwise
     */
    fun getAll(): Single<List<BuildingEntity>>

    /*
    Creates a building in the database

        Params:
            buildingEntity: BuildingEntity specifying the fields of the building. The fields of
            building in the database will be the same exact fields as in the buildingEntity
     */
    fun create(buildingEntity: BuildingEntity): Single<BuildingEntity>

    /*
    Deletes a building from the database based on its buildingId

        Params:
            buildingId: String representing the id of the building to be deleted

        Returns:
            Completable that emits completion if the building was successfully deleted, or an error
                otherwise
     */
    fun delete(buildingId: String): Completable

    /*
    Observes changes in a specific building through its building id

        Params:
            buildingId: String representing the id of the building to be observed

        Returns:
            Observable that emits a BuildingEntity every time its data changes
     */
    fun observe(id: String): Observable<BuildingEntity>

    /*
    Observes changes in a specific building through its building name

        Params:
            buildingName: String representing the name of the building to be observed

        Returns:
            Observable that emits a BuildingEntity every time its data changes
     */
    fun observeByName(buildingName: String): Observable<BuildingEntity>

    /*
    Retrieves a building based on its name

        Params:
            buildingName: String specifying the id of the building to be retrieved

        Returns:
            Single that emits a BuildingEntity if the building was found, or an error if no building
                was found
     */
    fun getByName(buildingName: String): Single<BuildingEntity>


    /*
    Increments by a specific value (positive or negative) the number of people in the building

        Params:
            buildingId: String representing the building whose number of people will be changed
            incrementCount: Double representing by how much the number of people will be changed.
                Can be positive or negative. If negative, the number of people will be decremented
                instead.
        Returns:
            Single that emits the updated building entity upon a successfull update, or an error
            otherwise
     */
    fun incrementNumPeople(buildingId: String, incrementCount: Double): Single<BuildingEntity>

    /*
    Updates the capacities of multiple buildings. If the number of people in any of the buildings
        is lower than the building's updated capacity, none of the building's capacities will be updated
        and the function will emit an error.

        Params:
            buildingCapacities: HashMap of building names and their new capacities

        Returns:
            Completable that emits completion if the building capacities were successfully updated,
            or an error otherwise.
     */
    fun updateCapacities(buildingCapacities: HashMap<String, Double>): Completable

    /*
    Updates the capacity of a single building. If the number of people in the building is lower
    than the new capacity, then the building's capacity will not be updated and the function will
    emit an error.

        Params:
            HashMap containing building names and capacities.

        Returns:
            Completable that emits completion if the capacity was updated, or an error otherwise.
    */
    fun updateSingleCapacity(buildingId: String, capacity: Double): Completable
    
    /*
    Checks if building exists.

        Params:
            Name of the building.

        Return:
            True if building exists, false if it does not.
    */
    fun buildingExists(buildingName: String): Boolean
}
