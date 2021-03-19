/*
package com.csci310_group29.trojancheckincheckout.viewmodels

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.data.fake.AuthFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import io.reactivex.CompletableObserver
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

            observable.subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    dis = d
                }

                override fun onComplete() {
                    val observable2 = userDomain.getCurrentlyLoggedInUser()
                    observable2.subscribe(object: CompletableObserver {
                        override fun onComplete() {
                            emitter.onSuccess
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            emitter.onError()
                        }
                    })
                }

                override fun onError(e: Throwable) {
                    emitter.onError(Exception("Failed to login"))
                    dis!!.dispose()
                }
            })
        }
    }
}*/
