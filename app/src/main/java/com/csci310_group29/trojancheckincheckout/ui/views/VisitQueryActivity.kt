package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.VisitQueryAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_visit_query.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class VisitQueryActivity : AppCompatActivity() {

    val TAG = "VisitQueryActivity"

    private var startDate: Date? = null
    private var endDate: Date? = null

    @Inject
    lateinit var visitDomain: VisitUseCases
    @Inject
    lateinit var buildingDomain: BuildingUseCases

    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_query)

        rv = findViewById(R.id.queryRecyclerView)
        val spinner = findViewById<Spinner>(R.id.building_spinner) as Spinner

        val spinnerMajor = findViewById<Spinner>(R.id.major_spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.majors_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMajor.adapter = adapter


        val observable = buildingDomain.getAllBuildings()
        observable.subscribe(object: SingleObserver<List<Building>> {
            override fun onSuccess(t: List<Building>) {
                Log.i(TAG, "${t.size}")
                val list = getBuildingOptions(t)
                val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {

            }
        })

    }

    private fun getBuildingOptions(buildings: List<Building>): MutableList<Building> {
        val res = mutableListOf<Building>()
        res.add(Building("", "Building", "", 0, 0, ""))
        for(b in buildings) {
            res.add(b)
        }
        return res
    }

    fun onSearch(view:View) {
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {
        }

        val id = if(SearchId.text.toString().isBlank()) null else SearchId.text.toString()
        val building = if(building_spinner.selectedItem.toString() == "Building") null
        else building_spinner.selectedItem.toString()
        val major = if(major_spinner.selectedItem.toString().isEmpty()) null else major_spinner.selectedItem.toString()

        Log.i(TAG, "Id is null: " + id.isNullOrBlank().toString())
        Log.i(TAG, id.toString())
        Log.i(TAG, "building is null: " + building.isNullOrBlank().toString())
        Log.i(TAG, building.toString())
        Log.i(TAG, "major is null: " + major.isNullOrBlank().toString())
        Log.i(TAG, major.toString())
        if(startDate == null) {
            Log.i(TAG, "startdate is null")
        } else {
            Log.i(TAG, startDate.toString())
        }
        if(endDate == null) {
            Log.i(TAG, "enddate is null")
        } else {
            Log.i(TAG, endDate.toString())
        }


        val userQ = UserQuery(null, null,major, id,null,null)
        val visitQ = VisitQuery(null,null,null,null,building,null)

        val observable = visitDomain.searchVisits(userQ, visitQ)
        observable.subscribe(object: SingleObserver<List<Visit>> {
            override fun onSuccess(t: List<Visit>) {
                Log.i(TAG, "${t.size}")
                val adapter = VisitQueryAdapter(t)
                rv.adapter = adapter
                adapter.notifyDataSetChanged()
                if(t.size == 0)  makeToast("No results for this query")
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "subscribed query")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG,  "error query")
            }
        })
        rv.layoutManager = LinearLayoutManager(this)


    }

    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /*fun onClickStart(view: View) {
        if(startDate != null) {
            startDateView.text = "Start Date"
            startDateView.textSize =  24F
            startDate = null
        } else {
            dateTimePicker(true)
        }
    }

    fun onClickEnd(view: View) {
        if(endDate != null) {
            endDateView.text = "End Date"
            endDateView.textSize =  24F
            endDate = null
        } else {
            dateTimePicker(false)
        }
    }

    private fun dateTimePicker(isStart: Boolean) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this@VisitQueryActivity, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this@VisitQueryActivity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                if(isStart) {
                    startDate = pickedDateTime.time
                    Log.i(TAG, startDate.toString())
                    startDateView.text = startDate.toString()
                    startDateView.textSize =  16F
                } else {
                    endDate = pickedDateTime.time
                    Log.i(TAG,endDate.toString())
                    endDateView.text = endDate.toString()
                    endDateView.textSize =  16F
                }
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }*/
}