package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.nfc.Tag
import android.util.Log.d
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.security.ProtectionDomain
import javax.inject.Inject

class ManagerHomeViewModel @Inject constructor(private val authDomain: AuthUseCases,
                                                private val buildingDomain: BuildingUseCases):ViewModel() {

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)

    fun getBuildingInformation() {
        val observable = buildingDomain.getBuildingInfo("A")
        observable.subscribe(object: SingleObserver<Building> {

            override fun onSuccess(t: Building) {
                d("manHome", "$t")
                d("manHome1", "${t.buildingName}")
                d("manHome1", "${t.capacity}")
                d("manHome1", "${t.numPeople}")
            }

            override fun onSubscribe(d: Disposable) {
                d("manSub", "$d")
            }


            override fun onError(e: Throwable) {
                d("manErr", "$e")
            }
        })
    }



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