package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(private val userDomain: UserUseCases) {

    val TAG = "SplashScreenViewModel"
    fun isLoggedIn(): Single<User> {
        return Single.create{ emitter ->
            val observable = userDomain.getCurrentlyLoggedInUser(true)
            observable.subscribe(object: SingleObserver<User> {
                override fun onSuccess(t: User) {
                    Log.i(TAG, "user returned from domain")
                    Session.uid = t.id
                    Session.user = t
                    Session.checkedInBuilding = t.checkedInBuilding
                    emitter.onSuccess(t)
                }

                override fun onSubscribe(d: Disposable) {
                    Log.i(TAG, "view model subscribed")
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "user not logged in")
                    emitter.onError(e)
                }
            })

        }


    }
}