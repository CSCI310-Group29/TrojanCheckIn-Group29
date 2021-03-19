package com.csci310_group29.trojancheckincheckout.domain.models

import java.util.*

data class Visit(val user: User? = null,
                 val building: Building? = null,
                 val checkIn: Date? = null,
                 val checkOut: Date? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Visit

        if (user != other.user) return false
        if (building != other.building) return false
        if (checkIn != other.checkIn) return false
        if (checkOut != other.checkOut) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user?.hashCode() ?: 0
        result = 31 * result + (building?.hashCode() ?: 0)
        result = 31 * result + (checkIn?.hashCode() ?: 0)
        result = 31 * result + (checkOut?.hashCode() ?: 0)
        return result
    }
}
