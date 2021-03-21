
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
    lateinit var pb: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        pb = findViewById(R.id.indeterminateBar)
        loadingEnd()
        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            FirstName.text = newUser.firstName
            LastName.text = newUser.lastName
            Major.text = newUser.major
            StudentId.text = newUser.studentId
            SProfilePic.setImageBitmap(toBitmap(newUser.profilePicture))
            loadingEnd()

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
                    loadingStart()
                    viewModel.updateProfilePic(bitmap)
                } else {
                    Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }

    private fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArray,0, bArray.size)
    }

}
