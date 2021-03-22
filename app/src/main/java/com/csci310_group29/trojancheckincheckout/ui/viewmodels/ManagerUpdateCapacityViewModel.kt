package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.net.Uri
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
import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import javax.inject.Inject

class ManagerUpdateCapacityViewModel @Inject constructor(private val buildingDomain: BuildingUseCases) {

    private val TAG = "ManagerUpdateCapacityViewModel"
    private val colRef = Firebase.firestore.collection("buildings")

//    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)
//    var currBuilding: MutableLiveData<Building> = MutableLiveData<Building>(currUser.)

    fun updateWithUI(buildingId: String, newCapacity: Double): Completable {
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

    fun updateWithCSV(uri: Uri): Completable {
        Log.i(TAG, "Curr directory: " + Paths.get("").toAbsolutePath().toString())
        return Completable.create { emitter ->
            var buildingMap: HashMap<String, Double> = HashMap()
            var buildings: List<BuildingUpdate> = emptyList()
            var fileReader: BufferedReader? = null
            var csvToBean: CsvToBean<BuildingUpdate>

            try {
                Log.i(TAG, "Inside ViewModel. CSV URI: " + uri)
                val parsedUri = uri.getPath()!!.replace(".csv","")
                Log.i(TAG, "Inside ViewModel. Parsed URI: " + parsedUri)
                fileReader = BufferedReader(FileReader(parsedUri))
//                fileReader = BufferedReader(FileReader(uri.getPath()))
                Log.i(TAG, "Successfully opened file")
                csvToBean = CsvToBeanBuilder<BuildingUpdate>(fileReader)
                    .withType(BuildingUpdate::class.java)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()

//                val buildings = csvToBean.parse()
//                val csvcontents = csvReader().readAll(uri)
//                val buildings = grass<BuildingUpdate>().harvest(csvcontents)
                val buildings = csvToBean.parse()
                Log.i(TAG, "Successfully processed file")

            } catch(e: Exception) {
                Log.e(TAG,"Cannot read file")
                emitter.onError(e)
            } finally {
                try {
                    fileReader!!.close()
                    Log.i(TAG, "Successfully read and closed file")
                } catch(e: Exception) {
                    Log.e(TAG, "Cannot close file")
                    emitter.onError(e)
                }
            }

            for (b in buildings) {
                buildingMap.put(b.getName(), b.getCap())
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