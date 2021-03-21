package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface UserRepository {

    fun get(id: String): Single<UserEntity>

    fun update(userEntity: UserEntity): Completable

    fun delete(id: String): Completable

    fun create(userEntity: UserEntity): Single<UserEntity>

    fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity>

    fun updatePhotoUrl(id: String, url: String): Completable

    fun query(userQuery: UserQuery, visitQuery: VisitQuery): Observable<UserEntity>
}