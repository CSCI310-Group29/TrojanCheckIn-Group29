package com.csci310_group29.trojancheckincheckout.data.retrofit

import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserWebService {
    @PUT("users/1")
    fun testService(): Single<UserEntity>
}