package com.csci310_group29.trojancheckincheckout.domain.models

import com.google.firebase.firestore.DocumentId

data class Building(@DocumentId
                    val id: String,
                    val buildingName: String,
                    val address: String? = null,
                    val capacity: Int,
                    val numPeople: Int,
                    val qrCodeRef: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Building

        if (id != other.id) return false
        if (buildingName != other.buildingName) return false
        if (address != other.address) return false
        if (capacity != other.capacity) return false
        if (numPeople != other.numPeople) return false
        if (qrCodeRef != other.qrCodeRef) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + buildingName.hashCode()
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + capacity
        result = 31 * result + numPeople
        result = 31 * result + qrCodeRef.hashCode()
        return result
    }

}
