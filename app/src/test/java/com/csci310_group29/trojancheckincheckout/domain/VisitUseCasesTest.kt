package com.csci310_group29.trojancheckincheckout.domain

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


private val buildingEntity = BuildingEntity("1", "building", "123", 10, 5, "qrRef")
private val userEntity = UserEntity("12", false, "Tommy", "Trojan", "Computer Science", false, "123", "exampleURL")
private val visitEntity = VisitEntity("12", userEntity.id, buildingEntity.id, Date(), null)
private val user = User(
        userEntity.id!!, userEntity.isStudent, "tommy@usc.edu", userEntity.firstName,
        userEntity.lastName, userEntity.major, userEntity.isCheckedIn!!, userEntity.studentId,
        ByteArray(1024))
private val building = Building(buildingEntity.id, buildingEntity.buildingName, buildingEntity.address, buildingEntity.capacity, buildingEntity.numPeople, buildingEntity.qrCodeRef)
private val visit = Visit(user, building, visitEntity.checkIn, visitEntity.checkOut)

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

    private lateinit var visitUseCases: VisitUseCases

    @Before
    fun setUpDependencies() {
        `when`(mockBuildingRepo.incrementNumStudents(buildingEntity.id, 1)).thenReturn(Single.just(buildingEntity))
        `when`(mockUserUseCases.getCurrentlyLoggedInUser()).thenReturn(Single.just(user))
        `when`(mockUserRepo.setCheckedIn(userEntity.id!!, true)).thenReturn(Single.just(userEntity))
        `when`(mockVisitRepo.create(userEntity.id!!, buildingEntity.id)).thenReturn(Single.just(visitEntity))
        visitUseCases = VisitUseCases(mockBuildingRepo, mockVisitRepo, mockUserRepo, mockUserUseCases)
    }

    @Test
    fun attemptCheckInTest() {
        visitUseCases.attemptCheckIn(buildingEntity.id).test()
                .assertSubscribed().assertComplete().assertValue(visit)
    }
}