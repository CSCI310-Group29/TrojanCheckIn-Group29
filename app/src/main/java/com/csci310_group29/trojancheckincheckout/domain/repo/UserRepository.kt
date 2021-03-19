package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {

    fun getUser(userId: String): Single<UserEntity>

    fun setCheckedIn(userId: String, checkIn: Boolean): Single<UserEntity>
    /*
    Given either a firstName, lastName, major, studentId, or a combination of the previous,
    updates the profile of the user

    Params

    NOTE: if any params are null, they will not be updated

        firstName: String specifying firstName of user
        lastName: String specifying lastName of user
        major: String specifying major of user
        studentId: String specifying the USC studentID of user

    Returns
        Completable that emits completion if the user profile was successfully updated,
        or an error if profile could not be updated
     */
    fun updateProfile(firstName: String? = null,
                      lastName: String? = null,
                      major: String? = null,
                      studentId: String? = null): Completable

    fun updatePhotoURL(url: String): Completable

    /*
    Given the profile picture of the user, updates the profile picture of the user to the new
    profile picture

    Params:
        profilePicture: Bitmap specifying profile picture of user

    Returns:
        Completable that emits completion when the profile picture was successfully updated,
        or an error if not
     */
    fun updateProfilePicture(profilePicture: Bitmap): Completable

    /*
    Given a building name and a user object as a query, returns the list of checkedInusers for that
    specific building, or all checked in users if the building name is null

    Params
        buildingName: String specifying the name of the building. If null, all checkedIn users at
                        any building will be returned
        user: User object specifying the query fields for users. If any fields are null, those fields
                will not be queried
     */
    fun queryCheckedInUsers(buildingName: String? = null, user: User): Single<List<User>>
}