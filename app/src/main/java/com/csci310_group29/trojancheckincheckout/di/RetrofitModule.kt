package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.datasource.rest.UserRestDataSource
import com.csci310_group29.trojancheckincheckout.data.retrofit.UserWebService
import com.google.gson.Gson
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    companion object {
        private val LOCAL_URL = "http://localhost:5001/trojancheckin/us-central1/api"
    }
    fun provideUserWebService(): UserWebService {
        return Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(UserWebService::class.java)
    }
}