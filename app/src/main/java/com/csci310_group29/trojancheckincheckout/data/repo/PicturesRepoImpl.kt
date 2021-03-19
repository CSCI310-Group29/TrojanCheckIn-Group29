package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.datasource.remote.PictureFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class PicturesRepoImpl @Inject constructor(@Named("Data") private val remoteDataSource: PicturesRepository): PicturesRepository {
    override fun get(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun create(picture: ByteArray): Single<String> {
        TODO("Not yet implemented")
    }

    override fun update(url: String, picture: ByteArray): Completable {
        TODO("Not yet implemented")
    }

    override fun delete(url: String): Completable {
        TODO("Not yet implemented")
    }
}