package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.datasource.remote.PictureFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class PicturesRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: PicturesRepository): PicturesRepository {
    override fun get(url: String): Single<ByteArray> {
        return remoteDataSource.get(url)
    }

    override fun create(url: String, picture: ByteArray): Completable {
        return remoteDataSource.create(url, picture)
    }

    override fun delete(url: String): Completable {
        return remoteDataSource.delete(url)
    }
}