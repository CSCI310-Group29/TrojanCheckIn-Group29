package com.csci310_group29.trojancheckincheckout.domain.repo

import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import io.reactivex.Single

interface VisitRepository {
    /*
    Given a userId and a building name, attempts to check in the user.
    Params
        userId: String representing the unique identification of the user in the database
        buildingName: String identifying the name of the building
    Returns
        Single that emits a Visit object if the user check in was successful, and an error
        if the check in was unsuccessful
     */
//    fun attemptCheckIn(userId: String, buildingName: String): Single<Visit>

    /*
    Given a userId of a user, attempts to check out the user from the current checked in building
    Params
        userId: String representing the unique identification of the user in the database
    Returns
        Single that emits a Visit object if the user check out was successful, and an error
        if the check out was unsuccessful
     */
//    fun checkOut(userId: String): Single<Visit>

    fun create(userId: String, buildingId: String): Single<VisitEntity>

    fun get(visitId: String): Single<VisitEntity>

    fun getLatestVisit(userId: String): Single<VisitEntity>

    fun checkOutVisit(visitId: String): Single<VisitEntity>

    /*
    Given a userId of a user, checks if the user is currently checked into a building
    Params
        userId: String representing the unique identification of the user in the database
    Returns
        Single that emits a Visit object if the user is currently checked in, and an error
        if the user is not checked in
     */
//    fun isCheckedIn(userId: String): Single<Visit>


    /*
    Given a User object specifying the fields for querying users, and a Visit object specifying
    details for querying visits, returns a list of Visit objects matching the query

    Params
        user: User object specifying user fields to query. Fields that are
                null will not be queried for.
        visit: Visit object specifying visit fields to query.
                Fields that are null will not be queried for
    Returns
        Single that emits a list of visit objects matching the specified query
     */
//    fun queryVisits(user: User, visit: Visit): Single<List<Visit>>
}