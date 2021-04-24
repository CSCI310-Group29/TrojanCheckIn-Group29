package com.csci310_group29.trojancheckincheckout.data.retrofit

import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.squareup.okhttp.ResponseBody
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MessagingWebService {
    @PUT("users/{userId}/{token}")
    fun updateDeviceToken(@Path("userId") userId: String, @Path("token") token: String): Call<ResponseBody>

    @GET("helloworld")
    fun helloworld(): Call<ResponseBody>

    @DELETE("users/{userId}/{token}")
    fun removeDeviceToken(@Path("userId") userId: String, @Path("token") token: String): Call<ResponseBody>
}