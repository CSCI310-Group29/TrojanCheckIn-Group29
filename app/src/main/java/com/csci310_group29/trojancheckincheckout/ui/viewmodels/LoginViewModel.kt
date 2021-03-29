
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val authDomain: AuthRepository) : ViewModel() {
    private var user = null
    private val TAG = "LoginViewModel"

    public fun login(email: String, password: String): Single<User> {
        var dis: Disposable? = null
        if (email.isEmpty()) {
            Log.e(TAG, "no email in login in LoginViewModel")
            throw Exception("Must enter an email")
        } else if (password.isEmpty()) {
            Log.e(TAG, "no email in login in LoginViewModel")
            throw Exception("Must enter a password")
        }

        Log.i(TAG, email + password)

        return Single.create { emitter ->

            val observable = authDomain.login(email, password);

            observable.subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    Log.i(TAG, "started subscribe in viewmodel")
                    dis = d
                }

                override fun onSuccess(t: User) {
                    Log.i(TAG, "success login from domain in viewmodel")
                    Session.uid = t.id!!
                    Session.user = t
                    emitter.onSuccess(t)
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "error in viewmodel")
                    emitter.onError(e)
                    dis!!.dispose()
                }
            })
        }
    }

    fun log(msg: String) {
        Log.i(TAG, msg)
    }
}
