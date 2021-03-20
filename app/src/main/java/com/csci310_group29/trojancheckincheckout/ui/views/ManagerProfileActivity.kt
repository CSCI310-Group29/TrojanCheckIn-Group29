package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_manager_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class ManagerProfileActivity : AppCompatActivity() {

    val TAG = "ManagerProfileActivity"

    @Inject
    lateinit var viewModel: ManagerProfileViewModel

    private val SELECT_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_profile)

        observeViewModel()
    }

    fun observeViewModel() {
        val userObserver = Observer<User> { newUser->
            MFirst.text = newUser.firstName
            MLast.text = newUser.lastName
            MProfilePic.setImageBitmap(toBitmap(newUser.profilePicture))
        }

        viewModel.currUser.observe(this, userObserver)


    }

    fun onUpdateProfilePic(view: View) {
        try {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
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
                    //MProfilePic.setImageBitmap(bitmap)
                    viewModel.updateProfilePic(bitmap)
                } else {
                    Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onDeleteAccount(view: View) {
        try {
            viewModel.deleteAccount()
            startActivity(Intent(this, AppHomeActivity::class.java))
            finishAffinity()
        } catch(e: Exception){
            Toast.makeText(this, "Unable to delete Account", Toast.LENGTH_SHORT).show()
        }
    }

    fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArray,0, bArray.size)
    }




}