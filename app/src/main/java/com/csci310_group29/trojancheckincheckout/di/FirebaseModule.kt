package com.csci310_group29.trojancheckincheckout.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        private val EMULATOR = false
        private val DB_PORT = 8080
        private val AUTH_PORT = 9099
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance(): FirebaseFirestore {
        val db = Firebase.firestore
        if (EMULATOR) db.useEmulator("10.0.2.2", DB_PORT)
        return db;
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        val auth = Firebase.auth
        if (EMULATOR) auth.useEmulator("10.0.2.2", AUTH_PORT)
        return auth
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageInstance(): FirebaseStorage {
        val storage = FirebaseStorage.getInstance()
        return storage
    }
}