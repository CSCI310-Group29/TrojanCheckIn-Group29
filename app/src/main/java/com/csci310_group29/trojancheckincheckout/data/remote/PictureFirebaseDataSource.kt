package com.csci310_group29.trojancheckincheckout.data.remote

import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Single

class PictureFirebaseDataSource: PicturesRepository {
    override fun getProfilePicture(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun getQrCode(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(picture: ByteArray): Single<String> {
        return Single.just("exampleURL")
    }
}