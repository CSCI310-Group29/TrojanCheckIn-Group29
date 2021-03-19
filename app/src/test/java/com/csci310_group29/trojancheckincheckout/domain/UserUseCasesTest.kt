package com.csci310_group29.trojancheckincheckout.domain

import android.graphics.BitmapFactory
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner


private val userEntity = UserEntity("12", false, "Tommy", "Trojan", "Computer Science", false, "123", "exampleURL")
private val user = User("12", false, "test@gmail.com", "Tommy", "Trojan", "Computer Science", false, "123", ByteArray(1024))
private val authEntity = AuthEntity("12",
        "test@gmail.com")

@RunWith(MockitoJUnitRunner::class)
class UserUseCasesTest {

    @Mock
    private lateinit var mockAuthRepo: AuthRepository

    @Mock
    private lateinit var mockPictureRepo: PicturesRepository

    @Mock
    private lateinit var mockUserRepo: UserRepository

    private lateinit var userUseCases: UserUseCases

    @Before
    fun setupRepo() {
        `when`(mockAuthRepo.getCurrentUser()).thenReturn(Single.just(authEntity))
        val inputStream = this.javaClass.getResourceAsStream("tennis_ball.jpg")
        if (inputStream != null) {
            val profileByteArray = ByteArray(inputStream.available())
            inputStream.read(profileByteArray)
            `when`(mockPictureRepo.get("exampleURL")).thenReturn(Single.just(profileByteArray))
        } else {
            val profileByteArray = ByteArray(1024)
            `when`(mockPictureRepo.get("exampleURL")).thenReturn(Single.just(profileByteArray))
        }
        `when`(mockUserRepo.get("12")).thenReturn(Single.just(userEntity))
        `when`(mockUserRepo.update(userEntity)).thenReturn(Completable.complete())
        userUseCases = UserUseCases(mockAuthRepo, mockUserRepo, mockPictureRepo)
    }

    @Test
    fun getLoggedInUserTest() {
        val observable = userUseCases.getCurrentlyLoggedInUser()
        observable.test().assertSubscribed().assertComplete().assertValue(user).dispose()
    }

    @Test
    fun updateProfileTest() {
        userUseCases.updateProfile(userEntity).test().assertSubscribed().assertComplete()
    }

}