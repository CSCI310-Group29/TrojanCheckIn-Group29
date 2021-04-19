package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerStudentProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_manager_student_profile.*
import javax.inject.Inject

private const val TAG = "ManagerStudentProfileActivity"

@AndroidEntryPoint
class ManagerStudentProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var userDomain: UserUseCases
    @Inject
    lateinit var viewModel: ManagerStudentProfileViewModel

    lateinit var pb: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_student_profile)

        pb = findViewById(R.id.MSPindeterminateBar)
        loadingStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        Log.i(TAG, "RECEIVED studentUID : " + intent.getStringExtra("studentUID").toString())
//        val currUser = userDomain.observeUserById(intent.getStringExtra("studentId").toString())
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
                    MSPcurrBuilding.text = "Not Checked In"
                }
            }
            MSPsProfilePic.setImageBitmap(toBitmap(newUser.profilePicture))
            loadingEnd()

        }

        viewModel.userInit(intent.getStringExtra("studentUID").toString())
        viewModel.currUser.observe(this, userObserver)
    }

    fun onViewHistory(view: View) {
        val i = Intent(this, StudentHistoryActivity::class.java)
        i.putExtra("studentUID", intent.getStringExtra("studentUID"))
//        startActivity(Intent(this, StudentProfileActivity::class.java))
        startActivity(i)
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