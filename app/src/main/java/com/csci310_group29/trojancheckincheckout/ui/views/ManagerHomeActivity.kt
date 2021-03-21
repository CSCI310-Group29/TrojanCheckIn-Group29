package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.databinding.ActivityManagerHomeBinding
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_manager_home.view.*
import kotlinx.android.synthetic.main.activity_student_home.*
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val TAG = "ManagerHomeActivity"


@AndroidEntryPoint
class ManagerHomeActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityManagerHomeBinding

    @Inject
    lateinit var viewModel: ManagerHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var building: ArrayList<Building>
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)

//        binding = ActivityManagerHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)

//        binding.checkBuilding

//        val products = listOf("Building", "car", "truck")
//        products.forEach{
//            d("buchi", "Product is: $it")
//            binding.buildingList.append("$it \n\n")
//        }





    }

    fun onGetBuildings(view: View) {
        startActivity(Intent(this, BuildingListAdapter::class.java))


//        try {
//           val rees = viewModel.getBuildingInformation()
//            Log.i(TAG, "$rees.")
//            binding.buildingList.apply {  }
//
//        } catch (e:Exception) {
//            Log.e(TAG, e.localizedMessage)
//            val toast = Toast.makeText(this, "Unable to get Building names", Toast.LENGTH_SHORT)
//            toast.show()
//        }


    }


    fun onViewProfile(view: View) {
        startActivity(Intent(this, ManagerProfileActivity::class.java))
    }

    fun onLogout(view: View)  {
        try {
            viewModel.logout()
            startActivity(Intent(this, AppHomeActivity::class.java))
            finishAffinity()
        } catch (e:Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to checkout. Try Again", Toast.LENGTH_SHORT)
            toast.show()
        }

    }


}