package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.retrofit.MessagingWebService
import com.csci310_group29.trojancheckincheckout.di.RetrofitModule
import com.csci310_group29.trojancheckincheckout.domain.repo.MessagingRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import com.squareup.okhttp.ResponseBody
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class MessagingFirebaseDataSource @Inject constructor(
    private val messaging: FirebaseMessaging,
    private val messagingRestService: MessagingWebService): MessagingRepository {

    private val testService: MessagingWebService = RetrofitModule.provideMessagingWebService()
    companion object {
        private val TAG = "MessagingFirebaseDataSource"
    }
    override fun updateDeviceToken(userId: String, token: String): Completable {
        return Completable.create { emitter ->
            Log.d(TAG, "updating device token")
            messagingRestService.updateDeviceToken(userId, token).enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>,  response: retrofit2.Response<ResponseBody>) {
                    Log.d(TAG, "code: ${response.code()}")
                    emitter.onComplete()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "error: ${t.localizedMessage}")
                    emitter.onComplete()
                }
            })
        }
    }



    override fun removeDeviceToken(userId: String, token: String): Completable {
        return Completable.create { emitter ->
            Log.d(TAG, "removing device token")
            messagingRestService.removeDeviceToken(userId, token).enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>,  response: retrofit2.Response<ResponseBody>) {
                    Log.d(TAG, "code: ${response.code()}")
                    emitter.onComplete()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "error: ${t.localizedMessage}")
                    emitter.onComplete()
                }
            })
        }
    }

    override fun getToken(): Single<String> {
        return Single.create { emitter ->
            messaging.token
                .addOnSuccessListener { token ->
                    emitter.onSuccess(token)
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

}