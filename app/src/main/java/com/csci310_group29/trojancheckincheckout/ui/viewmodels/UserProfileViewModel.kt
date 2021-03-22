//
//package com.csci310_group29.trojancheckincheckout.viewmodels
//
//import android.graphics.Bitmap
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
//import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
//import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session
//import io.reactivex.CompletableObserver
//import io.reactivex.SingleObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//abstract class UserProfileViewModel @Inject constructor(private val userDomain: UserUseCases,
//                                                        private val authDomain: AuthUseCases): ViewModel() {
//
//    fun updateProfilePic(bitmap: Bitmap) {
//        Log.i(TAG, "view model received bitmap")
//        val byteArray = toByteArray(bitmap)
//        try {
//            val observable = userDomain.updateProfilePicture(byteArray!!)
//            observable.subscribe(object: SingleObserver<User> {
//                override fun onSuccess(t: User) {
//                    Log.i(TAG, "successful upload")
//                    //Log.i(TAG, Session.user!!.profilePicture.toString())
//                    Session.user = t
//                    //Log.i(TAG, Session.user!!.profilePicture.toString())
//                    currUser.postValue(t)
//                }
//
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.i(TAG, e.localizedMessage)
//                }
//            })
//        } catch(e:Exception) {
//            Log.e(TAG, e.localizedMessage)
//        }
//    }
//
//
//    /*fun resetPassword() {
//        var error = false
//        val observe = authDomain.resetPassword()
//        observe.subscribe(object:CompletableObserver {
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onError(e: Throwable) {
//                error = true
//            }
//        })
//
//        if(error) {
//            throw Exception("problem with resetting password")
//        }
//    }*/
//
//    fun updateProfilePicture(newPic:ByteArray) {
//        var error = false
//        val observe = userDomain.updateProfilePicture(newPic)
//        observe.subscribe(object:CompletableObserver {
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onError(e: Throwable) {
//                error = true
//            }
//        })
//
//        if(error) {
//            throw Exception("problem with updating profile picture")
//        }
//    }
//
//    fun deleteAccount() {
//        var error = false
//        val observe = authDomain.deleteAccount()
//        observe.subscribe(object:CompletableObserver {
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onError(e: Throwable) {
//                error = true
//            }
//        })
//
//        if(error) {
//            throw Exception("problem with deleting user")
//        }
//    }
//
//
//}
