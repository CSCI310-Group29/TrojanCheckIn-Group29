package com.csci310_group29.trojancheckincheckout.domain.usecases

import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class VisitUseCasesTest {

    @Mock
    private lateinit var mockUserUseCases: UserUseCases

    @Mock
    private lateinit var mockBuildingRepo: BuildingRepository

    @Mock
    private lateinit var mockUserRepo: UserRepository

    @Mock
    private lateinit var mockVisitRepo: VisitRepository

    @Mock
    private lateinit var mockBuildingUseCases: BuildingUseCases

    private lateinit var visitUseCases: VisitUseCases

    @Before
    fun setUpDependencies() {
        visitUseCases = VisitUseCases(mockBuildingRepo, mockVisitRepo, mockUserRepo, mockUserUseCases, mockBuildingUseCases)
    }

    @Test
    fun attemptCheckInTest() {
        val userEntity = UserEntity("12", false, "Tommy", "Trojan",
            "Computer Science", null, "123", "exampleURL")
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId,null)
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val checkedInUser = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, building, userEntity.studentId,null)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date(), null)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)
        val newVisit = Visit(checkedInUser, building, visitEntity.checkIn, visitEntity.checkOut)
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockUserRepo.setCheckedInBuilding(userEntity.id!!, "1")).thenReturn(Single.just(userEntity))
        `when`(mockVisitRepo.create(userEntity.id!!, buildingEntity.id!!)).thenReturn(Single.just(visitEntity))
        `when`(mockBuildingRepo.incrementNumPeople(buildingEntity.id!!, 1.0)).thenReturn(Single.just(buildingEntity))
        `when`(mockBuildingUseCases.getBuildingInfoById(buildingEntity.id!!)).thenReturn(Single.just(building))
        `when`(mockVisitRepo.runCheckInTransaction(userEntity.id!!, buildingEntity.id!!)).thenReturn(Single.just(visitEntity))
        visitUseCases.attemptCheckIn(buildingEntity.id!!).test()
                .assertSubscribed().assertComplete().assertValue(newVisit)
    }

    @Test
    fun attemptCheckInWhenAlreadyCheckedInTest() {
        val userEntity = UserEntity("12", false, "Tommy", "Trojan",
            "Computer Science", "1", "123", "exampleURL")
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, building, userEntity.studentId,null)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date(), null)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)

        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockUserRepo.setCheckedInBuilding(userEntity.id!!, "1")).thenReturn(Single.just(userEntity))
        `when`(mockVisitRepo.create(userEntity.id!!, buildingEntity.id!!)).thenReturn(Single.just(visitEntity))
        `when`(mockBuildingUseCases.getBuildingInfoById(buildingEntity.id!!)).thenReturn(Single.just(building))
        `when`(mockBuildingRepo.incrementNumPeople(buildingEntity.id!!, 1.0)).thenReturn(Single.just(buildingEntity))
        `when`(mockBuildingRepo.get(buildingEntity.id!!)).thenReturn(Single.just(buildingEntity))
        visitUseCases.attemptCheckIn(buildingEntity.id!!).test()
            .assertSubscribed().assertError(Exception::class.java)
    }

    @Test
    fun checkOutCorrectlyTest() {
        val userEntity = UserEntity("12", false, "Tommy", "Trojan",
            "Computer Science", null, "123", "exampleURL")
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId,null)
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date(), null)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
    }
}