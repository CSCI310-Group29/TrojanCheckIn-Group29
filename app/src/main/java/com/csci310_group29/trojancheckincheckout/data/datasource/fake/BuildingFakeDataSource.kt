package com.csci310_group29.trojancheckincheckout.data.datasource.fake

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity
import com.csci310_group29.trojancheckincheckout.domain.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BuildingFakeDataSource @Inject constructor(): BuildingRepository {

    override fun get(id: String): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun getByName(buildingName: String): Single<BuildingEntity> {
        TODO("Not yet implemented")
    }

    override fun incrementNumStudents(buildingId: String, incrementCount: Int): Single<BuildingEntity> {
        return Single.just(BuildingEntity(buildingId, "building", "mayflow dr", 10, 5, "qrCodeRef"))
    }

    override fun updateCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        TODO("Not yet implemented")
    }
}