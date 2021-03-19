package com.csci310_group29.trojancheckincheckout.domain.models


// model used for interacting with

data class User(val id: String,
                @field:JvmField
                val isStudent: Boolean? = null,
                val email: String? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val major: String? = null,
                val isCheckedIn: Boolean = false,
                val studentId: String? = null,
                val profilePicture: ByteArray? = null): Comparable<User> {

    override fun compareTo(other: User): Int {
        if (this.id != other.id) return -1
        if (this.isStudent != other.isStudent) return -1
        if (this.email != other.email) return -1
        if (this.firstName != other.firstName) return -1
        if (this.lastName != other.lastName) return -1
        if (this.major != other.major) return -1
        if (this.isCheckedIn != other.isCheckedIn) return -1
        if (this.studentId != other.studentId) return -1
        if (!this.profilePicture.contentEquals(other.profilePicture)) return -1
        return 0
    }

}
