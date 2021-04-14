package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.*
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ManagerProfileViewModel @Inject constructor(private val userDomain: UserUseCases, private val authDomain: AuthUseCases): ViewModel() {
    val TAG = "ManagerProfileViewModel"

    var currUser: MutableLiveData<User> = getUserData();

    private fun getUserData(): MutableLiveData<User> {

        return object: MutableLiveData<User>() {
            init {
                //val observable1 = userDomain.getCurrentlyLoggedInUser(true);


                val observable = userDomain.observeUserById(Session.uid, true);
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


    fun updateProfilePic(bitmap: Bitmap) {
        Log.i(TAG, "view model received bitmap")
        val byteArray = toByteArray(bitmap)
        try {
            val observable = userDomain.updateProfilePicture(byteArray!!)
            observable.subscribe(object: SingleObserver<User> {
                override fun onSuccess(t: User) {
                    Log.i(TAG, "successful upload")
                    //Log.i(TAG, Session.user!!.profilePicture.toString())
                    Session.user = t
                    //Log.i(TAG, Session.user!!.profilePicture.toString())
                    currUser.postValue(t)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, e.localizedMessage)
                }
            })
        } catch(e:Exception) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    fun deleteAccount(): Completable {
        return Completable.create { emitter ->
            val observe = authDomain.deleteAccount()
            observe.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    Session.uid = ""
                    Session.user = null
                    //check out of building before delete
                    Session.checkedInBuilding = null
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

    fun updateProfilePicWithLink(link: String): Single<User> {
        return Single.create { emitter ->
            Log.i(TAG, "view model received link")
//        val byteArray = toByteArray(bitmap)
            try {
                val observable = userDomain.updateProfilePictureByUrl(link)
                observable.subscribe(object: SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        Log.i(TAG, "successful upload by link")
                        //Log.i(TAG, Session.user!!.profilePicture.toString())
                        Session.user = t
                        //Log.i(TAG, Session.user!!.profilePicture.toString())
                        currUser.postValue(t)
                        emitter.onSuccess(t)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        emitter.onError(e)
                        Log.i(TAG, e.localizedMessage)
                    }
                })
            } catch(e:Exception) {
                Log.e(TAG, e.localizedMessage)
            }
        }

    }

    private fun toByteArray(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return data

    }
}