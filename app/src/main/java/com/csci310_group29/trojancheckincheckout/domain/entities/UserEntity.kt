package com.csci310_group29.trojancheckincheckout.domain.entities

data class UserEntity(var id: String? = null,
                      @field:JvmField
                      var isStudent: Boolean? = null,
                      var firstName: String? = null,
                      var lastName: String? = null,
                      var major: String? = null,
                      @field:JvmField
                      var checkedInBuildingId: String? = null,
                      var studentId: String? = null,
                      var deleted: Boolean? = null,
                      var photoUrl: String? = null): Comparable<UserEntity> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (isStudent != other.isStudent) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (major != other.major) return false
        if (checkedInBuildingId != other.checkedInBuildingId) return false
        if (studentId != other.studentId) return false
        if (deleted != other.deleted) return false
        if (photoUrl != other.photoUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (isStudent?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (major?.hashCode() ?: 0)
        result = 31 * result + (checkedInBuildingId?.hashCode() ?: 0)
        result = 31 * result + (studentId?.hashCode() ?: 0)
        result = 31 * result + (deleted?.hashCode() ?: 0)
        result = 31 * result + (photoUrl?.hashCode() ?: 0)
        return result
    }

    override fun compareTo(other: UserEntity): Int {
        if (this.lastName == null || other.lastName == null) {
            return 0;
        } else return this.lastName!!.toLowerCase().compareTo(other.lastName!!.toLowerCase())
    }


}
