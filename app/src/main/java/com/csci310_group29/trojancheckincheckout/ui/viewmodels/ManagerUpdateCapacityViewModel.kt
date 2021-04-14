package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.opencsv.bean.CsvBindByName
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

    /**
     * Update Capacity with CSV
     * commented out for assignment 3
     */
    fun updateWithCSV(uri: Uri): Completable {
        Log.i(TAG, "Curr directory: " + Paths.get("").toAbsolutePath().toString())
        Log.i(TAG, "Is absolute URI? " + uri.isAbsolute)
        Log.i(TAG, "Is relative URI? " + uri.isRelative)
        return Completable.create { emitter ->
            var buildingMap: HashMap<String, Double> = HashMap()

            try {
//                Log.i(TAG, "Inside ViewModel. CSV URI: " + uri.getPath())
                val parsedUri = uri.getPath()!!.replace("/document/raw:","")
                Log.i(TAG, "Inside ViewModel. Parsed URI: " + parsedUri)
                Log.i(TAG, "Successfully opened file")
                csvReader().open(parsedUri) {
                    readAllAsSequence().forEach { row ->
                        val b: String = row[0]
                        val c: Double = row[1].toDouble()

                        buildingMap.put(b, c)
                    }
                }


                // Debugging
                Log.i(TAG, "Building List size: " + buildingMap.size)
                for(b in buildingMap) {
                    val msg = "  B: " + b.key + "\t C: " + b.value
                    Log.i(TAG, msg)
                }

                Log.i(TAG, "Successfully processed file")

            } catch(e: Exception) {
                Log.e(TAG,"Cannot read file")
                Log.e(TAG, "   Error: " + e.printStackTrace())
                emitter.onError(e)
            } finally {
                try {
//                    fileReader!!.close()
                    Log.i(TAG, "Successfully read and closed file")
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
                    Log.i(TAG, "Building Domain returned error: ${e.localizedMessage}")
                    emitter.onError(e)
                }
            })
        }
    }
}

