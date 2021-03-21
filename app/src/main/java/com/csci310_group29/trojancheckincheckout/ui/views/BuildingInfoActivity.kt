//package com.csci310_group29.trojancheckincheckout.ui.views
//
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.csci310_group29.trojancheckincheckout.R
//import com.csci310_group29.trojancheckincheckout.domain.models.Building
//import com.csci310_group29.trojancheckincheckout.domain.models.MutableBuilding
//import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
//import dagger.hilt.android.AndroidEntryPoint
//import io.reactivex.SingleObserver
//import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class BuildingInfoActivity : AppCompatActivity() {
//
//
//    @Inject
//    lateinit var buildingDomain: BuildingUseCases
//
//    lateinit var rv: RecyclerView
//    lateinit var map: HashMap<String, Int>
//    lateinit var adapter: BuildingListAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_building_info)
//
//
//        rv = findViewById<View>(R.id.buildingInfo) as RecyclerView
////
//        var buildingInfo: MutableList<MutableBuilding>
//
//        try {
//            val observable = buildingDomain.getAllBuildings()
//            observable.subscribe(object : SingleObserver<List<Building>> {
//                override fun onSuccess(t: List<Building>) {
//                    //buildingInfo = initializeList(t)
//                    //adapter = BuildingListAdapter(buildingInfo)
//                    //rv.adapter = adapter
//                }
//
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onError(e: Throwable) {
//                    throw Exception("unable to get visit history")
//                }
//            })
//
//        } catch(e: Exception) {
//            val toast = Toast.makeText(this, "unable to get building info", Toast.LENGTH_SHORT)
//            toast.show()
//        }
////
//        rv.layoutManager = LinearLayoutManager(this)
//    }
//
//    //doesnt work lol
//    /*fun initializeHashmap(list: List<Building>) {
//        for(b in list)
//    }*/
//    /*
//
//
//    fun initializeList(list: List<Building>): MutableList<MutableBuilding> {
//        val res: MutableList<MutableBuilding> = mutableListOf<MutableBuilding>()
//        for(b in list) {
//            res.add( MutableBuilding(b.id,b.buildingName,b.address,b.capacity, b.numPeople,
//                b.qrCodeRef))
//        }
//
//        return res
//    }*/
//
//}