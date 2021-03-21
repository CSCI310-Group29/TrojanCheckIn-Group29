
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class StudentHomeViewModel @Inject constructor(private val authDomain: AuthUseCases,
                                               private val visitDomain: VisitUseCases):ViewModel() {

    private val TAG = "StudentHomeViewModel"
    private val colRef = Firebase.firestore.collection("users")
    private var listener: ListenerRegistration? = null

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)



    fun getUserData(): MutableLiveData<User> {
        return object: MutableLiveData<User>() {
            init {
                //observe changes in DB
                //currUser.setValue(newUser)
            }
        }
        /*
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
        }*/

    }


    fun decodeQR(bitmap: Bitmap): Single<Visit> {
        return Single.create{emitter ->
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            var image: InputImage? = null
            try {
                image = InputImage.fromBitmap(bitmap,0)
            } catch(e: Exception) {
                Log.e(TAG,e.localizedMessage)
                emitter.onError(Exception("Unable to get picture"))
            }

            val scanner = BarcodeScanning.getClient(options)
            scanner.process(image).addOnSuccessListener {barcodes ->
                for(barcode in barcodes) {
                    val rawValue = barcode.rawValue as String
                    Log.i(TAG, "raw value: $rawValue")
                    if(!(Session.isCheckedIn)) {
                        Log.i(TAG,"checking in")
                        attemptCheckInEmit(emitter,rawValue)
                    } else {
                        Log.i(TAG,"checking out")
                        attemptCheckOutEmit(emitter)
                    }
                }
            }.addOnFailureListener {
                emitter.onError(Exception("could not decode QR code"))
            }
        }

    }

    private fun attemptCheckOutEmit(emitter:SingleEmitter<Visit>) {
        val observable = visitDomain.checkOut()
        observable.subscribe(object: SingleObserver<Visit>{
            override fun onSuccess(t: Visit) {
                Log.i(TAG, "success domain check out")
                Session.isCheckedIn = false
                emitter.onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "view model subscribed checkout")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, e.localizedMessage)
                Log.i(TAG, "error domain check out")
                emitter.onError(Exception("Could not check out of the building"))
            }
        })
    }

    private fun attemptCheckInEmit(emitter: SingleEmitter<Visit>, buildingId: String) {
        val observable = visitDomain.attemptCheckIn(buildingId)
        observable.subscribe(object: SingleObserver<Visit>{
            override fun onSuccess(t: Visit) {
                Log.i(TAG, "success domain check in")
                /*val newUser = User(Session.uid, Session.user!!.isStudent!!, Session.user!!.email,
                    Session.user!!.firstName, Session.user!!.lastName, Session.user!!.major, !(Session.user!!.isCheckedIn!!),
                    Session.user!!.studentId, Session.user!!.profilePicture)*/
                Session.isCheckedIn = true
                emitter.onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "view model subscribed checkin")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, e.localizedMessage)
                Log.i(TAG, "error domain check in")
                emitter.onError(Exception("Could not check into the building"))
            }
        })
    }

    fun checkOutManual() {
        val observable = visitDomain.checkOut()
        observable.subscribe(object: SingleObserver<Visit> {
            override fun onSuccess(t: Visit) {
                Session.isCheckedIn = false
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                throw Exception("unable to check out")
            }
        })



    }

    fun logout() {
        lateinit var dis: Disposable
        val observable = authDomain.logout();
        observable.subscribe(object:CompletableObserver {
            override fun onComplete() {
                Session.uid = ""
                Session.user = null
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

