package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ManagerProfileViewModel @Inject constructor(private val userDomain: UserUseCases, private val authDomain: AuthUseCases): ViewModel() {
    val TAG = "ManagerProfileViewModel"

    val currUser:MutableLiveData<User> = MutableLiveData<User>(Session.user)


    fun updateProfilePic(bitmap: Bitmap) {
        Log.i(TAG, "view model received bitmap")
        val byteArray = toByteArray(bitmap)
        try {
            val observable = userDomain.updateProfilePicture(byteArray!!)
            observable.subscribe(object: SingleObserver<User> {
                override fun onSuccess(t: User) {
                    Log.i(TAG, "successful upload")
                    //Log.i(TAG, Session.user!!.profilePicture.toString())
                    Session.user = t
                    //Log.i(TAG, Session.user!!.profilePicture.toString())
                    currUser.postValue(t)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, e.localizedMessage)
                }
            })
        } catch(e:Exception) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    fun deleteAccount() {
        val observe = authDomain.deleteAccount()
        observe.subscribe(object: CompletableObserver {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                throw Exception("problem with deleting user")
            }
        })
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return data

    }
}