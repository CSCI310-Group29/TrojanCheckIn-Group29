package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import io.reactivex.Completable
import io.reactivex.Single

interface BuildingRepository {

    fun get(id: String): Single<BuildingEntity>

    fun getByName(buildingName: String): Single<BuildingEntity>

    fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity>

    fun updateCapacities(buildingCapacities: HashMap<String, Int>): Completable
}