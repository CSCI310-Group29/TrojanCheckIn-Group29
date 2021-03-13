package com.csci310_group29.trojancheckincheckout.data.fake

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.data.models.Building
import com.csci310_group29.trojancheckincheckout.data.repo.BuildingRepository
import io.reactivex.Completable
import io.reactivex.Single

class BuildingFakeDataSource: BuildingRepository {

    override fun getBuildingInfo(buildingName: String): Single<Building> {
        return Single.create { emitter ->
            emitter.onSuccess(Building(
                buildingName = buildingName,
                address = "Sample address",
                capacity = 50,
                numPeople = 20
            ))
        }
    }

    override fun updateBuildingCapacities(buildingCapacities: HashMap<String, Int>): Completable {
        return Completable.create {emitter ->
            emitter.onComplete()
        }
    }

    override fun getQRCode(buildingName: String): Single<Bitmap> {
        return Single.create { emitter ->
            emitter.onError(Exception("Cant find QR code"))
        }
    }
}