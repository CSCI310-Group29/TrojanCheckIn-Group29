package com.csci310_group29.trojancheckincheckout.domain.query

data class UserQuery(
    val firstName: String?,
    val lastName: String?,
    val major: String?,
    val studentId: String?,
    @JvmField
    val isCheckedIn: Boolean?,
    val isStudent: Boolean?
)