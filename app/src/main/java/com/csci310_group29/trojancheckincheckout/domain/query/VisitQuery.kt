package com.csci310_group29.trojancheckincheckout.domain.query

import java.util.*

data class VisitQuery(
    var startCheckIn: Date? = null,
    var endCheckIn: Date? = null,
    var buildingName: String? = null,
    var buildingId: String? = null
    )
