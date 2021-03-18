package com.csci310_group29.trojancheckincheckout.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.viewmodels.StudentHomeViewModel

private const val TAG = "StudentHomeActivity"

class StudentHomeActivity : AppCompatActivity() {

    private val viewModel: StudentHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

    }

    private fun observeViewModel() {
        TODO("add observers for views in ui")
    }
    
    fun onViewProfile() {
        TODO("Create intent to go to student profile")
        //val intent = Intent(this,)
    }

    fun onLogout(view: View) {
        try {
            viewModel.logout()
            startActivity(Intent(this,AppHomeActivity::class.java))
            finishAffinity()
        } catch(e:Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to logout. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun onManualCheckout() {
        try {
            viewModel.checkOutManual();
        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to checkout. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }


}