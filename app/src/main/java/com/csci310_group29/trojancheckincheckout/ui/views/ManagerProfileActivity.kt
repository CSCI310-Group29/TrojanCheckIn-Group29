package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_manager_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class ManagerProfileActivity : AppCompatActivity() {

    val TAG = "ManagerProfileActivity"
    lateinit var pb: ProgressBar

    @Inject
    lateinit var viewModel: ManagerProfileViewModel

    private val SELECT_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_profile)
        pb = findViewById(R.id.indeterminateBar)

        observeViewModel()
    }

    fun observeViewModel() {
        val userObserver = Observer<User> { newUser->
            MFirst.text = newUser.firstName
            MLast.text = newUser.lastName
            val bitmap = toBitmap(newUser.profilePicture)
            MProfilePic.setImageBitmap(bitmap)
            loadingEnd()

        }

        viewModel.currUser.observe(this, userObserver)


    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }

    fun onUpdateProfilePic(view: View) {
        try {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            i.setType("image/*");
            Log.i(TAG, " image pick activity start")
            startActivityForResult(i, SELECT_PHOTO)
        } catch(e: java.lang.Exception) {
            Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "image pick activity returned")
        when(requestCode) {
            SELECT_PHOTO -> {
                if(resultCode == RESULT_OK) {
                    val uri = data!!.data!!
                    val stream = applicationContext.contentResolver.openInputStream(data!!.data!!)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    loadingStart()
                    viewModel.updateProfilePic(bitmap)
                } else {
                    Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onDeleteAccount(view: View) {
        val yn = arrayOf("Yes", "No")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to delete your account?")
        builder.setItems(yn) { dialog, which ->
            when(which) {
                0 -> {
                    val observable = viewModel.deleteAccount()
                    observable.subscribe(
                        object: CompletableObserver {
                            override fun onComplete() {
                                goToStarter();
                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onError(e: Throwable) {
                                makeToast("Unable to delete Account")
                            }
                        }

                    )

                }
                1-> {
                    dialog!!.cancel()
                }
            }
        }
        builder.show()

    }

    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    fun goToStarter() {
        startActivity(Intent(this, AppHomeActivity::class.java))
        finishAffinity()
    }

    fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        try {
            val bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.size)
            if(bitmap == null) Log.i(TAG, "cannot decode byte array")
            return bitmap
        } catch(e: java.lang.Exception) {
            Log.i(TAG, e.localizedMessage)
        }
        return null
    }




}