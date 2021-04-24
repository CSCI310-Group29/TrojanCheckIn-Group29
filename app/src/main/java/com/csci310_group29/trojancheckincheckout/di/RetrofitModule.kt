package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.retrofit.MessagingWebService
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Interceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val LOCAL_URL = "http://10.0.2.2:5001/trojancheckin/us-central1/api/"
    private val PROD_URL = "https://us-central1-trojancheckin.cloudfunctions.net/api/"


    @Singleton
    @Provides
    fun provideMessagingWebService(): MessagingWebService {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(PROD_URL)
            .client(getHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MessagingWebService::class.java)
    }

    fun getHttpClient(): OkHttpClient {
        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return okhttp3.OkHttpClient
            .Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }
}