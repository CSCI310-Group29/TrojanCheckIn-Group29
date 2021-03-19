
package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentProfileViewModel
import kotlinx.android.synthetic.main.activity_student_profile.*
import java.io.File
import javax.inject.Inject

class StudentProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: StudentProfileViewModel

    private val TAG = "StudentProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<UserEntity> { newUser ->
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

    fun onUpdateProfilePicture(view: View) {
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
    }
}
