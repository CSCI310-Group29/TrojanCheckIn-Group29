package com.csci310_group29.trojancheckincheckout.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    companion object {
        private val EMULATOR = true
        private val HOST = "10.0.2.2"
        private val DB_PORT = 8080
        private val AUTH_PORT = 9099
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        val db = Firebase.firestore
        db.firestoreSettings = settings
        if (EMULATOR) db.useEmulator(HOST, DB_PORT)
        return db;
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        val auth = Firebase.auth
        if (EMULATOR) auth.useEmulator(HOST, AUTH_PORT)
        return auth
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageInstance(): FirebaseStorage {
        val storage = FirebaseStorage.getInstance()
        return storage
    }

    @Provides
    @Singleton
    fun provideFirebaseMessagingInstance(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}