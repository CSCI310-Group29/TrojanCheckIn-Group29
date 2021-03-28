package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PictureFirebaseDataSource @Inject constructor(private val storage: FirebaseStorage): PicturesRepository {

    companion object {
        private val TAG = "PictureFirebaseDataSource"
    }

    override fun get(url: String): Single<ByteArray> {
        Log.d(TAG, "Getting picture")
        return Single.create { emitter ->
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            val ONE_MEGABYTE: Long = 1024 * 1024 * 5
            pictureRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { picture ->
                    Log.d(TAG, "Successfully got image")
                    Log.d(TAG, "total bytes: ${picture.size}")
                    emitter.onSuccess(picture)
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.message.toString())
                    emitter.onError(e)
                }
        }
    }

    override fun create(url: String, picture: ByteArray): Completable {
        return Completable.create { emitter ->
            Log.d(TAG, "creating image")
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            pictureRef.putBytes(picture)
                .addOnSuccessListener { taskSnapshot ->
                    Log.d(TAG, "successfully created image")
                    emitter.onComplete()
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, e.message.toString())
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