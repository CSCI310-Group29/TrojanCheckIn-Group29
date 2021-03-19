package com.csci310_group29.trojancheckincheckout.domain.entities

import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.google.firebase.firestore.DocumentId
import java.util.*

data class VisitEntity(@DocumentId
                 val id: String,
                       val userId: User,
                       val buildingId: String,
                       val checkIn: Date? = null,
                       val checkOut: Date? = null,
                       val photoUrl: String? = null)
