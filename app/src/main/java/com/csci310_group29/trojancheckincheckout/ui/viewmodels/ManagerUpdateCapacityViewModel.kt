package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.BuildingUpdate
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vhl.blackmo.grass.dsl.grass
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import java.io.BufferedReader
import java.io.FileReader
import javax.inject.Inject

class ManagerUpdateCapacityViewModel @Inject constructor(private val buildingDomain: BuildingUseCases) {

    private val TAG = "ManagerUpdateCapacityViewModel"
    private val colRef = Firebase.firestore.collection("buildings")

//    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)
//    var currBuilding: MutableLiveData<Building> = MutableLiveData<Building>(currUser.)

    fun updateWithUI(buildingId: String, newCapacity: Int): Completable {
        return Completable.create { emitter ->
            val observe = buildingDomain.updateSingleBuildingCapacity(buildingId, newCapacity)
            observe.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    emitter.onComplete()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    emitter.onError(e)
                }
            })
        }

    }

    fun updateWithCSV(uri: String): Completable {
        return Completable.create { emitter ->
            var buildingMap: HashMap<String, Int> = HashMap()
            var fileReader: BufferedReader? = null
            var csvToBean: CsvToBean<BuildingUpdate>

            try {
                fileReader = BufferedReader(FileReader(uri))
                csvToBean = CsvToBeanBuilder<BuildingUpdate>(fileReader)
                    .withType(BuildingUpdate::class.java)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()

//                val buildings = csvToBean.parse()
//                val csvcontents = csvReader().readAll(uri)
//                val buildings = grass<BuildingUpdate>().harvest(csvcontents)
//                for (b in buildings) {
//                    buildingMap.put(b.getName(), b.getCap())
//                }

            } catch(e: Exception) {
                Log.e(TAG,"Cannot read file")
                emitter.onError(e)
            } finally {
                try {
                    fileReader!!.close()
                } catch(e: Exception) {
                    Log.e(TAG, "Cannot close file")
                    emitter.onError(e)
                }
            }


            val observe = buildingDomain.updateMultipleBuildingCapacities(buildingMap)

            observe.subscribe(object: CompletableObserver {
                override fun onComplete() {
                    emitter.onComplete()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    emitter.onError(e)
                }
            })


        }

    }

}