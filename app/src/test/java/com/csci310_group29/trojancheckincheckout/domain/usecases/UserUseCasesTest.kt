package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.BitmapFactory
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.LoginViewModel
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner
import java.lang.Exception


private val userEntity = UserEntity("12", false, "Tommy", "Trojan", "Computer Science", null, "123", false,null)
private val user = User("12", false, "test@usc.edu", "Tommy", "Trojan", "Computer Science", null, "123", false, null)
private val authEntity = AuthEntity("12", "test@usc.edu")

@RunWith(MockitoJUnitRunner::class)
class UserUseCasesGetLoggedInUserTestTest {

    @Mock
    private lateinit var mockAuthRepo: AuthRepository

    @Mock
    private lateinit var mockPictureRepo: PicturesRepository

    @Mock
    private lateinit var mockUserRepo: UserRepository

    @Mock
    private lateinit var mockBuildingUseCases: BuildingUseCases

    @Mock
    private lateinit var authDomain: AuthUseCases

    @Mock
    private lateinit var buildingDomain: BuildingUseCases

    @Mock
    private lateinit var mockVisitRepo: VisitRepository

    private lateinit var userUseCases: UserUseCases

    @Before
    fun setupRepo() {
        val inputStream = this.javaClass.getResourceAsStream("tennis_ball.jpg")
        if (inputStream != null) {
            val profileByteArray = ByteArray(inputStream.available())
            inputStream.read(profileByteArray)
            `when`(mockPictureRepo.get("exampleURL")).thenReturn(Single.just(profileByteArray))
        } else {
            val profileByteArray = ByteArray(1024)
            `when`(mockPictureRepo.get("exampleURL")).thenReturn(Single.just(profileByteArray))
        }
        `when`(mockUserRepo.update(userEntity)).thenReturn(Completable.complete())
        userUseCases = UserUseCases(mockAuthRepo, mockUserRepo, mockPictureRepo, mockVisitRepo, mockBuildingUseCases)
    }

    @Test
    fun userIsLoggedIn() {
        `when`(mockAuthRepo.getCurrentUser()).thenReturn(Single.just(authEntity))
        `when`(mockUserRepo.get("12")).thenReturn(Single.just(userEntity))
        val observable = userUseCases.getCurrentlyLoggedInUser()
        observable.test().assertSubscribed().assertComplete().assertValue(user).dispose()
    }

    @Test
    fun userIsLoggedOut() {
        `when`(mockAuthRepo.getCurrentUser()).thenReturn(Single.error(Exception()))
        val observable = userUseCases.getCurrentlyLoggedInUser()
        observable.test().assertSubscribed().assertError(Exception::class.java).dispose()
    }

    @Test
    fun logInViewModelTest() {
        val mockLogInViewModel = LoginViewModel(authDomain)
        `when`(
            mockLogInViewModel.login(
                "tommy@usc.edu",
                "1234"
            )
        ).thenReturn(Single.error(Exception()))
        val observable = userUseCases.getCurrentlyLoggedInUser()
        observable.test().assertSubscribed().assertError(Exception::class.java).dispose()
    }

    @Test
    fun managerHomeViewModelTest() {
        val mockManagerViewModel = ManagerHomeViewModel(authDomain, buildingDomain)

    }

    @Test
    fun managerProfileViewModelTest() {
        val mockManagerViewModel = ManagerHomeViewModel(authDomain, buildingDomain)

    }

    @Test
    fun searchUsers() {
        val spyUserUseCases = Mockito.spy(userUseCases)
        val userEntity1 = UserEntity(
            id = "1",
            isStudent = true,
            firstName = "Tommy",
            lastName = "Trojan",
            major = "Computer Science",
            checkedInBuildingId = null,
            studentId = "1234567890",
            deleted = false,
            photoUrl = null
        )
        val user = User(
            id = userEntity1.id!!,
            isStudent = userEntity1.isStudent,
            email = null,
            firstName = userEntity1.firstName!!,
            lastName = userEntity1.lastName!!,
            major = userEntity1.major,
            checkedInBuilding = null,
            studentId = userEntity1.studentId,
            deleted = userEntity1.deleted,
            profilePicture = null
        )
        `when`(mockUserRepo.getAll()).thenReturn(Single.just(listOf(userEntity1)))
        `when`(spyUserUseCases.getUser(null, null, true, userEntity))
            .thenReturn(Single.just(user))
        val userQuery = UserQuery(
            lastName = "Tro"
        )
        val visitQuery = VisitQuery()
        spyUserUseCases.searchUsers(userQuery, visitQuery).test().assertValue(listOf(user))

    }


}