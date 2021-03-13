package com.csci310_group29.trojancheckincheckout.data.models

import java.time.LocalDateTime

data class Visit(val user: User? = null,
                 val buildingName: String? = null,
                 val checkIn: LocalDateTime? = null,
                 val checkOut: LocalDateTime? = null)
