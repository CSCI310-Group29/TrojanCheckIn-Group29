package com.csci310_group29.trojancheckincheckout.domain.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MessagingRepository {
    fun updateDeviceToken(userId: String, token: String): Completable

    fun removeDeviceToken(userId: String, token: String): Completable

    fun getToken(): Single<String>
}