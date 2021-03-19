package com.csci310_group29.trojancheckincheckout.viewmodels

//import androidx.lifecycle.ViewModel
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
//import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
//import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
//import dagger.hilt.android.AndroidEntryPoint
//import io.reactivex.SingleObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//private const val TAG = "LoginViewModel"
//
//class LoginViewModel @Inject constructor(private val userUseCases: UserUseCases,
//                                         private val authUseCases: AuthUseCases): ViewModel() {
//
//    public fun login(email: String, password: String): User {
//        TODO("Not yet implemented")
////        var success: Boolean = true
////        var dis: Disposable? = null
////        if(email.isEmpty()) {
////            //Log.e(TAG, "no email in login in LoginViewModel")
////            throw Exception("Must enter an email")
////        } else if(password.isEmpty()) {
////            //Log.e(TAG, "no email in login in LoginViewModel")
////            throw Exception("Must enter a password")
////        }
////
////        val observable = authRepo.getUserWithCredentials(email,password);
////
////        observable.subscribe(object: SingleObserver<User> {
////            override fun onSubscribe(d: Disposable) {
////                dis= d
////            }
////
////            override fun onSuccess(t: User) {
////                user = t
////                dis!!.dispose()
////            }
////
////            override fun onError(e: Throwable) {
////                success = false
////                dis!!.dispose()
////            }
////        })
////
////        if(!success) {
////            //Log.e(TAG, "cannot login in login in LoginViewModel")
////            throw Exception("Username or Password Incorrect")
////        }
////        return user;
//    }
//}