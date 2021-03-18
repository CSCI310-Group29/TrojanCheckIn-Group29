package com.csci310_group29.trojancheckincheckout.data.entities

import com.google.firebase.firestore.DocumentId

data class BuildingEntity(@DocumentId
                          val id: String,
                          val buildingName: String? = null,
                          val address: String? = null,
                          val capacity: Int? = null,
                          val numPeople: Int? = null,
                          val qrCodeRef: String? = null)
