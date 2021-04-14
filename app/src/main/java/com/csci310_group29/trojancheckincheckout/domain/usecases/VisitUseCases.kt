package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

open class VisitUseCases @Inject constructor(
    @Named("Repo") private val buildingRepo: BuildingRepository,
    @Named("Repo") private val visitRepo: VisitRepository,
    @Named("Repo") private val userRepo: UserRepository,
    private val userUseCases: UserUseCases,
    private val buildingUseCases: BuildingUseCases
) {

    companion object {
        private val TAG = "VisitUseCases"
    }

    open fun attemptCheckIn(buildingId: String): Single<Visit> {
        /*
        Attempts to check in the currently logged in user.

            Params:
                buildingId: id of the building to be checked in at
            Returns:
                Single that emits a Visit if the user was checked in, or an error otherwise
         */
        return userUseCases.getCurrentlyLoggedInUser()
            .flatMap { user ->
                if (user.checkedInBuilding != null) throw Exception("user is already checked in")
                else {
                    visitRepo.runCheckInTransaction(user.id, buildingId)
                        .flatMap { visitEntity ->
                            buildingUseCases.getBuildingInfoById(buildingId)
                                .flatMap { building ->
                                    user.checkedInBuilding = building
                                    Single.just(buildVisitModel(user, null, building, visitEntity))
                                }
                        }
                }
            }
    }

    open fun checkOut(buildingId: String): Single<Visit> {
        /*
        Checks out the currently logged in user out of the building specified by the building id. If the building id
        does not correspond the checked in building id of the user, there will be an error. If the
        user is not already checked in, there will be an error.

            Params:
                buildingId: String corresponding to the building id the user is checking out of

            Returns:
                Single that emits a Visit if the user has successfully checked out, or an error otherwise.
         */
        return userUseCases.getCurrentlyLoggedInUser()
            .flatMap { user ->
                if (user.checkedInBuilding != null && user.checkedInBuilding?.id == buildingId) {
                    visitRepo.getLatestVisit(user.id)
                        .flatMap { visitEntity ->
                            visitRepo.runCheckOutTransaction(user.id, visitEntity.id!!, user.checkedInBuilding?.id!!)
                                .flatMap { visitEntityCheckOut ->
                                    buildingUseCases.getBuildingInfoById(user.checkedInBuilding!!.id)
                                        .flatMap {building ->
                                            user.checkedInBuilding = null
                                            Single.just(buildVisitModel(user, null, building, visitEntityCheckOut))
                                        }
                                }
                        }
                } else {
                    throw Exception("User is not checked in or is checking out of the wrong building")
                }
            }
    }

    private fun getVisit(visitEntity: VisitEntity, picture: Boolean = true): Single<Visit> {
        /*
        Gets a visit that corresponds to the VisitEntity

            Params:
                visitEntity: VisitEntity object that will be converted to a Visit
                picture: Boolean specifying whether to get the profile picture of the user
                    corresponding to the visit

            Returns:
                Single that emits a Visit upon success, or an error otherwise
         */
        return Single.zip(buildingUseCases.getBuildingInfoById(visitEntity.buildingId!!),
        userUseCases.getUser(visitEntity.userId!!, null, picture), { building, user ->
                buildVisitModel(user, null, building, visitEntity)
            })
    }

    open fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<Visit>> {
        /*
        Gets and filters the visit history of a specific user based on the userId

            Params:
                userId: id of the user whose visit history will be queried
                visitQuery: specifies the fields to be queried in the user's visit history.
                    Any fields that are not null will be queried for.

            Returns:
                Single that emits a list of visits on success, or an error otherwise
         */
        Log.d(TAG, "domain get user visit history called")
        if (visitQuery.buildingName != null) {
            return buildingUseCases.getBuildingInfo(visitQuery.buildingName!!)
                .flatMap { building ->
                    visitQuery.buildingId = building.id
                    visitRepo.getUserVisitHistory(userId, visitQuery)
                        .flatMap { visitEntities ->
                            Log.d(TAG, "$visitEntities")
                            Observable.fromIterable(visitEntities)
                                .concatMap { visitEntity ->
                                    userUseCases.getUser(visitEntity.userId!!).toObservable()
                                        .concatMap { user ->
                                            Observable.just(buildVisitModel(user, null, building, visitEntity))
                                        }
                                }.toList()
                        }
                }
        } else {
            return visitRepo.getUserVisitHistory(userId, visitQuery)
                .flatMap { visitEntities ->
                    Log.d(TAG, "$visitEntities")
                    Observable.fromIterable(visitEntities)
                        .concatMap { visitEntity ->
                            Observable.zip(buildingUseCases.getBuildingInfoById(visitEntity.buildingId!!).toObservable(),
                                userUseCases.getUser(visitEntity.userId!!).toObservable(), { building, user ->
                                    Log.d(TAG, "adding visit")
                                    buildVisitModel(user, null, building, visitEntity)
                                })
                        }.toList()
                }
        }
    }

    private fun checkUser(userEntity: UserEntity, userQuery: UserQuery): Boolean {
        /*
        Checks whether the non-null fields in the userQuery match the fields in the userEntity.

            Params:
                userEntity: UserEntity object to check
                userQuery: UserQuery object whose non-null fields will be used to check
                    whether it matches the userEntity

            Returns:
                Boolean that returns true whether the userQuery matches the userEntity, or
                false otherwise
         */
        Log.d(TAG, userQuery.toString())
        if (userQuery.firstName != null && userQuery.firstName != userEntity.firstName)
            return false
        if (userQuery.lastName != null && userQuery.lastName != userEntity.lastName)
            return false
        if (userQuery.isCheckedIn != null) {
            if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn!!)
                return false
            if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn!!)
                return false
        }
        Log.d(TAG, userQuery.studentId.toString())
        if (userQuery.major != null && userQuery.major == userEntity.major)
            return false
        if (userQuery.isStudent != null && userQuery.isStudent != userEntity.isStudent)
            return false
        if (userQuery.studentId.toBoolean() && userQuery.studentId != userEntity.studentId)
            return false
        return true
    }

    private fun buildVisitModel(user: User, buildingEntity: BuildingEntity?, building: Building?, visitEntity: VisitEntity): Visit {
        /*
        Builds a visit based on a user, building, and visitEntity

            Params:
                user: User object for the visit
                buildingEntity: nullable BuildingEntity object for the visit. If this is not null
                    then the function will use the buildingEntity to build the visit instead
                    of the Building
                building: nullable Building object for the visit. If this is not null
                    then the function will use the buildingEntity to build the visit instead
                    of the Building
                visitEntity: VisitEntity object for the visit

            Returns:
                Visit object

         */
        if (building != null) {
            return Visit(
                user = user,
                building = building,
                checkIn = visitEntity.checkIn,
                checkOut = visitEntity.checkOut
            )
        } else {
            val newBuilding = Building(
                id = buildingEntity?.id!!,
                buildingName = buildingEntity.buildingName!!,
                address = buildingEntity.address,
                capacity = buildingEntity.capacity!!,
                numPeople = buildingEntity.numPeople!!,
                qrCodeRef = buildingEntity.qrCodeRef!!
            )
            return Visit(
                user = user,
                building = newBuilding,
                checkIn = visitEntity.checkIn,
                checkOut = visitEntity.checkOut
            )
        }
    }


}