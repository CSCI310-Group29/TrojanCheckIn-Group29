package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.inject.Inject

class PictureFirebaseDataSource @Inject constructor(private val storage: FirebaseStorage): PicturesRepository {

    companion object {
        private val TAG = "PictureFirebaseDataSource"
    }

    override fun get(url: String): Single<ByteArray> {
        Log.d(TAG, "Getting picture at url $url")
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

    override fun getFromExternalUrl(url: URL): Single<ByteArray> {
        return Single.create { emitter ->
            try {
                val buffer = ByteArrayOutputStream()
                val inputStream = url.openStream()
                val data = ByteArray(4096)
                var n = inputStream.read(data, 0, data.size)
                while (n != -1) {
                    buffer.write(data, 0, n)
                    n = inputStream.read(data, 0, data.size)
                }
                emitter.onSuccess(buffer.toByteArray())
            } catch(e: Exception) {
                Log.d(TAG, e.toString())
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