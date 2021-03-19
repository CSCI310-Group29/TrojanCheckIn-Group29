package com.csci310_group29.trojancheckincheckout.data.repo

import com.csci310_group29.trojancheckincheckout.data.remote.PictureFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Single
import javax.inject.Inject

class PicturesRepoImpl @Inject constructor(private val remoteDataSource: PictureFirebaseDataSource): PicturesRepository {
    override fun getProfilePicture(url: String): Single<ByteArray> {
        return remoteDataSource.getProfilePicture(url)
    }

    override fun getQrCode(url: String): Single<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(picture: ByteArray): Single<String> {
        TODO("Not yet implemented")
    }
}