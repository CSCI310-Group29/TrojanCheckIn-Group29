package com.csci310_group29.trojancheckincheckout.domain.entities

data class UserEntity(val id: String?,
                      @field:JvmField
                      val isStudent: Boolean? = null,
                      val firstName: String? = null,
                      val lastName: String? = null,
                      val major: String? = null,
                      val isCheckedIn: Boolean? = false,
                      val studentId: String? = null,
                      val photoUrl: String? = null) {
}
