//
//package com.csci310_group29.trojancheckincheckout.viewmodels
//
//import androidx.lifecycle.ViewModel
//import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
//import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
//import io.reactivex.CompletableObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//abstract class UserProfileViewModel @Inject constructor(private val userDomain: UserUseCases,
//                                                        private val authDomain: AuthUseCases): ViewModel() {
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
