package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class VisitUseCases @Inject constructor(
    @Named("Repo") private val buildingRepo: BuildingRepository,
    @Named("Repo") private val visitRepo: VisitRepository,
    @Named("Repo") private val userRepo: UserRepository,
    private val userUseCases: UserUseCases,
    private val buildingUseCases: BuildingUseCases
) {

    companion object {
        private val TAG = "VisitUseCases"
    }

    fun attemptCheckIn(buildingId: String): Single<Visit> {
        return buildingRepo.incrementNumPeople(buildingId, 1.toDouble())
                .flatMap { building ->
                    userUseCases.getCurrentlyLoggedInUser()
                            .flatMap {user ->
                                userRepo.setCheckedInBuilding(user.id, buildingId)
                                        .flatMap {visitRepo.create(user.id, building.id!!)
                                                    .flatMap { visit ->
                                                        Single.just(buildVisitModel(user, building, null, visit))
                                                    }
                                        }
                            }
                        }
    }

    fun checkOut(buildingId: String): Single<Visit> {
        return userUseCases.getCurrentlyLoggedInUser()
                .flatMap { user ->
                    Log.d(TAG, user.toString())

                    if (user.checkedInBuilding != null && user.checkedInBuilding.id == buildingId) {
                        Log.d(TAG, "user is checked in")
                        userRepo.setCheckedInBuilding(user.id, null)
                            .flatMap {  newUser ->
                                visitRepo.getLatestVisit(newUser.id!!)
                                    .flatMap {visitEntity ->
                                        Log.d(TAG, visitEntity.toString())
                                        Single.zip(visitRepo.checkOutVisit(newUser.id!!, visitEntity.id!!),
                                            buildingRepo.incrementNumPeople(visitEntity.buildingId!!,
                                                (-1).toDouble()
                                            ),
                                            { newVisitEntity, buildingEntity ->
                                                buildVisitModel(user, buildingEntity, null, newVisitEntity)
                                            })
                                    }
                            }
                    } else {
                        throw Exception("user is not checked in or is checkout of wrong building")
                    }
                }
    }

    fun searchVisits(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<Visit>> {
        if (visitQuery.buildingName != null) {
            return buildingUseCases.getBuildingInfo(visitQuery.buildingName!!)
                .flatMap { building ->
                    visitQuery.buildingId = building.id
                    visitRepo.query(userQuery, visitQuery)
                        .flatMap { visitEntities ->
                            val visits: MutableList<Visit> = mutableListOf()
                            visitEntities.forEach { visitEntity ->
                                userUseCases.getUser(visitEntity.userId!!)
                                    .doAfterSuccess { user ->  visits.add(buildVisitModel(user, null, building, visitEntity))}
                                    .doOnError { e -> throw e}
                            }
                            Single.just(visits)
                        }
                }
        } else {
            return visitRepo.query(userQuery, visitQuery)
                .flatMap { visitEntities ->

                    val visits: MutableList<Visit> = mutableListOf()
                    visitEntities.forEach { visitEntity ->
                        buildingUseCases.getBuildingInfoById(visitEntity.buildingId!!)
                            .flatMap { building ->
                                userUseCases.getUser(visitEntity.userId!!)
                                    .doAfterSuccess { user ->
                                        visits.add(buildVisitModel(user, null, building, visitEntity))
                                    }
                                    .doOnError { e -> throw e}
                            }

                    }
                    Single.just(visits)
                }
        }
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