package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManagerStudentProfileViewModel @Inject constructor(private val userDomain: UserUseCases): ViewModel() {

    private val TAG = "ManagerStudentProfileViewModel"

    lateinit var currUser : MutableLiveData<User>
//    var currUser: MutableLiveData<User> = getUserData()

    fun userInit(studentUID: String) {
        Log.i(TAG, "RECEIVED studentUID : " + studentUID)
        currUser = getUserData(studentUID)
    }

    private fun getUserData(sUID : String): MutableLiveData<User> {

        return object: MutableLiveData<User>() {
            init {
                val observable = userDomain.observeUserById(sUID, false);
                observable.subscribe(object: Observer<User> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: User) {
                        currUser.setValue(t);
                    }

                    override fun onError(e: Throwable) {

                    }
                })
            }
        }
    }
}