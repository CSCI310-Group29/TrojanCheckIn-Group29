package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BuildingFirebaseDataSourceTest {
    companion object {
        private val TAG = "BuildingFirebaseDataSourceTest"
        private val DB_PORT = 8080
        private val HOST = "10.0.2.2"
    }

    private lateinit var dataSource: BuildingFirebaseDataSource
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
        dataSource = BuildingFirebaseDataSource(db)
    }

    @After
    fun teardown() {
        app.delete()
    }

    @Test
    fun createGetDeleteTest() {
        val buildingEntity = BuildingEntity(
            id = "1",
            buildingName = "A",
            address = "A",
            capacity = 10,
            numPeople = 4,
            qrCodeRef = "ref"
        )
        val id = createBuilding(buildingEntity)
        try {
            getBuildingById(id)
        } catch (e: Exception) {
            delete(id)
            throw e
        }
        delete(id)
    }

    @Test
    fun getInvalidBuildingTest() {
        getBuildingById("invalid id", true)
    }

    @Test
    fun incrementNumPeopleWhenOpenTest() {
        val buildingEntity = BuildingEntity(
            id = "1",
            buildingName = "A",
            address = "A",
            capacity = 10,
            numPeople = 4,
            qrCodeRef = "ref"
        )
        val id = createBuilding(buildingEntity)
        try  {
            incrementNumPeople(id, buildingEntity.numPeople!!.toDouble(), 1.0)
        } catch (e: Exception) {
            delete(id)
            throw e
        }
        delete(id)
    }

    @Test
    fun incrementNumPeopleWhenClosedTest() {
        val buildingEntity = BuildingEntity(
            id = "1",
            buildingName = "A",
            address = "A",
            capacity = 10,
            numPeople = 10,
            qrCodeRef = "ref"
        )
        val id = createBuilding(buildingEntity)
        try {
            incrementNumPeople(id, buildingEntity.numPeople!!.toDouble(), 1.0, true)
        } catch (e: Exception) {
            delete(id)
            throw e
        }
        delete(id)
    }

    @Test
    fun getAllBuildingsTest() {
        val buildingEntities = mutableListOf<BuildingEntity>()
        val sampleBuildingEntity = BuildingEntity(
            "1", "A", "A", 10, 5, "ref"
        )
        for (i in 1..4) {
            buildingEntities.add(sampleBuildingEntity)
        }
        for (entity in buildingEntities) {
            createBuilding(entity)
        }
        try {
            val returnedBuildingEntities = getAllBuildings(buildingEntities.size)
            for (entity in returnedBuildingEntities) {
                delete(entity.id!!)
            }
        } catch (e: AssertionError) {
            throw e
        }
        getAllBuildings(0)

    }

    @Test
    fun updateCapacitiesTest() {
        val buildingEntities = mutableListOf<BuildingEntity>()
        val sampleBuildingEntity = BuildingEntity(
            "1", "A", "A", 10, 5, "ref"
        )
        for (capacity in 1..4) {
            buildingEntities.add(sampleBuildingEntity)
        }
        val buildingCapacities = hashMapOf<String, Double>()
        for (entity in buildingEntities) {
            val id = createBuilding(entity)
            buildingCapacities[id] = 20.0
        }
        updateCapacities(buildingCapacities)
        for (id in buildingCapacities.keys) {
            delete(id)
        }
    }



    private fun createBuilding(buildingEntity: BuildingEntity): String {
        val single = dataSource.create(buildingEntity)
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

    private fun getBuildingById(id: String, expectError: Boolean = false) {
        val single = dataSource.get(id)
        try {
            val returnedEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(id, returnedEntity.id)
            } else {
                fail("Expected an exception when trying to get a building by its id")
            }
        } catch (e: Exception) {
            if (!expectError) {
                fail("Attempted to get a building by its id. Got an exception instead: ${e.localizedMessage}")
            }
        }
    }

    private fun getBuildingByName(buildingName: String, expectError: Boolean = false) {
        val single = dataSource.get(buildingName)
        try {
            val returnedEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(buildingName, returnedEntity.buildingName)
            } else {
                fail("Expected an exception when trying to get a building by its name")
            }
        } catch (e: Exception) {
            if (!expectError) {
                fail("Attempted to get a building by its name. Got an exception instead: ${e.localizedMessage}")
            }
        }
    }

    private fun updateCapacities(buildingCapacities: HashMap<String, Double>, expectError: Boolean = false) {
        val completable = dataSource.updateCapacities(buildingCapacities)
        try {
            completable.blockingAwait()
            buildingCapacities.forEach { buildingId, capacity ->
                testCapacity(buildingId, capacity.toInt(), expectError)
            }
        } catch(e: Exception) {
            if (!expectError) {
                fail("got an exception when trying to update building capacities: ${e.localizedMessage}")
            }
        }
    }

    private fun testCapacity(buildingId: String, capacity: Int, expectError: Boolean = false) {
        val single = dataSource.get(buildingId)
        try {
            val buildingEntity = single.blockingGet()
            if (expectError) {
                assertNotEquals(capacity, buildingEntity.capacity)
            } else {
                assertEquals(capacity, buildingEntity.capacity)
            }
        } catch(e: Exception) {
            fail("got an error when testing capacity")
        }
    }

    private fun incrementNumPeople(
        buildingId: String,
        prevNumPeople: Double,
        increment: Double,
        expectError: Boolean = false
    ) {
        val single = dataSource.incrementNumPeople(buildingId, increment)
        try {
            val returnedEntity = single.blockingGet()
            if (!expectError) {
                assertEquals(buildingId, returnedEntity.id)
                assertEquals((prevNumPeople + increment).toInt(), returnedEntity.numPeople)
            } else {
                fail("Expected an error when trying to increment num people")
            }

        } catch (e: Exception) {
            if (!expectError) {
                fail("Got an exception when trying to increment num people: ${e.localizedMessage}")
            }
        }
    }

    private fun delete(buildingId: String, expectError: Boolean = false) {
        val completable = dataSource.delete(buildingId)
        try {
            completable.blockingAwait()
            if (expectError) fail("Expected an exception when trying to delete building by id")
        } catch (e: Exception) {
            if (!expectError) fail("Got an exception when trying to delete building by id")
        }
    }

    private fun getAllBuildings(size: Int, expectError: Boolean = false): List<BuildingEntity> {
        val single = dataSource.getAll()
        return try {
            val buildingEntities = single.blockingGet()
            if (!expectError) assertEquals(size, buildingEntities.size)
            buildingEntities
        } catch (e: Exception) {
            if (!expectError) fail("Expected an exception when trying to get all buildings")
            listOf<BuildingEntity>()
        }
    }
}