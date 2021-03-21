package com.csci310_group29.trojancheckincheckout.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.MutableBuilding
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class BuildingInfoActivity : AppCompatActivity() {


    @Inject
    lateinit var buildingDomain: BuildingUseCases

    lateinit var rv: RecyclerView
    var map: HashMap<String, Int> = HashMap<String,Int>()
    lateinit var adapter: BuildingListAdapter
    lateinit var buildingInfo: MutableList<MutableBuilding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)


        rv = findViewById<View>(R.id.buildingInfo) as RecyclerView

        try {
            val observable = buildingDomain.getAllBuildings()
            observable.subscribe(object : SingleObserver<List<Building>> {
                override fun onSuccess(t: List<Building>) {
                    buildingInfo = initializeList(t)
                    initializeHashmap(t)
                    adapter = BuildingListAdapter(buildingInfo)
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


    fun initializeHashmap(list: List<Building>) {
        var i = 0
        for(b in list) {
            map.put(b.id,i)
            i++
        }
    }


    fun initializeList(list: List<Building>): MutableList<MutableBuilding> {
        val res: MutableList<MutableBuilding> = mutableListOf<MutableBuilding>()
        for(b in list) {
            res.add( MutableBuilding(b.id,b.buildingName,b.address,b.capacity, b.numPeople,
                b.qrCodeRef))

            val observable = buildingDomain.observeBuildingById(b.id)
            observable.subscribe(object: Observer<Building> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Building) {
                    val elem = buildingInfo.get(map.getValue(t.id))
                    elem.numPeople = t.numPeople
                    elem.capacity = t.capacity
                    adapter.notifyDataSetChanged()

                }

                override fun onError(e: Throwable) {

                }
            })
        }

        return res
    }

}