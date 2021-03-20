
package com.csci310_group29.trojancheckincheckout.ui.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class RegisterViewModel @Inject constructor(private val authDomain: AuthUseCases): ViewModel() {
    private val TAG = "RegisterViewModel"


    public fun register(email:String, password:String,user: UserEntity) {
        var success:Boolean = true
        var dis: Disposable? = null
        Log.i(TAG, "register called successfully");
        if(user.firstName == "") {
            //Log.e(TAG, "No first name passed to registerViewModel")
            throw Exception("Must enter first name")
        } else if(user.lastName == "") {
            //Log.e(TAG, "No last name passed to registerViewModel")
            throw Exception("Must enter last name")
        } else if(user.studentId == "" && user.isStudent!!) {
            //Log.e(TAG, "No student id passed to registerViewModel")
            throw Exception("Must enter student id if you are a student")
        } else if(password.isEmpty()) {
            throw Exception("Must enter password")
        }

        val domain = getEmailDomain(email)
        if(domain != "usc.edu") {
            //Log.e(TAG, "Not usc email passed to registerViewModel")
            throw Exception("Must register with a usc email")
        }


        val observable = authDomain.signup(email,password,user);
        observable.subscribe(object: CompletableObserver {
            override fun onComplete() {
                dis!!.dispose()
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "vm subscribed")
                dis = d
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, e.localizedMessage)
                throw(e)
                dis!!.dispose()
            }
        })


    }

    private fun getEmailDomain(email: String): String {
        val index = email.indexOf('@')
        if(index > 0)  {
            return email.substring(index+1)
        }
        return ""
    }
}
