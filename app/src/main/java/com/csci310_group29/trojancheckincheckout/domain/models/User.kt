package com.csci310_group29.trojancheckincheckout.domain.models


// model used for interacting with

data class User(val id: String,
                @field:JvmField
                val isStudent: Boolean? = null,
                val email: String? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val major: String? = null,
                var checkedInBuilding: Building? = null,
                val studentId: String? = null,
                var deleted: Boolean? = null,
                val profilePicture: ByteArray? = null): Comparable<User> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (isStudent != other.isStudent) return false
        if (email != other.email) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (major != other.major) return false
        if (checkedInBuilding != other.checkedInBuilding) return false
        if (studentId != other.studentId) return false
        if (deleted != other.deleted) return false
        if (profilePicture != null) {
            if (other.profilePicture == null) return false
            if (!profilePicture.contentEquals(other.profilePicture)) return false
        } else if (other.profilePicture != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (isStudent?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (major?.hashCode() ?: 0)
        result = 31 * result + (checkedInBuilding?.hashCode() ?: 0)
        result = 31 * result + (studentId?.hashCode() ?: 0)
        result = 31 * result + (deleted?.hashCode() ?: 0)
        result = 31 * result + (profilePicture?.contentHashCode() ?: 0)
        return result
    }

    override fun compareTo(other: User): Int {
        return if (this.lastName == null || other.lastName == null) 0
        else this.lastName.toLowerCase().compareTo(other.lastName.toLowerCase())
    }


}
