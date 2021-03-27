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

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    companion object {
        private val EMULATOR = false
    }

    @Provides
    fun provideFirebaseFirestoreInstance(): FirebaseFirestore {
        val db = Firebase.firestore
        return db;
    }

    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        val auth = Firebase.auth
        return auth
    }

    @Provides
    fun provideFirebaseStorageInstance(): FirebaseStorage {
        val storage = FirebaseStorage.getInstance()
        return storage
    }
}