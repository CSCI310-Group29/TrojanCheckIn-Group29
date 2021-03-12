package com.csci310_group29.trojancheckincheckout.data

import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.google.firebase.FirebaseApp
import org.junit.Test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class AuthRepoTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val TAG = "AuthRepoTest"

    fun testSignUp() {
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
        val observable = repo.createUser(email = email, password = password, user = user)
        try {
            observable.blockingAwait()
        } catch(e: Exception) {
            Log.d(TAG, e.message.toString())
            assertTrue(false)
        }
        Log.d(TAG, "testing finished")
    }

    @Test
    fun testSignIn() {
        FirebaseApp.initializeApp(context)
        val repo = AuthRepoImpl()
        val email = "tommy@gmail.com"
        val password = "123456"
        val observable = repo.getUserWithCredentials(email = email, password = password)
        observable.blockingSubscribe(object: Observer<User?> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: User) {
                val user = t
                assertTrue(true)
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, e.message.toString())
                assertTrue(false)
            }

            override fun onComplete() {
                assertTrue(true)
            }

        })
    }

    @Test
    fun testStayLoggedIn() {
        FirebaseApp.initializeApp(context)
        val repo = AuthRepoImpl()
        val observable = repo.getCurrentUser()
        observable.blockingSubscribe(object: Observer<User?> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: User) {
                val user = t
                assertTrue(true)
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, e.message.toString())
                assertTrue(false)
            }

            override fun onComplete() {
                assertTrue(true)
            }

        })
    }
}