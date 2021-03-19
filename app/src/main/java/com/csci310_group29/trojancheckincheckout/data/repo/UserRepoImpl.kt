package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class UserRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: UserRepository): UserRepository {

    override fun get(id: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun update(userEntity: UserEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): Completable {
        TODO("Not yet implemented")
    }

    override fun setCheckedIn(userId: String, checkedIn: Boolean): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun updatePhotoUrl(id: String): Completable {
        TODO("Not yet implemented")
    }

    override fun queryCheckedInUsers(buildingName: String?, user: User): Single<List<User>> {
        return remoteDataSource.queryCheckedInUsers(buildingName, user)
    }
}