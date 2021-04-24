package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import io.reactivex.rxjava3.core.Completable
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @Mock
    private lateinit var mockAuthUseCases: AuthUseCases

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(mockAuthUseCases)
    }

    @Test
    fun registerStudentSuccessfullyTest() {
        val email = "test@usc.edu"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "1234567890", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity)
    }

    @Test
    fun registerStudentWithNonUSCEmailTest() {
        val email = "test@gmail.com"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "1234567890", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    @Test
    fun registerStudentWithNoLastNameTest() {
        val email = "test@usc.edu"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "", "Computer Science",
            null, "1234567890", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    @Test
    fun registerStudentWithNoStudentIdTest() {
        val email = "test@usc.edu"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    @Test
    fun registerStudentWithNoPasswordTest() {
        val email = "test@usc.edu"
        val password = ""
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "1234567890", null,null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    @Test
    fun registerStudentWithInvalidStudentIdLengthTest() {
        val email = "test@usc.edu"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "123456789", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    @Test
    fun registerStudentWithNonDigitStudentIdTest() {
        val email = "test@usc.edu"
        val password = "password"
        val userEntity = UserEntity(
            null, true, "Tommy", "Trojan", "Computer Science",
            null, "123456789A", null, null
        )
        `when`(mockAuthUseCases.signup(email, password, userEntity)).thenReturn(Completable.complete())
        register(email, password, userEntity, true)
    }

    private fun register(email: String, password: String, userEntity: UserEntity, expectError: Boolean = false) {
        val completable = registerViewModel.register(email, password, userEntity)
        try {
            completable.blockingAwait()
            if (expectError) {
                fail("expected an exception when trying to register")
            }
        } catch(e: Exception) {
            if (!expectError) {
                fail("got an exception when trying to register: ${e.localizedMessage}")
            }
        }
    }
}