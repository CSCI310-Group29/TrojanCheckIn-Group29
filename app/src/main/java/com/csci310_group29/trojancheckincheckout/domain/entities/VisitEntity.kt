package com.csci310_group29.trojancheckincheckout.domain.entities

import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.google.firebase.firestore.DocumentId
import java.util.*

data class VisitEntity(@DocumentId
                       val id: String? = null,
                       val userId: String? = null,
                       val buildingId: String? = null,
                       val checkIn: Date? = null,
                       val checkOut: Date? = null)
