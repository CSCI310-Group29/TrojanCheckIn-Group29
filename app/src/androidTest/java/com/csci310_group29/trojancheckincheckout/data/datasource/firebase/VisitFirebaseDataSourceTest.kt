package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
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

@RunWith(AndroidJUnit4::class)
class VisitFirebaseDataSourceTest {

    companion object {
        private val TAG = "BuildingFirebaseDataSourceTest"
        private val DB_PORT = 8080
        private val HOST = "10.0.2.2"
    }

    private lateinit var visitDataSource: VisitFirebaseDataSource
    private lateinit var userDataSource: UserFirebaseDataSource
    private lateinit var buildingDataSource: BuildingFirebaseDataSource
    private lateinit var app: FirebaseApp
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        app = FirebaseApp.initializeApp(context)!!
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        val db = Firebase.firestore
        db.firestoreSettings = settings
        db.useEmulator(HOST, DB_PORT)
        visitDataSource = VisitFirebaseDataSource(db)
        userDataSource = UserFirebaseDataSource(db)
        buildingDataSource = BuildingFirebaseDataSource(db)
    }

    @After
    fun teardown() {
        app.delete()
    }

    @Test
    fun createGetDeleteTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false, "ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 2, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitId = createVisit(userId, buildingId)
        getVisit(userId, visitId)
        deleteVisit(userId, visitId)
        getVisit(userId, visitId, true)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun createCheckoutTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 2, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitId = createVisit(userId, buildingId)
        checkOutVisit(userId, visitId)
        deleteVisit(userId, visitId)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun getLatestVisitTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 2, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitId = createVisit(userId, buildingId)
        checkOutVisit(userId, visitId)
        val visitId2 = createVisit(userId, buildingId)
        getLatestVisit(userId, visitId2)
        deleteVisit(userId, visitId)
        deleteVisit(userId, visitId2)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun checkInWithOpenCapacityTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 2, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitId = runCheckInTransaction(userId, buildingId)
        checkUserBuildingId(userId, buildingId)
        checkBuildingNumPeople(buildingId, buildingEntity.numPeople?.plus(1)!!)
        getVisit(userId, visitId)
        deleteVisit(userId, visitId)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun checkInAtFullCapacityTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 10, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitId = runCheckInTransaction(userId, buildingId, true)
        checkBuildingNumPeople(buildingId, buildingEntity.numPeople!!)
        checkUserBuildingId(userId, null)
        getVisit(userId, visitId, true)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun checkOutSuccessfullyTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 8, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        val visitIdCheckIn = runCheckInTransaction(userId, buildingId)
        checkBuildingNumPeople(buildingId, buildingEntity.numPeople?.plus(1)!!)
        checkUserBuildingId(userId, buildingId)
        val visitIdCheckOut = runCheckOutTransaction(userId, visitIdCheckIn, buildingId)
        checkBuildingNumPeople(buildingId, buildingEntity.numPeople!!)
        checkUserBuildingId(userId, null)
        deleteBuilding(buildingId)
        deleteUser(userId)
    }

    @Test
    fun visitHistorySingleVisitTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 8, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        var visitId = runCheckInTransaction(userId, buildingId)
        visitId = runCheckOutTransaction(userId, visitId, buildingId)
        val visitQuery = VisitQuery()
        getUserVisitHistory(userId, visitQuery, 1)
        deleteVisit(userId, visitId)
        getUserVisitHistory(userId, visitQuery, 0)
        deleteUser(userId)
        deleteBuilding(buildingId)
    }

    @Test
    fun visitHistoryTwoVisitsTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 8, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        var visitId = runCheckInTransaction(userId, buildingId)
        visitId = runCheckOutTransaction(userId, visitId, buildingId)
        var visitId2 = runCheckInTransaction(userId, buildingId)
        visitId2 = runCheckOutTransaction(userId, visitId2, buildingId)
        val visitQuery = VisitQuery()
        getUserVisitHistory(userId, visitQuery, 2)
        deleteVisit(userId, visitId)
        getUserVisitHistory(userId, visitQuery, 1)
        deleteVisit(userId, visitId2)
        getUserVisitHistory(userId, visitQuery, 0)
        deleteUser(userId)
        deleteBuilding(buildingId)
    }

    @Test
    fun searchVisitsOneVisitTest() {
        val userEntity = UserEntity("1", true, "Tommy",
            "Trojan", "Computer Science", null, "2", false,"ref")
        val buildingEntity = BuildingEntity("2", "A", "A", 10, 0, "ref")
        val userId = createUser(userEntity)
        val buildingId = createBuilding(buildingEntity)
        var visitId = runCheckInTransaction(userId, buildingId)
        visitId = runCheckOutTransaction(userId, visitId, buildingId)
        var visitId2 = runCheckInTransaction(userId, buildingId)
        visitId2 = runCheckInTransaction(userId, buildingId)
        val visitId3 = runCheckInTransaction(userId, buildingId)
        val visit2 = getVisit(userId, visitId2)
        val visit3 = getVisit(userId, visitId3)
        val visitQuery = VisitQuery(buildingId = buildingId, start = visit2?.checkIn,
            end = visit3?.checkIn)
        searchVisits(visitQuery, 2)
        deleteVisit(userId, visitId)
        deleteVisit(userId, visitId2)
        deleteVisit(userId, visitId3)
        deleteUser(userId)
        deleteBuilding(buildingId)
    }


    private fun runCheckInTransaction(userId: String, buildingId: String, expectError: Boolean = false): String {
        val single = visitDataSource.runCheckInTransaction(userId, buildingId)
        try {
            val visitEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(userId, visitEntity.userId)
                assertEquals(buildingId, visitEntity.buildingId)
                return visitEntity.id!!
            } else {
                fail("expected an exception when trying to run transaction")
                return "na"
            }
        } catch(e: Exception) {
            if (!expectError) {
                fail("got an exception when trying to run transaction: ${e.localizedMessage}")
            }
            return "na"
        }
    }

    private fun searchVisits(visitQuery: VisitQuery, testSize: Int) {
        val single = visitDataSource.query(visitQuery)
        try {
            val visits = single.blockingGet()
            assertEquals(visits.size, testSize)
        } catch(e: Exception) {
            fail("got an exception when trying to get visits")
        }
    }

    private fun checkBuildingNumPeople(buildingId: String, testNumPeople: Int) {
        val single = buildingDataSource.get(buildingId)
        try {
            val buildingEntity = single.blockingGet()
            assertEquals(testNumPeople, buildingEntity.numPeople)
        } catch(e: Exception) {
            fail("got an exception when trying to check building num people: ${e.localizedMessage}")
        }
    }

    private fun getUserVisitHistory(userId: String, visitQuery: VisitQuery, testSize: Int, expectError: Boolean = false) {
        val single = visitDataSource.getUserVisitHistory(userId, visitQuery)
        try {
            val visitEntities = single.blockingGet()
            if (!expectError) {
                assertEquals(testSize, visitEntities.size)
            } else {
                fail("expected an exception when trying to get visit history")
            }
        } catch(e: Exception) {
            if (!expectError) fail("got an exception when trying to get user visit history: ${e.localizedMessage}")
        }
    }

    fun checkUserBuildingId(userId: String, buildingId: String?) {
        val single = userDataSource.get(userId)
        try {
            val userEntity = single.blockingGet()
            assertEquals(buildingId, userEntity.checkedInBuildingId)
        } catch(e: Exception) {
            fail("got an exception when trying to get the user's buildingId: ${e.localizedMessage}")
        }
    }

    private fun runCheckOutTransaction(userId: String, visitId: String, buildingId: String, expectError: Boolean = false): String {
        val single = visitDataSource.runCheckOutTransaction(userId, visitId, buildingId)
        try {
            val visitEntity = single.blockingGet()
            if (!expectError) {
                assertNotNull(visitEntity.checkOut)
                assertEquals(visitId, visitEntity.id)
                assertEquals(userId, visitEntity.userId)
                assertEquals(buildingId, visitEntity.buildingId)
                return visitEntity.id!!
            } else {
                fail("expected an exception when trying to run check out transaction")
                return "na"
            }
        } catch(e: Exception) {
            if (!expectError) fail("got an exception when trying to run check out transaction")
            return "na"
        }
    }


    private fun createUser(userN: UserEntity): String {
        val single = userDataSource.create(userN)
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
        val single = userDataSource.get(id)
        try {
            val user = single.blockingGet()
            Log.d(TAG, user.toString())
            if (!expectError) assertEquals(user.firstName, user.firstName)
            else fail("Expected an exception")
        } catch(e: Exception) {
            if (!expectError) fail("Expected a user. Got an exception instead")
        }
    }

    private fun deleteUser(id: String) {
        val completable = userDataSource.delete(id)
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("Could not delete user: ${e.localizedMessage}")
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
        val completable = buildingDataSource.delete(buildingId)
        try {
            completable.blockingAwait()
            if (expectError) fail("Expected an exception when trying to delete building by id")
        } catch (e: Exception) {
            if (!expectError) fail("Got an exception when trying to delete building by id")
        }
    }

    private fun createVisit(userId: String, buildingId: String): String {
        val single = visitDataSource.create(userId, buildingId)
        try {
            val visitEntity = single.blockingGet()
            assertEquals(userId, visitEntity.userId)
            assertEquals(buildingId, visitEntity.buildingId)
            return visitEntity.id!!
        } catch(e: Exception) {
            fail("got an exception when trying to create a visit: ${e.localizedMessage}")
            return "12"
        }
    }

    private fun getVisit(userId: String, visitId: String, expectError: Boolean = false): VisitEntity? {
        val single = visitDataSource.get(userId, visitId)
        try {
            val visitEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(userId, visitEntity.userId)
                assertEquals(visitId, visitEntity.id)
                return visitEntity
            } else {
                fail("Expected an exception when trying to get visit")
                return null
            }
        } catch(e: Exception) {
            if (!expectError) fail("Got an exception when trying to get a visit: ${e.localizedMessage}")
            return null
        }
    }

    private fun deleteVisit(userId: String, visitId: String) {
        val completable = visitDataSource.delete(userId, visitId)
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("got an exception when deleting visit: ${e.localizedMessage}")
        }
    }

    private fun getLatestVisit(userId: String, testVisitId: String) {
        val single = visitDataSource.getLatestVisit(userId)
        try {
            val visitEntity = single.blockingGet()
            assertEquals(userId, visitEntity.userId)
            assertEquals(testVisitId, visitEntity.id)
        } catch(e: Exception) {
            fail("got an exception when trying to get the latest visit")
        }
    }

    private fun checkOutVisit(userId: String, visitId: String) {
        val single = visitDataSource.checkOutVisit(userId, visitId)
        try {
            val visitEntity = single.blockingGet()
            assertEquals(userId, visitEntity.userId)
            assertEquals(visitId, visitEntity.id)
            assertNotNull(visitEntity.checkOut)
        } catch(e: Exception) {
            fail("got an exception when trying to check out visit: ${e.localizedMessage}")
        }
    }

}