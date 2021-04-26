package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.repo.MessagingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class MessagingRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: MessagingRepository): MessagingRepository {
    override fun updateDeviceToken(userId: String, token: String): Completable {
        return remoteDataSource.updateDeviceToken(userId, token)
    }

    override fun removeDeviceToken(userId: String, token: String): Completable {
        return remoteDataSource.removeDeviceToken(userId, token)
    }

    override fun getToken(): Single<String> {
        return remoteDataSource.getToken()
    }
}