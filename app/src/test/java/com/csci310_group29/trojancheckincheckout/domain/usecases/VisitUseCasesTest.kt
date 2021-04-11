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
            "Computer Science", null, "123", false, "exampleURL")
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted,null)
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val checkedInUser = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, building, userEntity.studentId, userEntity.deleted,null)
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
            "Computer Science", "1", "123", false, "exampleURL")
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, building, userEntity.studentId, userEntity.deleted,null)
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
            "Computer Science", "1", "123", false,"exampleURL")
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, building, userEntity.studentId, userEntity.deleted,null)
        val checkOutUser = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted,null)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date())
        val checkOutDate = Date()
        val checkOutVisitEntity = VisitEntity(visitEntity.id, userEntity.id, buildingEntity.id, visitEntity.checkIn, checkOutDate)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)
        val buildingCheckedOut = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address!!,
        buildingEntity.capacity!!, buildingEntity.numPeople?.minus(1)!!, buildingEntity.qrCodeRef!!)
        val checkOutVisit = Visit(checkOutUser, buildingCheckedOut, visitEntity.checkIn, checkOutVisitEntity.checkOut)
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockVisitRepo.getLatestVisit(user.id)).thenReturn(Single.just(visitEntity))
        `when`(mockVisitRepo.runCheckOutTransaction(user.id, visitEntity.id!!, buildingEntity.id!!))
            .thenReturn(Single.just(checkOutVisitEntity))
        `when`(mockBuildingUseCases.getBuildingInfoById(buildingEntity.id!!)).thenReturn(Single.just(buildingCheckedOut))
        visitUseCases.checkOut(buildingEntity.id!!).test().assertComplete().assertValue(checkOutVisit)
    }

    @Test
    fun checkOutWhenNotCheckedInTest() {
        val userEntity = UserEntity("12", false, "Tommy", "Trojan",
            "Computer Science", null, "123", false,"exampleURL")
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted,null)
        val checkOutUser = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted,null)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date())
        val checkOutDate = Date()
        val checkOutVisitEntity = VisitEntity(visitEntity.id, userEntity.id, buildingEntity.id, visitEntity.checkIn, checkOutDate)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)
        val buildingCheckedOut = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address!!,
            buildingEntity.capacity!!, buildingEntity.numPeople?.minus(1)!!, buildingEntity.qrCodeRef!!)
        val checkOutVisit = Visit(checkOutUser, buildingCheckedOut, visitEntity.checkIn, checkOutVisitEntity.checkOut)
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockVisitRepo.getLatestVisit(user.id)).thenReturn(Single.just(visitEntity))
        `when`(mockVisitRepo.runCheckOutTransaction(user.id, visitEntity.id!!, buildingEntity.id!!))
            .thenReturn(Single.just(checkOutVisitEntity))
        `when`(mockBuildingUseCases.getBuildingInfoById(buildingEntity.id!!)).thenReturn(Single.just(buildingCheckedOut))
        visitUseCases.checkOut(buildingEntity.id!!).test().assertError(Exception::class.java)
    }

    @Test
    fun checkOutOfWrongBuildingTest() {
        val userEntity = UserEntity("12", false, "Tommy", "Trojan",
            "Computer Science", null, "123", false,"exampleURL")
        val buildingEntity = BuildingEntity("1", "building", "123",
            10, 5, "qrRef")
        val building = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address,
            buildingEntity.capacity!!, buildingEntity.numPeople!!, buildingEntity.qrCodeRef!!)
        val user = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted,null)
        val checkOutUser = User(
            userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
            userEntity.lastName, userEntity.major, null, userEntity.studentId, userEntity.deleted, null)
        val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date())
        val checkOutDate = Date()
        val checkOutVisitEntity = VisitEntity(visitEntity.id, userEntity.id, buildingEntity.id, visitEntity.checkIn, checkOutDate)
        val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)
        val buildingCheckedOut = Building(buildingEntity.id!!, buildingEntity.buildingName!!, buildingEntity.address!!,
            buildingEntity.capacity!!, buildingEntity.numPeople?.minus(1)!!, buildingEntity.qrCodeRef!!)
        val checkOutVisit = Visit(checkOutUser, buildingCheckedOut, visitEntity.checkIn, checkOutVisitEntity.checkOut)
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockVisitRepo.getLatestVisit(user.id)).thenReturn(Single.just(visitEntity))
        `when`(mockVisitRepo.runCheckOutTransaction(user.id, visitEntity.id!!, buildingEntity.id!!))
            .thenReturn(Single.just(checkOutVisitEntity))
        `when`(mockBuildingUseCases.getBuildingInfoById(buildingEntity.id!!)).thenReturn(Single.just(buildingCheckedOut))
        visitUseCases.checkOut("wrong building id").test().assertError(Exception::class.java)
    }
}