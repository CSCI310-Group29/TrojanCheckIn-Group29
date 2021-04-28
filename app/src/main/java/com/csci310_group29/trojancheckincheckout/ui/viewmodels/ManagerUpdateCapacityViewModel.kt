package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.net.Uri
import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.models.BuildingOperation
import com.csci310_group29.trojancheckincheckout.domain.models.Operation
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.opencsv.CSVReader
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
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
            observe.subscribe(object : CompletableObserver {
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

    fun removeWithUI(buildingId: String): Completable {
        return Completable.create { emitter ->
            //need arian's function
           // val observe = buildingDomain.updateSingleBuildingCapacity(buildingId)
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

    fun addWithUI(buildingId: String, newCapacity: Double): Completable {
        return Completable.create { emitter ->
            //need arian's function
            //val observe = buildingDomain.updateSingleBuildingCapacity(buildingId, newCapacity)
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

    /**
     * Pass List<BuildingOperation> to UseCase
     */
    fun updateWithCSV(uri: Uri): Completable {
        Log.i(TAG, "Curr directory: " + Paths.get("").toAbsolutePath().toString())
        Log.i(TAG, "Is absolute URI? " + uri.isAbsolute)
        Log.i(TAG, "Is relative URI? " + uri.isRelative)
        return Completable.create { emitter ->
//            var buildingMap: HashMap<String, Double> = HashMap()
            var opList: MutableList<BuildingOperation> = ArrayList()

            try {
//                Log.i(TAG, "Inside ViewModel. CSV URI: " + uri.getPath())
                val parsedUri = uri.getPath()!!.replace("/document/raw:", "")
                Log.i(TAG, "Inside ViewModel. Parsed URI: " + parsedUri)
                Log.i(TAG, "Successfully opened file")
//                csvReader().open(parsedUri) {
//                    readAllAsSequence().forEach { row ->
//                        val x: String = row[0]
//                        if(x != "U" && x != "A" && x != "R") {
//                            throw Exception("Invalid operation")
//                        }
//                        var o: Operation
//                        if(x == "U") o = Operation.UPDATE
//                        else if(x == "A") o = Operation.ADD
//                        else o = Operation.REMOVE
//
//                        val y: String = row[1]
//
//                        var op: BuildingOperation
//                        if(x != "R") {
//                            val z: Int = row[2].toInt()
//                            op = BuildingOperation(o, y, z)
//                        } else {
//                            op = BuildingOperation(o, y)
//                        }
//
////                        buildingMap.put(b, c)
//                        opList.add(op)
//                    }
//                }

                val reader = CSVReader(FileReader(parsedUri))
                val rows = reader.readAll()
                for (r in rows) {
                    val x: String = r[0]
                    if(x != "U" && x != "A" && x != "R") {
                        throw Exception("Invalid operation")
                    }
                    var o: Operation
                    if(x == "U") o = Operation.UPDATE
                    else if(x == "A") o = Operation.ADD
                    else o = Operation.REMOVE

                    val y: String = r[1]

                    var op: BuildingOperation
                    if(x != "R") {
                        val z: Int = r[2].toInt()
                        if(z < 1) throw Exception("Invalid operation")
                        op = BuildingOperation(o, y, z)
                    } else {
                        op = BuildingOperation(o, y)
                    }

                    opList.add(op)
                }

                // Debugging
//                Log.i(TAG, "Building List size: " + buildingMap.size)
//                for(b in buildingMap) {
//                    val msg = "  B: " + b.key + "\t C: " + b.value
//                    Log.i(TAG, msg)
//                }
                Log.i(TAG, "Operation List size: " + opList.size)
                for(o in opList) {
                    val msg = "  Op: " + o
                    Log.i(TAG, msg)
                }

                Log.i(TAG, "Successfully processed file")

            } catch (e: Exception) {
                Log.e(TAG, "Cannot read file")
                Log.e(TAG, "   Error: " + e.printStackTrace())
                emitter.onError(e)
            } finally {
                try {
//                    fileReader!!.close()
                    Log.i(TAG, "Successfully read and closed file")
                } catch (e: Exception) {
                    Log.e(TAG, "Cannot close file")
                    emitter.onError(e)
                }
            }

//            val observe = buildingDomain.updateMultipleBuildingCapacities(buildingMap)
            val observe = buildingDomain.processBuildingOperations(opList)

            observe.subscribe(object : CompletableObserver {
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

