package com.csci310_group29.trojancheckincheckout.viewmodels

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.data.fake.AuthFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

private const val TAG = "LoginViewModel"
class LoginViewModel: ViewModel() {
    private var authRepo = AuthRepoImpl(true)
    private var user = User()

    public fun login(email: String, password: String): User {
        var success: Boolean = true
        var dis: Disposable? = null
        if(email.isEmpty()) {
            //Log.e(TAG, "no email in login in LoginViewModel")
            throw Exception("Must enter an email")
        } else if(password.isEmpty()) {
            //Log.e(TAG, "no email in login in LoginViewModel")
            throw Exception("Must enter a password")
        }

        val observable = authRepo.getUserWithCredentials(email,password);

        observable.subscribe(object: SingleObserver<User> {
            override fun onSubscribe(d: Disposable) {
                dis= d
            }

            override fun onSuccess(t: User) {
                user = t
                dis!!.dispose()
            }

            override fun onError(e: Throwable) {
                success = false
                dis!!.dispose()
            }
        })

        if(!success) {
            //Log.e(TAG, "cannot login in login in LoginViewModel")
            throw Exception("Username or Password Incorrect")
        }
        return user;
    }
}