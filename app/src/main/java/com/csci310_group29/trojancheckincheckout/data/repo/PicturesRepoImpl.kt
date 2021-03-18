package com.csci310_group29.trojancheckincheckout.data.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import io.reactivex.Single

class PicturesRepoImpl: PicturesRepository {
    override fun getProfilePicture(url: String): Single<Bitmap> {
        TODO("Not yet implemented")
    }

    override fun getQrCode(url: String): Single<Bitmap> {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(picture: Bitmap): Single<String> {
        TODO("Not yet implemented")
    }
}