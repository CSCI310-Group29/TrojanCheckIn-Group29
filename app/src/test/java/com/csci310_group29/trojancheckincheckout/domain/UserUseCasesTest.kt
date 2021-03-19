package com.csci310_group29.trojancheckincheckout.domain

import android.graphics.BitmapFactory
import com.csci310_group29.trojancheckincheckout.data.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.data.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner


private val userEntity = UserEntity("12", false, "Tommy", "Trojan", "Computer Science", false, "123")
private val user = User("12", false, "test@gmail.com", "Tommy", "Trojan", "Computer Science", false, "123")
private val authEntity = AuthEntity("12",
        "test@gmail.com", photoURL="exampleURL")

@RunWith(MockitoJUnitRunner::class)
class UserUseCasesTest {

    @Mock
    private lateinit var mockAuthRepo: AuthRepository

    @Mock
    private lateinit var mockPictureRepo: PicturesRepository

    @Mock
    private lateinit var mockUserRepo: UserRepository

    @Before
    fun setupRepo() {
        `when`(mockAuthRepo.getCurrentUser()).thenReturn(Single.just(authEntity))
        val inputStream = this.javaClass.getResourceAsStream("tennis_ball.jpg")
        if (inputStream != null) {
            val profileByteArray = ByteArray(inputStream.available())
            inputStream.read(profileByteArray)
            `when`(mockPictureRepo.getProfilePicture("exampleURL")).thenReturn(Single.just(profileByteArray))
        } else {
            val profileByteArray = ByteArray(1024)
            `when`(mockPictureRepo.getProfilePicture("exampleURL")).thenReturn(Single.just(profileByteArray))
        }
        `when`(mockUserRepo.getUser("12")).thenReturn(Single.just(userEntity))
    }

    @Test
    fun getLoggedInUserTest() {
        val userUseCase = UserUseCases(mockAuthRepo, mockUserRepo, mockPictureRepo)
        val observable = userUseCase.getCurrentlyLoggedInUser()
        observable.test().assertSubscribed().assertComplete()
        observable.subscribe({ mUser ->
            assertEquals(mUser.id, user.id)
            assertEquals(mUser.email, user.email)
        }, { e ->
            assertTrue(false)
        })
    }


}