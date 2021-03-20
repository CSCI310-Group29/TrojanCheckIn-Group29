package com.csci310_group29.trojancheckincheckout.data.datasource.remote

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PictureFirebaseDataSource @Inject constructor(): PicturesRepository {

    companion object {
        private val TAG = "PictureFirebaseDataSource"
    }

    private val storage = FirebaseStorage.getInstance()

    override fun get(url: String): Single<ByteArray> {
        Log.d(TAG, "Getting picture")
        return Single.create { emitter ->
            val storageRef = storage.reference
            val pictureRef = storageRef.child(url)
            pictureRef.stream
                .addOnSuccessListener { downloadTask ->
                    Log.d(TAG, "Successfully got image")
                    val bytesTransferred = downloadTask.bytesTransferred
                    val picture = ByteArray(bytesTransferred.toInt())
                    downloadTask.stream.read(picture)
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