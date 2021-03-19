package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Single

interface PicturesRepository {
    fun get(url: String): Single<ByteArray>

    fun create(picture: ByteArray): Single<String>

    fun update(url: String, picture: ByteArray): Completable

    fun delete(url: String): Completable
}