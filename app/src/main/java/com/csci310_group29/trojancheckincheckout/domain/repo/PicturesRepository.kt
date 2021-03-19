package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import io.reactivex.Single

interface PicturesRepository {
    fun getProfilePicture(url: String): Single<ByteArray>

    fun getQrCode(url: String): Single<ByteArray>

    fun updateProfilePicture(picture: ByteArray): Single<String>
}