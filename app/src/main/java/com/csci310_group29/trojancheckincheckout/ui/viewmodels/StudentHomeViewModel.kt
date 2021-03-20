
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class StudentHomeViewModel @Inject constructor(private val authDomain: AuthUseCases,
                                               private val visitDomain: VisitUseCases):ViewModel() {

    private val TAG = "LoginActivity"
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


    fun decodeQR(bitmap: Bitmap) {
       /* val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context,uri)
        } catch(e: Exception) {
            Log.e(TAG,e.localizedMessage)
            throw Exception("unable to get picture")
        }

        val scanner = BarcodeScanning.getClient()
        val result = scanner.process(image).addOnSuccessListener {barcodes ->
            for(barcode in barcodes) {
                val rawValue = barcode.rawValue;
                if(currUser.value!!.isCheckedIn != null) {
                    visitRepo.attemptCheckIn(currUser.value!!.id!!, rawValue)
                } else {
                    visitRepo.checkOut(Session.uid!!)
                }
            }
        }.addOnFailureListener {
            throw Exception("could not decode QR code")
        }*/
    }

    fun checkOutManual() {
        val observable = visitDomain.checkOut()
        observable.subscribe(object: SingleObserver<Visit> {
            override fun onSuccess(t: Visit) {
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

