package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PictureFirebaseDataSource @Inject constructor(): PicturesRepository {

    private val storage = FirebaseStorage.getInstance()

    override fun get(url: String): Single<ByteArray> {
        return Single.create { emitter ->
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            val ONE_MEGABYTE: Long = 1024 * 1024
            pictureRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { picture ->
                    emitter.onSuccess(picture)
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun create(url: String, picture: ByteArray): Completable {
        return Completable.create { emitter ->
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            pictureRef.putBytes(picture)
                .addOnSuccessListener { taskSnapshot ->
                    emitter.onComplete()
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }

    }

    override fun delete(url: String): Completable {
        return Completable.create { emitter ->
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            pictureRef.delete()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e -> emitter.onError(e)}
        }
    }
}