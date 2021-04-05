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
}