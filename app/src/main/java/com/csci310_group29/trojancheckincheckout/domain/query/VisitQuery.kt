package com.csci310_group29.trojancheckincheckout.domain.query

import java.util.*

data class VisitQuery(
    val startCheckIn: Date?,
    val endCheckIn: Date?,
    val buildingName: String?
    )
