
package com.csci310_group29.trojancheckincheckout.ui.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session.Companion.user
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject


class RegisterViewModel @Inject constructor(private val authDomain: AuthUseCases): ViewModel() {
    private val TAG = "RegisterViewModel"


    public fun register(email:String, password:String,user: UserEntity): Completable {
        return Completable.create {emitter->
            var success: Boolean = true
            var dis: Disposable? = null
            Log.i(TAG, "register called successfully");
            if (user.firstName == "") {
                Log.e(TAG, "No first name passed to registerViewModel")
                emitter.onError(Exception("Must enter first name"))
            } else if (user.lastName == "") {
                Log.e(TAG, "No last name passed to registerViewModel")
                emitter.onError(Exception("Must enter last name"))
            } else if (user.studentId!!.length != 10 && user.isStudent!!) {
                Log.e(TAG, "No student id passed to registerViewModel")
                emitter.onError(Exception("Must enter student id that is 10 digits if you are a student"))
            } else if (password.isEmpty()) {
                emitter.onError(Exception("Must enter password"))
            } else if (user.major == "Major" && user.isStudent!!) {
                emitter.onError(Exception("Must choose a major if you are a student"))
            }
            else if (getEmailDomain(email) != "usc.edu") {
                Log.e(TAG, "Not usc email passed to registerViewModel")
                emitter.onError(Exception("Must register with a usc email"))
            } else if(user.isStudent!! && !isNumber(user.studentId!!)) {
                emitter.onError(Exception("Student id must only contain digits"))
            }
            else {


                val observable = authDomain.signup(email, password, user);
                observable.subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        emitter.onComplete()
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

    private fun getEmailDomain(email: String): String {
        val index = email.indexOf('@')
        if(index > 0)  {
            return email.substring(index+1)
        }
        return ""
    }

    private fun isNumber(str: String): Boolean {
        try {
            str.toDouble();
        } catch(e: Exception) {
            //Log.i(TAG + "isNumber", e.localizedMessage)
            return false
        }
        return true
    }
}
