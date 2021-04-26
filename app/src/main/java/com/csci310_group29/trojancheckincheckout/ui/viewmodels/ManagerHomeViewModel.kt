package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class ManagerHomeViewModel @Inject constructor(private val authDomain: AuthUseCases,
                                                private val buildingDomain: BuildingUseCases):ViewModel() {

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)

    companion object {
        private val TAG = "ManagerHomeViewModel"
    }


    fun logout(): Completable {
        Log.d(TAG, "trying to log out")
        return Completable.create { emitter ->
            val observable = authDomain.logout();
            observable.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    Log.d(TAG, "succesfully logged out")
                    emitter.onComplete()
                    Session.uid = ""
                    Session.user = null
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "error: ${e.localizedMessage}")
                    emitter.onError(e)
                }
            })
        }

    }

}