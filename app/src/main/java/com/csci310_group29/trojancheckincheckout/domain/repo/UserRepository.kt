package com.csci310_group29.trojancheckincheckout.domain.repo

import android.graphics.Bitmap
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface UserRepository {

    /*
    Gets a user based on its id

        Params:
            id: String representing the id of the user to be retrieved

        Returns:
            Single that emits a UserEntity if the user was found, or an error otherwise
     */
    fun get(id: String): Single<UserEntity>

    /*
    Observes all users in the specified building

        Params:
            id: String representing the id of the building
            limit: the limit of how many users to retrieve

        Returns:
            Observable that emits a list of UserEntities whenever the users in the building change,
            or an error otherwise
     */
    fun observeUsersInBuilding(buildingId: String): Observable<List<UserEntity>>

    fun observeUserById(userId: String): Observable<UserEntity>

    /*
    Updates a user based on the fields in userEntity. Whatever fields the values are in the object will be
        the field values in the user document in the database

        Params:
            userEntity: UserEntity object specifying the updated state of the user

        Returns:
            Completable that emits completion if the user was successfully updated, or an error
                otherwise
     */
    fun update(userEntity: UserEntity): Completable

    /*
    Deletes a user in the database using its id

        Params:
            id: String specifying the id of the user to be deleted

        Returns:
            Completable that emits completion if the user was successfully deleted, or an error
                otherwise
     */
    fun delete(id: String): Completable

    fun getAll(): Single<List<UserEntity>>

    fun addDeleteField(id: String): Completable

    /*
    Creates a user in the database using the fields in the UserEntity

        Params:
            userEntity: UserEntity object whose field values will be the exact same values in the
                database
        Returns:
            Single that emits the UserEntity whose fields match the values in the database
     */
    fun create(userEntity: UserEntity): Single<UserEntity>

    /*
    Sets the checked in building of a user

        Params:
            userId: String representing the user to be updated
            buildingId: Nullable String representing the id of the checked in building.

        Returns:
            Single that emits the updated UserEntity upon success, or an error otherwise
     */
    fun setCheckedInBuilding(userId: String, buildingId: String?): Single<UserEntity>

    /*
    Updates the profile picture url of the user

        Params:
            id: String representing the id the user whose url will be updated
            url: String representing the url of the photo

        Returns:
            Completable that emits completion if the url was successfully updated, or an error
                otherwise
     */
    fun updatePhotoUrl(id: String, url: String): Completable

    /*
    BUGGY: Needs revision
     */
    fun query(userQuery: UserQuery, visitQuery: VisitQuery): Observable<UserEntity>
}