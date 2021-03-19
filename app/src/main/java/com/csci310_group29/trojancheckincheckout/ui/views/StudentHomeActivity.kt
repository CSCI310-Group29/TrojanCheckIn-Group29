
package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentHomeViewModel
import java.io.File

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
        val file = File(filesDir, "cameraPic")
        val uri = FileProvider.getUriForFile(this, "file_provider",file)


        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if(success) {
                Log.i(TAG,"got image")
                viewModel.parseQR(applicationContext,uri)
            } else {
                Log.e(TAG, "unable to take picture")
                val toast = Toast.makeText(this, "Unable to checkout. Try again",Toast.LENGTH_SHORT)
                toast.show()
            }

        }

        takePicture.launch(uri)

    }



}
