package com.csci310_group29.trojancheckincheckout.data

//import android.util.Log
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import com.google.firebase.FirebaseApp
//import org.junit.Test
//
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource
//import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
//import org.junit.runner.RunWith
//
//import org.junit.Assert.*
//
//@RunWith(AndroidJUnit4::class)
//class AuthFirebaseDataSourceTest {
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    private val TAG = "AuthRepoTest"
//
//    @Test
//    fun testUserAuth() {
//        FirebaseApp.initializeApp(context)
//        val remoteDataSource = AuthFirebaseDataSource()
//        testSignUp(remoteDataSource)
//        testSignOut(remoteDataSource)
//        testSignIn(remoteDataSource)
//        deleteUser(remoteDataSource)
//    }
//
//    private fun testSignUp(remoteDataSource: AuthRepository) {
//        val user = User(id = null,
//                isStudent = true,
//                firstName = "Tommy",
//                lastName = "Trojan",
//                major = "Computer Science",
//                studentId = "0123456789")
//        val email = "tommy@gmail.com"
//        val password = "123456"
//        val observable = remoteDataSource.createUser(email = email, password = password, user = user)
//        try {
//            observable.blockingAwait()
//        } catch(e: Exception) {
//            Log.d(TAG, e.message.toString())
//            assertTrue(false)
//        }
//
//    }
//
//    private fun testSignIn(remoteDataSource: AuthRepository) {
//        val email = "tommy@gmail.com"
//        val password = "123456"
//        val observable = remoteDataSource.getUserWithCredentials(email = email, password = password)
//        try {
//            val user = observable.blockingGet()
//            assertTrue(true)
//        } catch(e: Exception) {
//            Log.d(TAG, e.message.toString())
//            assertTrue(false)
//        }
//    }
//
//    private fun testSignOut(remoteDataSource: AuthRepository) {
//        val observable = remoteDataSource.logoutCurrentUser()
////        observable.subscribe(object: Observer<> {
////            override fun onSubscribe(d: Disposable) {
////                TODO("Not yet implemented")
////            }
////
////            override fun onNext(t: ???) {
////                TODO("Not yet implemented")
////            }
////
////            override fun onError(e: Throwable) {
////                TODO("Not yet implemented")
////            }
////
////            override fun onComplete() {
////                TODO("Not yet implemented")
////            }
////
////        })
//        try {
//            observable.blockingAwait()
//            assertTrue(true)
//        } catch(e: Exception) {
//            Log.d(TAG, e.message.toString())
//            assertTrue(false)
//        }
//    }
//
//    private fun testStayLoggedIn(remoteDataSource: AuthRepository, loggedIn: Boolean) {
//        val observable = remoteDataSource.getCurrentUser()
////        observable.subscribe(object: SingleObserver<User> {
////            override fun onSubscribe(d: Disposable) {
////                TODO("Not yet implemented")
////            }
////
////            override fun onSuccess(t: User) {
////                // viewmodel do action with t
////            }
////
////            override fun onError(e: Throwable) {
////                TODO("Not yet implemented")
////            }
////
////        })
//        try {
//            val user = observable.blockingGet()
//            assertTrue(loggedIn)
//        } catch(e: Exception) {
//            Log.d(TAG, e.message.toString())
//            assertFalse(loggedIn)
//        }
//    }
//
//    private fun deleteUser(remoteDataSource: AuthRepository) {
//        val observable = remoteDataSource.deleteCurrentUser()
//        try {
//            observable.blockingAwait()
//            assertTrue(true)
//        } catch(e: Exception) {
//            Log.d(TAG, e.message.toString())
//            assertTrue(false)
//        }
//        Log.d(TAG, "testing finished")
//    }
//}