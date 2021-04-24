package com.csci310_group29.trojancheckincheckout.services

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.repo.MessagingRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private val TAG = "MyFirebaseMessagingService"
    }

    @Inject
    @Named("Repo")
    lateinit var messagingRepo: MessagingRepository

    @Inject
    lateinit var authUseCases: AuthUseCases

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        authUseCases.getUserAuth()
            .flatMapCompletable { authEntity ->
                messagingRepo.getToken()
                    .flatMapCompletable { token ->
                        messagingRepo.updateDeviceToken(authEntity.id, token)
                    }
            }
            .doOnComplete { Log.d(TAG, "updated device token ") }
            .doOnError { e -> Log.d(TAG, "unable to update token: ${e.localizedMessage}")}
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val data = p0.data
        val notification = p0.notification
        Log.d(TAG, data.toString())
        Log.d(TAG, "notification: ${notification?.body}")
    }
}