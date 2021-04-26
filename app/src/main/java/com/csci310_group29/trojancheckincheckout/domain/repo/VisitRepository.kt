package com.csci310_group29.trojancheckincheckout.domain.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface VisitRepository {

    /*
    Creates a visit in the database

        Params:
            userId: String representing the id of the user performing the visit
            buildingVisit: String representing the id of the building the visit is taking place at

        Returns:
            Single that emits a VisitEntity if the visit was successfully created, or an error otherwise
     */
    fun create(userId: String, buildingId: String): Single<VisitEntity>

    /*
    Gets a visit in the database based on the user id and the visit id

        Params:
            userId: String specifying the id of the user
            visitId: String specifying the id of the visit

        Returns:
            Single that emits a VisitEntity if the visit was found, or an error if the visit was
                not found
     */
    fun get(userId: String, visitId: String): Single<VisitEntity>

    /*
    Deletes a visit in the database based on the user id and visit id

        Params:
            userId: String specifying the id of the user
            visitId: String specifying the id of the visit

        Returns:
            Completable that emits completion if the user was deleted, or an error otherwise
     */
    fun delete(userId: String, visitId: String): Completable

    /*
    Gets the latest visit based on the checkIn field of the visit

        Params:
            userId: String representing the user whose latest visit will be retrieved

        Returns:
            VisitEntity representing the latest visit
     */
    fun getLatestVisit(userId: String): Single<VisitEntity>

    /*
    Adds a checkOut date to the visit of a user

        Params:
            userId: String representing the userId of the visit
            visitId: String representing the id of the visit

        Returns:
            Updated VisitEntity with a checkOut date
     */
    fun checkOutVisit(userId: String, visitId: String): Single<VisitEntity>

    /*
    Gets the visit history of a specific user and filters the visits

        Params:
            userId: String representing the user whose visit history will be queried
            visitQuery: VisitQuery object for querying visits. All fields that are not null
                will be queried for.
     */
    fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<VisitEntity>>

    /*
    Checks in a user to a building. To check in a user, a transaction of incrementing the building's
    capacity, setting the checked in building id of the user, and creating a visit is executed. If
    the number of students in the building is equal to the capacity, none of the operations in this
    transaction will be executed, and the function will emit an error.

        Params:
            userId: id of the user to be checked in
            buildingId: id of the building the user is checking in to

        Returns:
            Single that emits a VisitEntity upon successful check in, or an error otherwise
     */
    fun runCheckInTransaction(userId: String, buildingId: String): Single<VisitEntity>

    /*
    Checks out a user from a building. To check out a user, a transaction of decrementing the
    building's capacity, setting the checked in building id of the user to null, and checking out
    the visit is executed. If any of the operations fail, none of the changes done by the operations
    will be committed, and the function will return an error.

        Params:
            userId: id of the user to be checked out
            visitId: id of the visit to set a check out date
            buildingId: id of the building to be checked out from

        Returns:
            Single that emits an updated VisitEntity object upon successful check out, or an error
            otherwise
     */
    fun runCheckOutTransaction(userId: String, visitId: String, buildingId: String, force: Boolean): Single<VisitEntity>

    /*
    Queries all visits based on the non null fields in the VisitQuery.

        Params:
            visitQuery: VisitQuery whose non null fields will be queried for

        Returns:
            Single that emits a list of visits upon a successful query, or an error otherwise

     */
    fun query(visitQuery: VisitQuery): Single<List<VisitEntity>>
}