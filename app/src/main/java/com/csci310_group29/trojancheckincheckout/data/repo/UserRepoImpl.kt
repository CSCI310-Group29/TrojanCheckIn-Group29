package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class UserRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: UserRepository): UserRepository {

    override fun get(id: String): Single<UserEntity> {
        return remoteDataSource.get(id)
    }

    override fun update(userEntity: UserEntity): Completable {
        return remoteDataSource.update(userEntity)
    }

    override fun delete(id: String): Completable {
        return remoteDataSource.delete(id)
    }

    override fun create(userEntity: UserEntity): Single<UserEntity> {
        return remoteDataSource.create(userEntity)
    }

    override fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity> {
        return remoteDataSource.setCheckedInBuilding(userId, buildingId)
    }

    override fun updatePhotoUrl(id: String, url: String): Completable {
        return remoteDataSource.updatePhotoUrl(id, url)
    }

    override fun query(userQuery: UserQuery, visitQuery: VisitQuery): Single<List<UserEntity>> {
        return remoteDataSource.query(userQuery, visitQuery)
    }
}