package com.csci310_group29.trojancheckincheckout.viewmodels

//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import com.csci310_group29.trojancheckincheckout.domain.models.Visit
//import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
//import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepoImpl
//import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
//import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
//import io.reactivex.CompletableObserver
//import io.reactivex.SingleObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//private const val TAG = "StudentHomeViewModel"
//
//class StudentHomeViewModel @Inject constructor(private val authUseCases: AuthUseCases,
//                                               private val visitUseCases: VisitUseCases): ViewModel() {
//
//    val authRepo = AuthRepoImpl(true)
//    val visitRepo = VisitRepoImpl()
//
//    val currUser: MutableLiveData<User> by lazy {
//        MutableLiveData<User>()
//    }
//
//    init {
//        val observer = authRepo.getCurrentUser()
//        var dis:Disposable? = null
//        observer.subscribe(object: SingleObserver<User>{
//            override fun onSuccess(t: User) {
//                currUser.setValue(t)
//                dis!!.dispose()
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                dis = d
//            }
//
//            override fun onError(e: Throwable) {
//                dis!!.dispose()
//            }
//        })
//        }
//
//
//
//    fun scan() {
//        TODO("Add scanning implementation")
//    }
//
//    fun checkOutManual() {
//        val observable = visitRepo.checkOut(currUser.value!!.id!!)
//        observable.subscribe(object: SingleObserver<Visit> {
//            override fun onSuccess(t: Visit) {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onError(e: Throwable) {
//                throw Exception("unable to check out")
//            }
//        })
//    }
//
//    fun logout() {
//        lateinit var dis: Disposable
//        val observable = authRepo.logoutCurrentUser();
//        observable.subscribe(object:CompletableObserver {
//            override fun onComplete() {
//                dis.dispose()
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                dis = d
//            }
//
//            override fun onError(e: Throwable) {
//                dis.dispose()
//                throw Exception(e.localizedMessage)
//            }
//        })
//
//    }
//}