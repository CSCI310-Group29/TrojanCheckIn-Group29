package com.csci310_group29.trojancheckincheckout.viewmodels
//import androidx.lifecycle.ViewModel
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
//import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
//import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
//import dagger.hilt.android.AndroidEntryPoint
//import io.reactivex.CompletableObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//private const val TAG = "RegisterViewModel"
//
//
//class RegisterViewModel @Inject constructor(private val authUseCases: AuthUseCases,
//                                            private val userUseCases: UserUseCases): ViewModel() {
//
//
//    public fun register(email:String, password:String,user: User) {
////        var success:Boolean = true
////        var dis: Disposable? = null
////        //Log.i(TAG, "register called successfully");
////        if(user.firstName == "") {
////            //Log.e(TAG, "No first name passed to registerViewModel")
////            throw Exception("Must enter first name")
////        } else if(user.lastName == "") {
////            //Log.e(TAG, "No last name passed to registerViewModel")
////            throw Exception("Must enter last name")
////        } else if(user.id == "" && !user.isStudent!!) {
////            //Log.e(TAG, "No student id passed to registerViewModel")
////            throw Exception("Must enter student id if you are a student")
////        } else if(password.isEmpty()) {
////            throw Exception("Must enter password")
////        }
////
////        val domain = getEmailDomain(email)
////        if(domain != "usc.edu") {
////            //Log.e(TAG, "Not usc email passed to registerViewModel")
////            throw Exception("Must register with a usc email")
////        }
////
////
////        val observable = authRepo.createUser(email,password,user);
////        observable.subscribe(object: CompletableObserver {
////            override fun onComplete() {
////                dis!!.dispose()
////            }
////
////            override fun onSubscribe(d: Disposable) {
////                dis = d
////            }
////
////            override fun onError(e: Throwable) {
////                success = false
////                dis!!.dispose()
////            }
////        })
////
////        if(!success) {
////            throw Exception("Unable to Register")
////        }
//    }
//
//    private fun getEmailDomain(email: String): String {
//        val index = email.indexOf('@')
//        if(index > 0)  {
//            return email.substring(index+1)
//        }
//        return ""
//    }
//}