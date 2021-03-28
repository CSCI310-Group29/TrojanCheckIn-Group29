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
        return Single.zip(buildingUseCases.getBuildingInfoById(visitEntity.buildingId!!),
        userUseCases.getUser(visitEntity.userId!!, null, picture), { building, user ->
                buildVisitModel(user, null, building, visitEntity)
            })
    }

    open fun searchVisits(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<Visit>> {
        if (visitQuery.buildingName != null) {
            return buildingUseCases.getBuildingInfo(visitQuery.buildingName!!)
                .flatMap { building ->
                    visitQuery.buildingId = building.id
                    visitRepo.query(visitQuery)
                        .flatMap { visitEntities ->
                            Observable.fromIterable(visitEntities)
                                .flatMap { visitEntity ->
                                    Observable.zip(Observable.just(visitEntity), userRepo.get(visitEntity.userId!!).toObservable(),
                                        {visitEntity2, userEntity -> Pair(visitEntity2, userEntity)})
                                }
                                .filter { pair -> checkUser(pair.second, userQuery)}
                                .flatMap { pair ->
                                    Observable.just(pair.first)
                                }
                                .flatMap {visitEntity ->
                                    Log.d(TAG, "$visitEntity")
                                    getVisit(visitEntity).toObservable()
                                }.toList()

                        }
                }
        } else {
            return visitRepo.query(visitQuery)
                .flatMap { visitEntities ->
                    Observable.fromIterable(visitEntities)
                        .flatMap { visitEntity ->
                            Observable.zip(Observable.just(visitEntity), userRepo.get(visitEntity.userId!!).toObservable(),
                                {visitEntity2, userEntity -> Pair(visitEntity2, userEntity)})
                        }
                        .filter { pair ->
//                            Log.d(TAG, pair.first.toString())
                            checkUser(pair.second, userQuery)
                        }
                        .flatMap { pair ->
                            Log.d(TAG, pair.first.toString())
                            Observable.just(pair.first)
                        }
                        .flatMap {visitEntity ->
                            getVisit(visitEntity).toObservable()
                        }.toList()
                }
            }
        }


    open fun getUserVisitHistory(userId: String, visitQuery: VisitQuery): Single<List<Visit>> {
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
        Log.d(TAG, userQuery.toString())
        if (userQuery.firstName != null && userQuery.firstName != userEntity.firstName)
            return false
        if (userQuery.lastName != null && userQuery.lastName != userEntity.lastName)
            return false
        if (userQuery.isCheckedIn != null) {
            if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn)
                return false
            if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn)
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