package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {

    fun get(id: String): Single<UserEntity>

    fun update(userEntity: UserEntity): Completable

    fun delete(id: String): Completable

    fun create(userEntity: UserEntity): Single<UserEntity>

    fun setCheckedIn(userId: String, checkedIn: Boolean): Single<UserEntity>

    fun updatePhotoUrl(id: String, url: String): Completable

    fun queryCheckedInUsers(buildingName: String? = null, user: User): Single<List<User>>
}