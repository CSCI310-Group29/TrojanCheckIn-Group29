package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class PicturesRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: PicturesRepository): PicturesRepository {
    override fun get(url: String): Single<ByteArray> {
        return remoteDataSource.get(url)
    }

    override fun getFromExternalUrl(url: String): Single<ByteArray> {
        return remoteDataSource.getFromExternalUrl(url)
    }

    override fun create(url: String, picture: ByteArray): Completable {
        return remoteDataSource.create(url, picture)
    }

    override fun delete(url: String): Completable {
        return remoteDataSource.delete(url)
    }
}