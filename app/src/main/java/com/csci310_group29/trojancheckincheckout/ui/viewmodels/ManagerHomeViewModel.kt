package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManagerHomeViewModel @Inject constructor(private val authDomain: AuthUseCases) {


    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)


    fun logout() {
        lateinit var dis: Disposable
        val observable = authDomain.logout();
        observable.subscribe(object: CompletableObserver {
            override fun onComplete() {
                dis.dispose()
            }

            override fun onSubscribe(d: Disposable) {
                dis = d
            }

            override fun onError(e: Throwable) {
                dis.dispose()
                throw Exception(e.localizedMessage)
            }
        })

    }

}