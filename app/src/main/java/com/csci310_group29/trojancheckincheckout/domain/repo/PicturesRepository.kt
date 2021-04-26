package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PicturesRepository {
    /*
    Retrieves a picture based on its location in online storage

        Params:
            url: String representing the location of the picture in the online storage

        Returns:
            Single that emits a ByteArray if the picture was retrieved, or an error otherwise.
     */
    fun get(url: String): Single<ByteArray>

    fun getFromExternalUrl(url: String): Single<ByteArray>

    /*
    Creates a picture in a specified location in online storage

        Params:
            url: String representing the location in online storage the picture should be uploaded to
                picture: ByteArray specifying the picture to be uploaded

        Returns:
            Completable that emits completion if the picture was successfully updated, or an error
                otherwise
     */
    fun create(url: String, picture: ByteArray): Completable

    /*
    Deletes a picture based on its location in online storage

        Params:
            url: String representing the location of the picture in online storage

        Returns:
            Completable that emits completion if the picture was deleted, or an error otherwise
     */
    fun delete(url: String): Completable
}