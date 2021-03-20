package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_student_home.*
import java.lang.Exception
import java.util.*
import javax.inject.Inject

private const val TAG = "ManagerHomeActivity"


@AndroidEntryPoint
class ManagerHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)

        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            Name.text = newUser.firstName
        }

        viewModel.currUser.observe(this, userObserver)
    }

    fun onViewProfile(view: View) {
        startActivity(Intent(this, ManagerProfileActivity::class.java))
    }

//    fun onLogout(view: View)  {
//        try {
//            viewModel.logout()
//            startActivity(Intent(this, AppHomeActivity::class.java))
//            finishAffinity()
//        } catch (e:Exception) {
//            Log.e(TAG, e.localizedMessage)
//            val toast = Toast.makeText(this, "Unable to checkout. Try Again", Toast.LENGTH_SHORT)
//            toast.show()
//        }
//
//    }


}