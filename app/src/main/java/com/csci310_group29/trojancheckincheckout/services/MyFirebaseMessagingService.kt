package com.csci310_group29.trojancheckincheckout.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.repo.MessagingRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
import com.google.firebase.messaging.Constants.MessageNotificationKeys.COLOR
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

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

    private val CHANNEL_ID = "my_channel"

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

        val intent = Intent(this, StudentHomeActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(notification!!.title)
            .setContentText(notification!!.body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notif)

        Log.d(TAG, data.toString())
        Log.d(TAG, "notification: ${notification?.body}")
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "kickOutChannel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "channel for kick out"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}