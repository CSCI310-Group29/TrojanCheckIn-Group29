package com.csci310_group29.trojancheckincheckout.ui.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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

    private var startDate: Date? = null
    private var endDate: Date? = null

    @Inject
    lateinit var visitDomain: VisitUseCases
    lateinit var buildingDomain: BuildingUseCases

    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_query)

        rv = findViewById(R.id.queryRecyclerView)
        val spinner = findViewById<Spinner>(R.id.building_spinner) as Spinner


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

    }

    fun onSearch(view:View) {
        val id = if(SearchId.text.isEmpty()) null else SearchId.text.toString()
        val building = if(building_spinner.selectedItem.toString().isEmpty()) null
        else building_spinner.selectedItem.toString()
        val major = if(major_spinner.selectedItem.toString().isEmpty()) null else major_spinner.selectedItem.toString()

        val userQ = UserQuery(null, null,major, id,null,null)
        val visitQ = VisitQuery(startDate,null,null,endDate,building,null)

        val observable = visitDomain.searchVisits(userQ, visitQ)
        observable.subscribe(object: SingleObserver<List<Visit>> {
            override fun onSuccess(t: List<Visit>) {
                val adapter = VisitQueryAdapter(t)
                rv.adapter = adapter
            }

            override fun onSubscribe(d: Disposable) {
                TODO("Not yet implemented")
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }
        })


    }

    fun onClickStart(view: View) {
        dateTimePicker(true)
    }

    fun onClickEnd(view: View) {
        dateTimePicker(false)
    }

    private fun dateTimePicker(isStart: Boolean) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(applicationContext, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(applicationContext, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                if(isStart) {
                    startDate = pickedDateTime.time
                } else {
                    endDate = pickedDateTime.time
                }
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }
}