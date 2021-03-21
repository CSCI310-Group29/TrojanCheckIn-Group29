package com.csci310_group29.trojancheckincheckout.ui.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerUpdateCapacityViewModel
import kotlinx.android.synthetic.main.activity_manager_update_capacity.*
import javax.inject.Inject

private const val TAG = "ManagerUpdateCapacityActivity"

class ManagerUpdateCapacityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerUpdateCapacityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_update_capacity)

//        observeViewModel()
    }

//    private fun observeViewModel() {
//        val buildingObserver = Observer<Building> { newBuilding ->
//            BuildingCode.text = newBuilding.id
//            CurrentCapacity.text = newBuilding.numPeople
//            MaxCapacity.text = newBuilding.capacity
//        }
//
//        viewModel.currBuilding.observe(this, buildingObserver)
//    }

    fun onUpdateWithUI(view: View) {
        try {
            viewModel.updateWithUI()

        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to update capacity. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun onUpdateWithCSV(view: View) {
        try {
            viewModel.updateWithCSV()

        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to update capacity with file. Try again",Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}