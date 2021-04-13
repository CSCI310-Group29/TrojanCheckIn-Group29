package com.csci310_group29.trojancheckincheckout.domain.query

data class UserQuery(
    val firstName: String? = null,
    val lastName: String? = null,
    val major: String? = null,
    val studentId: String? = null,
    @JvmField
    var isCheckedIn: Boolean? = null,
    val isStudent: Boolean? = null
)