package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        //startActivity(Intent(this, BuildingInfoActivity::class.java))


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