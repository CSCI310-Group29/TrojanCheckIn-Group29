package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_manager_student_profile.*
import javax.inject.Inject

private const val TAG = "ManagerStudentProfileActivity"

@AndroidEntryPoint
class ManagerStudentProfileActivity : AppCompatActivity() {

//    @Inject
//    lateinit var viewModel: ManagerStudentProfileViewModel

    lateinit var pb: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_student_profile)

        pb = findViewById(R.id.MSPindeterminateBar)
        loadingStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            Log.i(TAG, "in Manager Student Profile")
            MSPfirstName.text = newUser.firstName
            MSPlastName.text = newUser.lastName
            MSPmajor.text = newUser.major
            MSPstudentId.text = newUser.studentId
            if(newUser.deleted != null && newUser.deleted!!) {
                MSPcurrBuilding.text = "Deleted"
            } else {
                if (newUser.checkedInBuilding != null) {
                    MSPcurrBuilding.text = newUser.checkedInBuilding!!.buildingName
                } else {
                    MSPcurrBuilding.text = ""
                }
            }
            MSPsProfilePic.setImageBitmap(toBitmap(newUser.profilePicture))
            loadingEnd()

        }
    }

    fun onViewHistory(view: View) {
        startActivity(Intent(this, StudentHistoryActivity::class.java))
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