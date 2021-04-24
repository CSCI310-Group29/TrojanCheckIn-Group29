package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.datasource.firebase.UserFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class UserRepoImpl @Inject constructor(
    @Named("Data") private val firebaseDataSource: UserRepository): UserRepository {

    override fun get(id: String): Single<UserEntity> {
        return firebaseDataSource.get(id)
    }

    override fun observeUsersInBuilding(buildingId: String): Observable<List<UserEntity>> {
        return firebaseDataSource.observeUsersInBuilding(buildingId)
    }

    override fun observeUserById(userId: String): Observable<UserEntity> {
        return firebaseDataSource.observeUserById(userId)
    }

    override fun update(userEntity: UserEntity): Completable {
        return firebaseDataSource.update(userEntity)
    }

    override fun delete(id: String): Completable {
        return firebaseDataSource.delete(id)
    }

    override fun getAll(): Single<List<UserEntity>> {
        return firebaseDataSource.getAll()
    }

    override fun addDeleteField(id: String): Completable {
        return firebaseDataSource.addDeleteField(id)
    }

    override fun create(userEntity: UserEntity): Single<UserEntity> {
        return firebaseDataSource.create(userEntity)
    }

    override fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity> {
        return firebaseDataSource.setCheckedInBuilding(userId, buildingId)
    }

    override fun updatePhotoUrl(id: String, url: String): Completable {
        return firebaseDataSource.updatePhotoUrl(id, url)
    }
}