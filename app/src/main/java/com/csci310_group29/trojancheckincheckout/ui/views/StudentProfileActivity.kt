
package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
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
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_student_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class StudentProfileActivity : AppCompatActivity() {

    private val SELECT_PHOTO = 1

    @Inject
    lateinit var viewModel: StudentProfileViewModel

    private val TAG = "StudentProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            FirstName.text = newUser.firstName
            LastName.text = newUser.lastName
            Major.text = newUser.major
            StudentId.text = newUser.studentId

        }

        viewModel.currUser.observe(this, userObserver)
    }

    fun onDelete(view: View) {
        viewModel.deleteAccount()
        startActivity(Intent(this, AppHomeActivity::class.java))
        finishAffinity()
    }

    fun onViewHistory(view: View) {
        startActivity(Intent(this, StudentHistoryActivity::class.java))
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
                    SProfilePic.setImageBitmap(bitmap)
                    //viewModel.updateProfilePic(bitmap)
                } else {
                    Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*fun onUpdateProfilePicture(view: View) {
        val file = File(filesDir, "cameraPic")
        val uri = FileProvider.getUriForFile(this, "file_provider",file)


        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if(success) {
                Log.i(TAG,"got image")
                try {
                    viewModel.updateProfilePic(uri, this.contentResolver)
                } catch(e:Exception) {
                    val toast = Toast.makeText(this, "Unable to update profile picture. Try again", Toast.LENGTH_SHORT)
                    toast.show()
                }
            } else {
                Log.e(TAG, "unable to take picture")
                val toast = Toast.makeText(this, "Unable to checkout. Try again", Toast.LENGTH_SHORT)
                toast.show()
            }

        }

        takePicture.launch(uri)
    }*/
}
