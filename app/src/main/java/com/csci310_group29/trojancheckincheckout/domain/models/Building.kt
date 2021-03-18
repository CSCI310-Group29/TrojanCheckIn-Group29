package com.csci310_group29.trojancheckincheckout.domain.models

import com.google.firebase.firestore.DocumentId

data class Building(@DocumentId
                    val id: String? = null,
                    val buildingName: String? = null,
                    val address: String? = null,
                    val capacity: Int? = null,
                    val numPeople: Int? = null)
