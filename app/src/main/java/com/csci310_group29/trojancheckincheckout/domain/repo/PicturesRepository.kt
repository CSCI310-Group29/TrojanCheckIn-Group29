package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import io.reactivex.Single

interface PicturesRepository {
    fun getProfilePicture(url: String): Single<Bitmap>

    fun getQrCode(url: String): Single<Bitmap>

    fun updateProfilePicture(picture: Bitmap): Single<String>
}