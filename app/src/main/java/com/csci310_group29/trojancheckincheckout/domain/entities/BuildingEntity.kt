package com.csci310_group29.trojancheckincheckout.domain.entities

import com.google.firebase.firestore.DocumentId

data class BuildingEntity(@DocumentId
                          val id: String? = null,
                          val buildingName: String? = null,
                          val address: String? = null,
                          val capacity: Int? = null,
                          val numPeople: Int? = null,
                          val qrCodeRef: String? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BuildingEntity

        if (id != other.id) return false
        if (buildingName != other.buildingName) return false
        if (address != other.address) return false
        if (capacity != other.capacity) return false
        if (numPeople != other.numPeople) return false
        if (qrCodeRef != other.qrCodeRef) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (buildingName?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (capacity ?: 0)
        result = 31 * result + (numPeople ?: 0)
        result = 31 * result + (qrCodeRef?.hashCode() ?: 0)
        return result
    }
}
