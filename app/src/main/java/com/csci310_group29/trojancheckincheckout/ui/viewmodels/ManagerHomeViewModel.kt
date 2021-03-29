package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManagerHomeViewModel @Inject constructor(private val authDomain: AuthRepository,
                                               private val buildingDomain: BuildingUseCases):ViewModel() {

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)


    fun logout(): Completable {
        return Completable.create { emitter ->
            val observable = authDomain.logout();
            observable.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    emitter.onComplete()
                    Session.uid = ""
                    Session.user = null
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    emitter.onError(e)
                }
            })
        }

    }

}