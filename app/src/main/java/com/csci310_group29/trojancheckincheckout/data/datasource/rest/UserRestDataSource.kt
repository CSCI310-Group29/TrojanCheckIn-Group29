package com.csci310_group29.trojancheckincheckout.data.datasource.rest

import com.csci310_group29.trojancheckincheckout.data.retrofit.UserWebService
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

private val userEntity = UserEntity("12", true,
    "Tommy", "Trojan", "Compute Science",
    "building", "1234", false, "exampleURL")

open class UserRestDataSource @Inject constructor(private val webService: UserWebService): UserRepository {
    override fun get(id: String): Single<UserEntity> {
        return webService.testService()
    }

    override fun observeUsersInBuilding(buildingId: String): Observable<List<UserEntity>> {
        TODO("Not yet implemented")
    }

    override fun observeUserById(userId: String): Observable<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun update(userEntity: UserEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getAll(): Single<List<UserEntity>> {
        TODO("Not yet implemented")
    }

    override fun addDeleteField(id: String): Completable {
        TODO("Not yet implemented")
    }

    override fun create(userEntity: UserEntity): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun updatePhotoUrl(id: String, url: String): Completable {
        TODO("Not yet implemented")
    }

    override fun updateDeviceToken(id: String, token: String): Completable {
        TODO("Not yet implemented")
    }

}