package com.csci310_group29.trojancheckincheckout.ui.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerUpdateCapacityViewModel
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_manager_update_capacity.*
import javax.inject.Inject



class ManagerUpdateCapacityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerUpdateCapacityViewModel

    private val TAG = "ManagerUpdateCapacityActivity"
    // Request code for selecting a PDF document.
    private val REQUEST_CSV = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_update_capacity)

        observeViewModel()
    }

    private fun observeViewModel() {
        val spinner = findViewById<View>(R.id.BuildingInput) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.buildings_array,
            android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


//        val buildingObserver = Observer<Building> { newBuilding ->
//            BuildingCode.text = newBuilding.id
//            CurrentCapacity.text = newBuilding.numPeople
//            MaxCapacity.text = newBuilding.capacity
//        }

//        viewModel.currBuilding.observe(this, buildingObserver)
        Log.i(TAG, "oncreate manager update capacity activity")
    }

    fun onUpdateWithUI(view: View) {
        val bSpinner = findViewById<Spinner>(R.id.BuildingInput)
        val buildingCode = bSpinner.selectedItem.toString()
        val newCap = findViewById<EditText>(R.id.NewCapacityInput)
        val newCapacity = Integer.parseInt(newCap.text.toString())
        // Check if fields are populated
//        if(NewCapacityInput.isEmpty || BuildingInput.isEmpty()) {
//            makeToast( "Need to enter Building and New Capacity")
//            // EXIT FUNCTION
//            startActivity(Intent(this, ManagerUpdateCapacityActivity.class)))
//        }

        val observable = viewModel.updateWithUI(buildingCode, newCapacity)
        observable.subscribe(
            object: CompletableObserver {
                override fun onComplete() {
                    makeToast("Successfully updated capacity")
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.localizedMessage)
                    makeToast("Unable to update capacity")
                }
            }
        )

//        try {
//            viewModel.updateWithUI()
//
//        } catch(e: Exception) {
//            makeToast("Unable to update capacity. Try again")
//        }
    }

    fun onUpdateWithCSV(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
        }

        try {
            startActivityForResult(Intent.createChooser(intent, "Open CSV"), REQUEST_CSV)
        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            makeToast("Invalid file")
        }

//        try {
//            viewModel.updateWithCSV()
//
//        } catch(e: Exception) {
//            Log.e(TAG, e.localizedMessage)
//            val toast = Toast.makeText(this, "Unable to update capacity with file. Try again",Toast.LENGTH_SHORT)
//            toast.show()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_CSV -> {
                if(resultCode == Activity.RESULT_OK) {
                    val csvUri = data!!.getDataString()
                    val observable = viewModel.updateWithCSV(csvUri!!)
                    observable.subscribe(
                        object: CompletableObserver {
                            override fun onComplete() {
                                makeToast("Successfully updated capacity")
                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onError(e: Throwable) {
                                Log.e(TAG, e.localizedMessage)
                                makeToast("Unable to update capacity")
                            }
                        }
                    )
                }
            }
        }
    }

    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}