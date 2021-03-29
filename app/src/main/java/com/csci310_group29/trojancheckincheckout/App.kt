package com.csci310_group29.trojancheckincheckout

import android.app.Application
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.*
import com.csci310_group29.trojancheckincheckout.di.FirebaseModule
import com.csci310_group29.trojancheckincheckout.domain.repo.*
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        private val EMULATOR = true
        private val HOST = "10.0.2.2"
        private val DB_PORT = 8080
        private val AUTH_PORT = 9099
    }

    private val db = getDb()
    private val auth = getAuth()
    private val storage = FirebaseStorage.getInstance()


    private fun getDb(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        val db = Firebase.firestore
        db.firestoreSettings = settings
        if (EMULATOR) db.useEmulator(HOST, DB_PORT)
        return db;
    }

    private fun getAuth(): FirebaseAuth {
        val auth = Firebase.auth
        if (EMULATOR) auth.useEmulator(HOST, AUTH_PORT)
        return auth
    }

    private fun getStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    fun retrieveDb(): FirebaseFirestore {
        return db
    }

    fun retrieveAuth(): FirebaseAuth {
        return auth
    }

    fun getBuildingRepo(): BuildingRepository {
        return BuildingFirebaseDataSource(db)
    }

    fun getUserRepo(): UserRepository {
        return UserFirebaseDataSource(db)
    }

    fun getAuthRepo(): AuthRepository {
        return AuthFirebaseDataSource(auth)
    }

    fun getPictureRepo(): PicturesRepository {
        return PictureFirebaseDataSource(storage)
    }

    fun getVisitRepo(): VisitRepository {
        return VisitFirebaseDataSource(db)
    }
}