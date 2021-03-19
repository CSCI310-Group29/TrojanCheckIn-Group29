package com.csci310_group29.trojancheckincheckout.domain.usecases

import com.csci310_group29.trojancheckincheckout.data.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.data.entities.VisitEntity
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

class VisitUseCases @Inject constructor(private val buildingRepo: BuildingRepository,
                                        private val visitRepo: VisitRepository,
                                        private val userRepo: UserRepository,
                                        private val userUserCases: UserUseCases) {

    fun attemptCheckIn(buildingId: String): Single<Visit> {
        return buildingRepo.incrementNumStudents(buildingId, 1)
                .flatMap { building ->
                    userUserCases.getCurrentlyLoggedInUser()
                            .flatMap {user ->
                                userRepo.setCheckedIn(user.id, true)
                                        .flatMap {visitRepo.createVisit(user.id, building.id)
                                                    .flatMap { visit ->
                                                        Single.just(buildVisitModel(user, building, visit))
                                                    }
                                        }
                            }
                        }
    }

    fun checkOut(): Single<Visit> {
        return userUserCases.getCurrentlyLoggedInUser(picture = false)
                .flatMap { user ->
                    if (user.isCheckedIn) {
                        visitRepo.getLatestVisit(user.id)
                                .flatMap {visitEntity ->
                                    Single.zip(visitRepo.checkOutVisit(visitEntity.id),
                                    buildingRepo.incrementNumStudents(visitEntity.buildingId, -1),
                                            { visitEnity, buildingEntity ->
                                        buildVisitModel(user, buildingEntity, visitEntity)
                                    })
                                }
                    } else {
                        throw Exception("hello")
                    }
                }
    }

    fun searchVisits(): Single<List<Visit>> {
        TODO("Not yet implemented")
    }

    private fun buildVisitModel(user: User, buildingEntity: BuildingEntity, visitEntity: VisitEntity): Visit {
        val building = Building(
                id = buildingEntity.id,
                buildingName = buildingEntity.buildingName,
                address = buildingEntity.address,
                capacity = buildingEntity.capacity,
                numPeople = buildingEntity.numPeople
        )
        return Visit(
                user = user,
                building = building,
                checkIn = visitEntity.checkIn,
                checkOut = visitEntity.checkOut
        )
    }


}