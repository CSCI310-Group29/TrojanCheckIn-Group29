package com.csci310_group29.trojancheckincheckout.viewmodels

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class StudentProfileViewModel @Inject constructor(private val userDomain: UserUseCases,
                                                  private val authDomain: AuthUseCases) {
    private val TAG = "StudentProfileViewModel"
    private val colRef = Firebase.firestore.collection("users")
    private var listener: ListenerRegistration? = null


    val currUser: MutableLiveData<UserEntity> = getUserData()


    fun getUserData(): MutableLiveData<UserEntity> {
        return object: MutableLiveData<UserEntity>() {
            val data = this
            val docRef = colRef.document(Session.uid)

            init {
                listener = docRef.addSnapshotListener {
                        snapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "could not get user data from firestore ")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val first = snapshot["firstName"] as String
                        val last = snapshot["lastName"] as String
                        val isStudent = snapshot["isStudent"] as Boolean
                        val major = snapshot["major"] as String
                        val sid = snapshot["studentId"] as String
                        val uid = snapshot.id

                        val newUser = UserEntity(uid, isStudent, first, last, major, null, sid)
                        currUser.setValue(newUser)
                    }
                }

            }
        }


    }

    fun deleteAccount() {
        var error = false
        val observe = authDomain.deleteAccount()
        observe.subscribe(object: CompletableObserver {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                error = true
            }
        })

        if(error) {
            throw Exception("problem with deleting user")
        }
    }



    fun updateProfilePic(uri: Uri, cr: ContentResolver) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(cr,uri)
            //userDomain.updateProfilePicture()
        }catch(e: Exception) {
            Log.e(TAG,"bitmap conversion failed or updateProfile repo failed")
        }
    }


}