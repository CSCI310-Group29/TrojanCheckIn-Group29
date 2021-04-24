package com.csci310_group29.trojancheckincheckout

import android.app.Application
import android.util.Log
import com.csci310_group29.trojancheckincheckout.data.retrofit.MessagingWebService
import com.csci310_group29.trojancheckincheckout.domain.repo.MessagingRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class App: Application() {

    companion object {
        private val TAG = "App"
    }

    @Inject
    lateinit var authUseCases: AuthUseCases

    @Inject
    @Named("Repo")
    lateinit var messagingRepo: MessagingRepository

    @Inject
    lateinit var messagingWebService: MessagingWebService

    override fun onCreate() {
        super.onCreate()
        checkToken().doOnComplete {
            Log.d(TAG, "success")
        }.doOnError { e ->
            Log.d(TAG, "error on updating device token: ${e.localizedMessage}")
        }
    }

    fun checkToken(): Completable {
        return authUseCases.getUserAuth()
            .flatMapCompletable { authEntity ->
                messagingRepo.getToken()
                    .flatMapCompletable { token ->
                        messagingRepo.updateDeviceToken(authEntity.id, token)
                    }
            }
    }
}