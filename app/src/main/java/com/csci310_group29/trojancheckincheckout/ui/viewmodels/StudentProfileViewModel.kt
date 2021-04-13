package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class StudentProfileViewModel @Inject constructor(private val userDomain: UserUseCases,
                                                  private val authDomain: AuthUseCases) {
    private val TAG = "StudentProfileViewModel"
    private val colRef = Firebase.firestore.collection("users")
    private var listener: ListenerRegistration? = null


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


    /*fun getUserData(): MutableLiveData<User> {
        return object: MutableLiveData<User>() {
            init {
                currUser.setValue(Session.user)
            }
        }

        return object: MutableLiveData<UserEntity>() {
            val data = this
            val docRef = colRef.document(Session.uid)

            init {
                listener = docRef.addSnapshotListener {
                        snapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "could not get user data from firestore ")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val first = snapshot["firstName"] as String
                        val last = snapshot["lastName"] as String
                        val isStudent = snapshot["isStudent"] as Boolean
                        val major = snapshot["major"] as String
                        val sid = snapshot["studentId"] as String
                        val uid = snapshot.id

                        val newUser = UserEntity(uid, isStudent, first, last, major, null, sid)
                        currUser.setValue(newUser)
                    }
                }

            }
        }


    }*/

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

    fun updateProfilePicWithLink(link: String) {
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

    private fun toByteArray(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return data

    }


}