package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Single
import javax.inject.Inject

class PictureFirebaseDataSource @Inject constructor(): PicturesRepository {
    override fun getProfilePicture(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun getQrCode(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(picture: ByteArray): Single<String> {
        TODO("Not yet implemented")
    }
}