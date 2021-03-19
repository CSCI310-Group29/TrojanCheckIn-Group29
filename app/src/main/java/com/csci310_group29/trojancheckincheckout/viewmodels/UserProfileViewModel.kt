/*
package com.csci310_group29.trojancheckincheckout.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepoImpl
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

abstract class UserProfileViewModel: ViewModel() {
    protected val authRepo: AuthRepoImpl = AuthRepoImpl()
    protected val userRepo: UserRepoImpl = UserRepoImpl()

    fun resetPassword() {
        var error = false
        val observe = authRepo.resetPassword()
        observe.subscribe(object:CompletableObserver {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                error = true
            }
        })

        if(error) {
            throw Exception("problem with resetting password")
        }
    }

    fun updateProfilePicture(newPic:Bitmap) {
        var error = false
        val observe = userRepo.updateProfilePicture(newPic)
        observe.subscribe(object:CompletableObserver {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                error = true
            }
        })

        if(error) {
            throw Exception("problem with updating profile picture")
        }
    }

    fun deleteAccount() {
        var error = false
        val observe = authRepo.deleteCurrentUser()
        observe.subscribe(object:CompletableObserver {
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


}*/
