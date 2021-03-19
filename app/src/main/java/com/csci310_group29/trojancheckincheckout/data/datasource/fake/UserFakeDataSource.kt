package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


private val userEntity = UserEntity("12", true, "Tommy", "Trojan", "Compute Science", false, "1234", "exampleURL")

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

    override fun setCheckedIn(userId: String, checkedIn: Boolean): Single<UserEntity> {
        return Single.just(userEntity)
    }

    override fun updatePhotoUrl(id: String): Completable {
        return Completable.complete()
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        TODO("Not yet implemented")
    }
}