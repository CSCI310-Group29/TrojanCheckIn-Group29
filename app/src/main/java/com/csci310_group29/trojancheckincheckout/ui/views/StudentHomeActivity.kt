
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
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_student_home.*
import java.util.jar.Attributes
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
        val userObserver = androidx.lifecycle.Observer<User> { newUser ->
            Log.i(TAG, "in Student Home")
            Name.text = newUser.firstName
            if(newUser.checkedInBuilding == null) {
                currBuilding.text = "None"
            } else {
                currBuilding.text = newUser.checkedInBuilding!!.buildingName
            }

        }

        viewModel.currUser.observe(this, userObserver)
    }


    fun onViewProfile(view: View) {
        startActivity(Intent(this, StudentProfileActivity::class.java))
    }

    fun onLogout(view: View) {
        val observable = viewModel.logout()
        observable.subscribe(object: CompletableObserver {
            override fun onComplete() {
                startActivity(Intent(this@StudentHomeActivity, AppHomeActivity::class.java))
                finishAffinity()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                val toast = Toast.makeText(this@StudentHomeActivity, "Unable to logout. Try again",Toast.LENGTH_SHORT)
                toast.show()
            }
        })

    }

    fun onManualCheckout(view: View) {
        Log.i(TAG, viewModel.currUser.value!!.checkedInBuilding.toString())
        if (viewModel.currUser.value!!.checkedInBuilding != null) {
            val yn = arrayOf("Yes", "No")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Are you sure you want to check out?")
            builder.setItems(yn) { dialog, which ->
                when(which) {
                    0 -> {
                        val observable = viewModel.checkOutManual()
                        observable.subscribe(
                            object: SingleObserver<Visit> {
                                override fun onSuccess(t: Visit) {
                                    makeToast("Checked out of ${t.building!!.buildingName}")
                                }

                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onError(e: Throwable) {
                                    Log.i(TAG,e.localizedMessage)
                                    if(viewModel.currUser.value!!.checkedInBuilding != null) {
                                        makeToast("Unable to Checkout. Try again")
                                    } else {
                                        makeToast("Must check into building before checking out")
                                    }
                                }
                            }
                        )
                    }
                    1-> {
                        dialog.cancel()
                    }
                }

            }
            builder.show()
        } else {
            makeToast("Must check into building before checking out")
        }

    }

    fun onScan(view: View) {

        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
        } catch(e: Exception) {
            makeToast("Unable to open camera")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if(resultCode == Activity.RESULT_OK) {
                    val imgBitmap = data!!.extras!!.get("data") as Bitmap
                    Log.i(TAG, "back in student home")
                    val yn = arrayOf("Yes", "No")
                    val builder = android.app.AlertDialog.Builder(this)
                    if (viewModel.currUser.value!!.checkedInBuilding == null) {
                        builder.setTitle("Are you sure you want to check in?")
                    } else {
                        builder.setTitle("Are you sure you want to check out?")
                    }
                    builder.setItems(yn) {dialog, which ->
                        when(which) {
                            0 -> {
                                Log.i(TAG,"yes checkin/out")
                                val observable = viewModel.decodeQR(imgBitmap)
                                observable.subscribe(object: SingleObserver<Visit> {
                                    override fun onSuccess(t: Visit) {
                                        if(viewModel.currUser.value!!.checkedInBuilding != null) {
                                            makeToast("Successfully checked into ${t.building!!.buildingName}")
                                        } else {
                                            makeToast("Successfully checked out of ${t.building!!.buildingName}")
                                        }
                                    }

                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onError(e: Throwable) {
                                        Log.i(TAG,"Error return check in/out activity")
                                        makeToast(e.localizedMessage)
                                    }
                                })
                            }
                            1 -> {
                                dialog!!.cancel()
                            }

                        }
                    }
                    builder.show()

                }
            }
        }
    }

    fun makeToast(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }



}
