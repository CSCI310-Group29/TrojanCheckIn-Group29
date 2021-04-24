package com.csci310_group29.trojancheckincheckout.data.datasource.rest

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.data.retrofit.MessagingWebService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class UserRestDataSourceTest {

    companion object {
        private val LOCAL_URL = "http://10.0.2.2:5001/trojancheckin/us-central1/api/"
        private val TAG = "UserRestDataSourceTest"
    }

    private lateinit var service: MessagingWebService

    @Before
    fun setup() {
        service = buildService()
    }

    fun buildService(): MessagingWebService {
        return Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(MessagingWebService::class.java)
    }

    @Test
    fun test() {
        val completable = service.helloworld()
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            Log.d(TAG, e.localizedMessage)
        }
    }
}