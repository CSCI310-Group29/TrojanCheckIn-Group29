package com.csci310_group29.trojancheckincheckout.domain.query

import java.util.*

data class VisitQuery(
    val startCheckIn: Date? = null,
    val endCheckIn: Date? = null,
    val startCheckOut: Date? = null,
    val endCheckOut: Date? = null,
    val buildingName: String? = null,
    val buildingId: String? = null
    )
