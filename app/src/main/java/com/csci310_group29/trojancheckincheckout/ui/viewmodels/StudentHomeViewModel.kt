
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.reactivex.*
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class StudentHomeViewModel @Inject constructor(private val authDomain: AuthUseCases,
                                               private val visitDomain: VisitUseCases):ViewModel() {

    private val TAG = "StudentHomeViewModel"

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)


    fun decodeQR(bitmap: Bitmap?): Single<Visit> {

        return Single.create{emitter ->

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            Log.i(TAG, "In studentHomeviewmodel");

            var image: InputImage? = null
            try {
                image = InputImage.fromBitmap(bitmap,0)
                val scanner = BarcodeScanning.getClient(options)
                scanner.process(image!!).addOnSuccessListener {barcodes ->
                    for(barcode in barcodes) {
                        val rawValue = barcode.rawValue as String
                        Log.i(TAG, "raw value: $rawValue")
                        if(Session.checkedInBuilding == null) {
                            Log.i(TAG,"checking in")
                            attemptCheckInEmit(emitter,rawValue)
                        } else {
                            Log.i(TAG,"checking out")
                            attemptCheckOutEmit(emitter, rawValue)
                        }
                    }
                }.addOnFailureListener {e ->
                    Log.i(TAG, e.localizedMessage)
                    emitter.onError(Exception("could not decode QR code"))
                }
            } catch(e: Exception) {
                Log.e(TAG,e.localizedMessage)
                emitter.onError(Exception("Unable to get picture"))
            }

        }

    }

    private fun attemptCheckOutEmit(emitter:SingleEmitter<Visit>, buildingId: String) {
        val observable = visitDomain.checkOut(buildingId)
        observable.subscribe(object: SingleObserver<Visit>{
            override fun onSuccess(t: Visit) {
                Log.i(TAG, "success domain check out")
                Session.checkedInBuilding = null
                emitter.onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "view model subscribed checkout")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, e.localizedMessage)
                Log.i(TAG, "error domain check out")
                val wrongBuilding = Exception("Check out before you can check into another building")
                if(Session.checkedInBuilding!!.id != buildingId) {
                    emitter.onError(wrongBuilding)
                } else {
                    emitter.onError(Exception("Could not check out of the building"))
                }
            }
        })
    }

    private fun attemptCheckInEmit(emitter: SingleEmitter<Visit>, buildingId: String) {
        val observable = visitDomain.attemptCheckIn(buildingId)
        observable.subscribe(object: SingleObserver<Visit>{
            override fun onSuccess(t: Visit) {
                Log.i(TAG, "success domain check in")
                Session.checkedInBuilding = t.building
                emitter.onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "view model subscribed checkin")
            }

            override fun onError(e: Throwable) {
                //Log.i(TAG, e.localizedMessage)
                Log.i(TAG, "error domain check in")
                emitter.onError(Exception("Could not check into the building"))
            }
        })
    }

    fun checkOutManual(): Single<Visit> {
        return Single.create{ emitter ->
            if(Session.checkedInBuilding == null) emitter.onError(Exception("Not Checked In"))
            else {
                val observable = visitDomain.checkOut(Session.checkedInBuilding!!.id)
                observable.subscribe(object : SingleObserver<Visit> {
                    override fun onSuccess(t: Visit) {
                        Session.checkedInBuilding = null
                        emitter.onSuccess(t)
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

    fun logout(): Completable {
        return Completable.create { emitter ->
            val observable = authDomain.logout();
            observable.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    Session.uid = ""
                    Session.user = null
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

