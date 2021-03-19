package com.csci310_group29.trojancheckincheckout.domain.entities

data class UserEntity(var id: String?,
                      @field:JvmField
                      var isStudent: Boolean? = null,
                      var firstName: String? = null,
                      var lastName: String? = null,
                      var major: String? = null,
                      var isCheckedIn: Boolean? = false,
                      var studentId: String? = null,
                      var photoUrl: String? = null) {
    fun merge(other: UserEntity) {
        if (other.id != null) this.id = other.id
        if (other.isStudent != null) this.isStudent = other.isStudent
        if (other.firstName != null) this.firstName = other.firstName
        if (other.lastName != null) this.lastName = other.lastName
        if (other.major != null) this.major = other.major
        if (other.isCheckedIn != null) this.isCheckedIn = other.isCheckedIn
        if (other.studentId != null) this.studentId = other.studentId
        if (other.photoUrl != null) this.photoUrl = other.photoUrl
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (isStudent != other.isStudent) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (major != other.major) return false
        if (isCheckedIn != other.isCheckedIn) return false
        if (studentId != other.studentId) return false
        if (photoUrl != other.photoUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (isStudent?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (major?.hashCode() ?: 0)
        result = 31 * result + (isCheckedIn?.hashCode() ?: 0)
        result = 31 * result + (studentId?.hashCode() ?: 0)
        result = 31 * result + (photoUrl?.hashCode() ?: 0)
        return result
    }

}
