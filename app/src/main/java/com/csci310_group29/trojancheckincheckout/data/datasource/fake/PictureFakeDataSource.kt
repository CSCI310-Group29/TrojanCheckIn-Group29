package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PictureFakeDataSource @Inject constructor(): PicturesRepository {

    override fun get(url: String): Single<ByteArray> {
        return Single.just(ByteArray(1024))
    }

    override fun create(url: String, picture: ByteArray): Completable {
        return Completable.complete()
    }

    override fun delete(url: String): Completable {
        return Completable.complete()
    }
}