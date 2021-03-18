package com.csci310_group29.trojancheckincheckout.domain.models

import java.util.*

data class Visit(val user: User? = null,
                 val building: Building? = null,
                 val checkIn: Date? = null,
                 val checkOut: Date? = null)
