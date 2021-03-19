
package com.csci310_group29.trojancheckincheckout.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val authDomain: AuthUseCases, private val userDomain: UserUseCases) : ViewModel() {
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

        return Single.create { emitter ->

            val observable = authDomain.login(email, password);

            observable.subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    dis = d
                }

                override fun onSuccess(t: User) {
                    Session.uid = t.id!!
                    Session.user = t
                    emitter.onSuccess(t)
                }

                override fun onError(e: Throwable) {
                    emitter.onError(Exception("Failed to login"))
                    dis!!.dispose()
                }
            })
        }
    }
}
