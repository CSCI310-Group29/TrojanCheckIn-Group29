package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
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
    private lateinit var buildingDataSource: BuildingFirebaseDataSource
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var app: FirebaseApp

    @Before
    fun setup() {
        app = FirebaseApp.initializeApp(context)!!
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        val db = Firebase.firestore
        db.firestoreSettings = settings
        db.useEmulator(HOST, DB_PORT)
        dataSource = UserFirebaseDataSource(db)
        buildingDataSource = BuildingFirebaseDataSource(db)
    }

    @After
    fun teardown() {
        app.delete()
    }

    @Test
    fun createGetDeleteTest() {
        val id = createUser(userEntity)
        getUser(id)
        deleteUser(id)
        getUser(id, true)
    }

    @Test
    fun getOneStudentInBuildingTest() {
        val buildingEntity = BuildingEntity(
            null,
            "A",
            "A",
            20,
            0,
            "ref"
        )
        val buildingId = createBuilding(buildingEntity)
        val user1 =  UserEntity("12", true,
            "Tommy", "Trojan", "Computer Science",
            buildingId, "123", "exampleURL")
        val user1Id = createUser(user1)
        getUsersInBuilding(buildingId, 1)
        deleteUser(user1Id)
        deleteBuilding(buildingId)
    }

    @Test
    fun getTwoStudentInBuildingTest() {
        val buildingEntity = BuildingEntity(
            null,
            "A",
            "A",
            20,
            0,
            "ref"
        )
        val buildingId = createBuilding(buildingEntity)
        val user1 =  UserEntity("12", true,
            "Tommy", "Trojan", "Computer Science",
            buildingId, "123", "exampleURL")
        val user2 = UserEntity("13", true,
            "Tommy", "Trojan", "Computer Science",
            buildingId, "123", "exampleURL")
        val user1Id = createUser(user1)
        val user2Id = createUser(user2)
        getUsersInBuilding(buildingId, 2)
        deleteUser(user1Id)
        deleteUser(user2Id)
        deleteBuilding(buildingId)
    }

    @Test
    fun getOneOutOfTwoStudentInBuildingTest() {
        val buildingEntity = BuildingEntity(
            null,
            "A",
            "A",
            20,
            0,
            "ref"
        )
        val buildingId = createBuilding(buildingEntity)
        val user1 =  UserEntity("12", true,
            "Tommy", "Trojan", "Computer Science",
            buildingId, "123", "exampleURL")
        val user2 = UserEntity("13", true,
            "Tommy", "Trojan", "Computer Science",
            null, "123", "exampleURL")
        val user1Id = createUser(user1)
        val user2Id = createUser(user2)
        getUsersInBuilding(buildingId, 1)
        deleteUser(user1Id)
        deleteUser(user2Id)
        deleteBuilding(buildingId)
    }

    private fun getUsersInBuilding(buildingId: String, testSize: Int) {
        val observable = dataSource.observeUsersInBuilding(buildingId)
        try {
            val users = observable.blockingFirst()
            assertEquals(users.size, testSize)
        } catch(e: Exception) {
            fail("Got an exception when trying to get users in building $buildingId")
        }
    }

    private fun createBuilding(buildingEntity: BuildingEntity): String {
        val single = buildingDataSource.create(buildingEntity)
        try {
            val returnedEntity = single.blockingGet()
            assertNotNull(returnedEntity.id)
            assertEquals(buildingEntity.buildingName, returnedEntity.buildingName)
            assertEquals(buildingEntity.capacity, returnedEntity.capacity)
            return returnedEntity.id!!
        } catch (e: Exception) {
            fail("Attempted to create a building. Got an exception instead: ${e.localizedMessage}")
            return "na"
        }
    }

    private fun deleteBuilding(buildingId: String, expectError: Boolean = false) {
        val completable = dataSource.delete(buildingId)
        try {
            completable.blockingAwait()
            if (expectError) fail("Expected an exception when trying to delete building by id")
        } catch (e: Exception) {
            if (!expectError) fail("Got an exception when trying to delete building by id")
        }
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