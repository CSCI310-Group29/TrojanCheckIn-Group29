package com.csci310_group29.trojancheckincheckout.data.datasources

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception


private val userEntity = UserEntity("12", true, "Tommy", "Trojan", "Computer Science", false, "123", "exampleURL")
@RunWith(AndroidJUnit4::class)
class UserFirebaseDataSourceTest {

    private var dataSource = UserFirebaseDataSource()

//    @Before
//    fun init() {
//        dataSource = UserFirebaseDataSource()
//    }

    @Test
    fun run() {
        val single = dataSource.get("ecZ1I9BHMMYUBG4LKyap").blockingGet()
        createAndGetTest()
    }
    fun createAndGetTest() {
        val id = createUser(userEntity)
        getUser(id)
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

    private fun getUser(id: String) {
        val single = dataSource.get(id)
        try {
            val user = single.blockingGet()
            assertEquals(user.firstName, userEntity.firstName)
        } catch(e: Exception) {
            assertTrue(false)
        }
    }
}