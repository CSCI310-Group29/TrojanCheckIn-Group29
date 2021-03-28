package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import org.junit.Test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.After
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.lang.AssertionError

@RunWith(AndroidJUnit4::class)
class AuthFirebaseDataSourceTest {

    companion object {
        private val TAG = "AuthRepoTest"
        private val HOST = "10.0.2.2"
        private val AUTH_PORT = 9099

    }

    private lateinit var authDataSource: AuthFirebaseDataSource
    private lateinit var app: FirebaseApp
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        app = FirebaseApp.initializeApp(context)!!
        val auth = Firebase.auth
        auth.useEmulator(HOST, AUTH_PORT)
        authDataSource = AuthFirebaseDataSource(auth)
    }

    @After
    fun teardown() {
        app.delete()
    }

    @Test
    fun signupSigninDeleteWithValidCredentials() {
        // clear any previous user session
        try {
            delete()
        } catch(e: AssertionError) {

        }
        val email = "test@usc.edu"
        val password = "password"
        signup(email, password)
        signin(email, password)
        getLoggedInUser(email)
        delete()
        getLoggedInUser(email, true)
    }

    @Test
    fun signupSigninDeleteWithInvalidCredentials() {
        // clear any previous user session
        try {
            delete()
        } catch(e: AssertionError) {

        }
        val email = "test@usc.edu"
        val password = "password"
        signup(email, password)
        signout()
        try {
            signin(email, "Invalid password", true)
        } catch(e: AssertionError) {
            delete()
            throw e
        }
        getLoggedInUser(email, true)
        //sign in with valid and delete to restore database
        signin(email, password)
        getLoggedInUser(email)
        delete()
        getLoggedInUser(email, true)
    }

    private fun signup(email: String, password: String) {
        val single = authDataSource.createUser(email, password)
        try {
            val authEntity = single.blockingGet()
            assertEquals(authEntity.email, email)
        } catch(e: Exception) {
            fail("Tried to sign up. Got an exception")
        }
    }

    private fun signin(email: String, password: String, expectError: Boolean = false) {
        val completable = authDataSource.loginUser(email, password)
        try {
            completable.blockingAwait()
            if (expectError) fail("Expected an exception after trying to sign in")
        } catch(e: Exception) {
            if (!expectError) fail("Expected a successfull sign in. Got an exception instead: ${e.localizedMessage}")
        }
    }

    private fun signout() {
        val completable = authDataSource.logoutCurrentUser()
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("Tried to signout. Got an exception: ${e.localizedMessage}")
        }
    }

    private fun delete() {
        val completable = authDataSource.deleteCurrentUser()
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("Tried to delete user. Got an exception: ${e.localizedMessage}")
        }
    }

    private fun getLoggedInUser(email: String, expectError: Boolean = false) {
        val single = authDataSource.getCurrentUser()
        try {
            val authEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(authEntity.email, email)
            } else {
                fail("Expected an exception when getting logged in user")
            }
        } catch(e: Exception) {
            if (!expectError) {
                fail("Attempted to get logged in user. Got an exception instead: ${e.localizedMessage}")
            }
        }
    }



}