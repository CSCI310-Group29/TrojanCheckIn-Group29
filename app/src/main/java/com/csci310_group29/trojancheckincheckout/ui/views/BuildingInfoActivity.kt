package com.csci310_group29.trojancheckincheckout.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.BuildingFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class BuildingInfoActivity : AppCompatActivity() {
    val buildingRepo = BuildingFirebaseDataSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)


        val rv = findViewById<View>(R.id.buildingInfo) as RecyclerView
//
        val buildingInfo: List<BuildingEntity>
        try {
            val observable = buildingRepo.getAll()
            observable.subscribe(object : SingleObserver<List<BuildingEntity>> {
                override fun onSuccess(t: List<BuildingEntity>) {
                    val buildings = t;
                    val adapter = BuildingListAdapter(buildings)
                    rv.adapter = adapter
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    throw Exception("unable to get visit history")
                }
            })
        } catch(e: Exception) {
            val toast = Toast.makeText(this, "unable to get building info", Toast.LENGTH_SHORT)
            toast.show()
        }
//
        rv.layoutManager = LinearLayoutManager(this)
    }
}