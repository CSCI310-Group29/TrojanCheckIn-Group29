package com.csci310_group29.trojancheckincheckout.data.datasource.rest

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.data.retrofit.UserWebService
import com.csci310_group29.trojancheckincheckout.di.RetrofitModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class UserRestDataSourceTest {

    companion object {
        private val LOCAL_URL = "http://localhost:5001/trojancheckin/us-central1/api/"
        private val TAG = "UserRestDataSourceTest"
    }

    private lateinit var dataSource: UserRestDataSource

    @Before
    fun setup() {
        dataSource = UserRestDataSource(buildService())
    }

    fun buildService(): UserWebService {
        return Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(UserWebService::class.java)
    }

    @Test
    fun test() {
        val single = dataSource.get("1")
        try {
            val user = single.blockingGet()
        } catch(e: Exception) {
            Log.d(TAG, e.localizedMessage)
        }
    }
}