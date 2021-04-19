package com.csci310_group29.trojancheckincheckout.domain.query

import java.util.*

data class VisitQuery(
    var start: Date? = null,
    var end: Date? = null,
    var buildingName: String? = null,
    var buildingId: String? = null
    )
