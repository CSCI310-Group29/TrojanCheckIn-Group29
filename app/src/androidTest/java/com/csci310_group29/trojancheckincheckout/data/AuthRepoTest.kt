package com.csci310_group29.trojancheckincheckout.data

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.google.firebase.FirebaseApp
import org.junit.Test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepository
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class AuthRepoTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val lock =  CountDownLatch(1);

    @Test
    fun testSignUp()  {
        FirebaseApp.initializeApp(context)
        val repo = AuthRepoImpl()
        val user = User(id = null,
                        isStudent = true,
                        firstName = "Tommy",
                        lastName = "Trojan",
                        major = "Computer Science",
                        studentId = "0123456789")
        val email = "tommy@gmail.com"
        val password = "123456"
        repo.createUser(email = email, password = password, user = user, object: AuthRepository.Callback {
            override fun onSuccess(user: User?) {
                assertTrue(true)
            }

            override fun onFailure() {
                assertTrue(false)
            }

        })
    }
}