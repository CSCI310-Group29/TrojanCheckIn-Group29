package com.csci310_group29.trojancheckincheckout.data.models

import java.util.*

data class Visit(val user: User? = null,
                 val buildingName: String? = null,
                 val checkIn: Date? = null,
                 val checkOut: Date? = null)
