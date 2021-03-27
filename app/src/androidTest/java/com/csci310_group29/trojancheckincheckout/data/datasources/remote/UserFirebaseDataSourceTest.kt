package com.csci310_group29.trojancheckincheckout.data.datasources.remote

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception


private val userEntity = UserEntity("12", true, "Tommy", "Trojan", "Computer Science", null, "123", "exampleURL")
@RunWith(AndroidJUnit4::class)
class UserFirebaseDataSourceTest {

    companion object {
        private val TAG = "UserFirebaseDataSourceTest"
        private val DB_PORT = 8080
        private val HOST = "10.0.2.2"
    }

    private lateinit var dataSource: UserFirebaseDataSource

    @Before
    fun setup() {
        val db = Firebase.firestore
        db.useEmulator(HOST, DB_PORT)
        dataSource = UserFirebaseDataSource(db)
    }

    @Test
    fun createGetDeleteTest() {
        val id = createUser(userEntity)
        getUser(id)
        deleteUser(id)
        getUser(id, true)
    }

    private fun createUser(userN: UserEntity): String {
        val single = dataSource.create(userN)
        try {
            val user = single.blockingGet()
            assertEquals(user.firstName, userN.firstName)
            return user.id!!
        } catch(e: Exception) {
            assertTrue(false)
            return "12"
        }
    }

    private fun getUser(id: String, expectError: Boolean = false) {
        val single = dataSource.get(id)
        try {
            val user = single.blockingGet()
            Log.d(TAG, user.toString())
            if (!expectError) assertEquals(user.firstName, userEntity.firstName)
            else fail("Expected an exception")
        } catch(e: Exception) {
            if (!expectError) fail("Expected a user. Got an exception instead")
        }
    }

    private fun deleteUser(id: String) {
        val completable = dataSource.delete(id)
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("Could not delete user: ${e.localizedMessage}")
        }
    }
}