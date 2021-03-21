
package com.csci310_group29.trojancheckincheckout.ui.views

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_student_home.*
import javax.inject.Inject

private const val TAG = "StudentHomeActivity"

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: StudentHomeViewModel

    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        observeViewModel()

    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            Name.text = newUser.firstName

        }

        viewModel.currUser.observe(this, userObserver)
    }


    fun onViewProfile(view: View) {
        startActivity(Intent(this, StudentProfileActivity::class.java))
    }

    fun onLogout(view: View) {
        try {
            viewModel.logout()
            startActivity(Intent(this, AppHomeActivity::class.java))
            finishAffinity()
        } catch(e:Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to logout. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun onManualCheckout(view: View) {
        try {
            viewModel.checkOutManual();
        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to checkout. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun onScan(view: View) {

        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
        } catch(e: Exception) {
            Toast.makeText(this, "unable to open camera", Toast.LENGTH_SHORT).show()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if(resultCode == Activity.RESULT_OK) {
                    val imgBitmap = data!!.extras!!.get("data") as Bitmap
                    val observable = viewModel.decodeQR(imgBitmap)
                    observable.subscribe(object: SingleObserver<Visit> {
                        override fun onSuccess(t: Visit) {
                            makeToast("Successfully checked into ${t.building!!.buildingName}")
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            makeToast(e.localizedMessage)
                        }
                    })
                }
            }
        }
    }

    fun makeToast(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT)
    }



}
