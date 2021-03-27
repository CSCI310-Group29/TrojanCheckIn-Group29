package com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases

import android.graphics.BitmapFactory
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner
import java.lang.Exception


private val userEntity = UserEntity("12", false, "Tommy", "Trojan", "Computer Science", null, "123", null)
private val user = User("12", false, "test@usc.edu", "Tommy", "Trojan", "Computer Science", null, "123", null)
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
        userUseCases = UserUseCases(mockAuthRepo, mockUserRepo, mockPictureRepo, mockBuildingUseCases)
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


}