package com.csci310_group29.trojancheckincheckout.ui.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_qr_code.*
import javax.inject.Inject

@AndroidEntryPoint
class QrCodeActivity : AppCompatActivity() {
    val TAG = "QRCodeActivity"

    @Inject
    lateinit var buildingDomain:BuildingUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = intent.extras
        val id = b!!.getString("buildingName")
        setContentView(R.layout.activity_qr_code)

        bName.text = id
        val observable = buildingDomain.getQrCode(id!!)
        observable.subscribe(object: SingleObserver<ByteArray> {
            override fun onSuccess(t: ByteArray) {
                qrCode.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                qrCode.setImageBitmap(toBitmap(t))
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                makeToast("unable to get QR code")
            }
        })


    }

    fun makeToast(msg: String) {
        Toast.makeText(this, msg , Toast.LENGTH_SHORT).show()
    }

    private fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        Log.i(TAG,"${bArray.size}")
        return BitmapFactory.decodeByteArray(bArray,0, bArray.size)
    }
}