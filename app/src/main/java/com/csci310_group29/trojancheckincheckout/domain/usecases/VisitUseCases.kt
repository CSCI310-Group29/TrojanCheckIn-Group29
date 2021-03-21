package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.BuildingRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class VisitUseCases @Inject constructor(@Named("Repo") private val buildingRepo: BuildingRepository,
                                        @Named("Repo") private val visitRepo: VisitRepository,
                                        @Named("Repo") private val userRepo: UserRepository,
                                        private val userUserCases: UserUseCases) {

    companion object {
        private val TAG = "VisitUseCases"
    }

    fun attemptCheckIn(buildingId: String): Single<Visit> {
        return buildingRepo.incrementNumPeople(buildingId, 1.toDouble())
                .flatMap { building ->
                    userUserCases.getCurrentlyLoggedInUser()
                            .flatMap {user ->
                                userRepo.setCheckedInBuilding(user.id, buildingId)
                                        .flatMap {visitRepo.create(user.id, building.id!!)
                                                    .flatMap { visit ->
                                                        Single.just(buildVisitModel(user, building, visit))
                                                    }
                                        }
                            }
                        }
    }

    fun checkOut(buildingId: String): Single<Visit> {
        return userUserCases.getCurrentlyLoggedInUser()
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
                                                buildVisitModel(user, buildingEntity, newVisitEntity)
                                            })
                                    }
                            }
                    } else {
                        throw Exception("user is not checked in or is checkout of wrong building")
                    }
                }
    }

    fun searchVisits(): Single<List<Visit>> {
        TODO("Not yet implemented")
    }

    private fun buildVisitModel(user: User, buildingEntity: BuildingEntity, visitEntity: VisitEntity): Visit {
        val building = Building(
                id = buildingEntity.id!!,
                buildingName = buildingEntity.buildingName!!,
                address = buildingEntity.address,
                capacity = buildingEntity.capacity!!,
                numPeople = buildingEntity.numPeople!!,
                qrCodeRef = buildingEntity.qrCodeRef!!
        )
        return Visit(
                user = user,
                building = building,
                checkIn = visitEntity.checkIn,
                checkOut = visitEntity.checkOut
        )
    }


}