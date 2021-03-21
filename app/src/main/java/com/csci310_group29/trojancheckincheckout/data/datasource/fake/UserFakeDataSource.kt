package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


private val userEntity = UserEntity("12", true, "Tommy", "Trojan", "Compute Science", "building", "1234", "exampleURL")

class UserFakeDataSource @Inject constructor(): UserRepository {
    override fun get(id: String): Single<UserEntity> {
        return Single.just(userEntity)
    }

    override fun update(userEntity: UserEntity): Completable {
        return Completable.complete()
    }

    override fun delete(id: String): Completable {
        return Completable.complete()
    }

    override fun create(userEntity: UserEntity): Single<UserEntity> {
        return Single.just(userEntity)
    }

    override fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity> {
        return Single.just(userEntity)
    }

    override fun updatePhotoUrl(id: String, url: String): Completable {
        return Completable.complete()
    }

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<UserEntity>> {
        return Single.just(listOf(userEntity))
    }

}