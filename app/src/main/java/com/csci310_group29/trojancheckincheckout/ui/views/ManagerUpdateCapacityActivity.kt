package com.csci310_group29.trojancheckincheckout.ui.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerUpdateCapacityViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


@AndroidEntryPoint
class ManagerUpdateCapacityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerUpdateCapacityViewModel

    @Inject
    lateinit var buildingDomain: BuildingUseCases

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

        val observable = buildingDomain.getAllBuildings()
        observable.subscribe(object: SingleObserver<List<Building>> {
            override fun onSuccess(t: List<Building>) {
                val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,t)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {

            }
        })


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
        val newCapacity = newCap.text.toString().toDouble()
        // Check if fields are populated
//        if(NewCapacityInput.isEmpty || BuildingInput.isEmpty()) {
//            makeToast( "Need to enter Building and New Capacity")
//            // EXIT FUNCTION
//            startActivity(Intent(this, ManagerUpdateCapacityActivity.class)))
//        }
        val str = "Attempting to update Building " + buildingCode + " with Cap " + newCapacity
        Log.i(TAG, str)

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
                    makeToast("Unable to update capacity\n" + e.localizedMessage)
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

    /**
     * Update capacities with CSV
     * Commented out for assignment 3
     */
    fun onUpdateWithCSV(view: View) {
        val mimetypes = arrayOf("text/csv", "text/comma-separated-values", "application/csv")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        }

        try {
            startActivityForResult(Intent.createChooser(intent, "Open CSV"), REQUEST_CSV)
        } catch(e: Exception) {
            Log.e(TAG, e.localizedMessage)
            makeToast("Invalid file")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_CSV -> {
                if(resultCode == Activity.RESULT_OK) {
                    val csvUri = data!!.data!!
                    val observable = viewModel.updateWithCSV(csvUri)
                    Log.i(TAG, "CSV URI: " + csvUri)
                    observable.subscribe(
                        object: CompletableObserver {
                            override fun onComplete() {
                                makeToast("Successfully updated capacity")
                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onError(e: Throwable) {
                                Log.e(TAG, e.localizedMessage)
                                makeToast("Unable to update capacity\n" + e.localizedMessage)
                            }
                        }
                    )
                }
                startActivity(Intent(this, ManagerUpdateCapacityActivity::class.java))
            }
        }
    }

    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}