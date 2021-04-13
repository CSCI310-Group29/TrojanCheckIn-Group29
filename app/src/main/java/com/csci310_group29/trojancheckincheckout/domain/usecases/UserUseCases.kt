package com.csci310_group29.trojancheckincheckout.domain.usecases

import android.graphics.Bitmap
import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.entities.AuthEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.entities.VisitEntity
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.PicturesRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.UserRepoImpl
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepoImpl
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.query.UserQuery
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.repo.AuthRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.PicturesRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.UserRepository
import com.csci310_group29.trojancheckincheckout.domain.repo.VisitRepository
import com.google.rpc.context.AttributeContext
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

open class UserUseCases @Inject constructor(
    @Named("Repo") private val authRepo: AuthRepository,
    @Named("Repo") private val userRepo: UserRepository,
    @Named("Repo") private val pictureRepo: PicturesRepository,
    @Named("Repo") private val visitRepo: VisitRepository,
    private val buildingUseCases: BuildingUseCases
    ) {

    companion object {
        private val TAG = "UserUseCases"
    }

    open fun getCurrentlyLoggedInUser(picture: Boolean = true): Single<User> {
        /*
        Gets the currently logged in user as a User object

            Params:
                picture: Boolean specifying whether the function should already get the profile
                    picture of the user

            Returns:
                Single that emits a User object if the user is logged in or an error otherwise.
         */
//        Log.d(TAG, "getting logged in user")
        return authRepo.getCurrentUser()
            .flatMap { authEntity ->
//                Log.d(TAG, "logged in user $authEntity")
                getUser(authEntity.id, authEntity, picture)
            }
    }

    open fun updateProfilePicture(picture: ByteArray): Single<User> {
        /*
        Updates the profile picture of the user

            Params:
                picture: ByteArray specifying the new profile picture

            Returns:
                Single that emits a User object with the profile picture if the profile picture
                    was successfully updated, or an error otherwise
         */
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable {user ->
                    if (user.photoUrl == null) {
                        user.photoUrl = "profilePictures/" + user.id + ".jpg"
                        userRepo.update(user).toSingleDefault(false)
                            .flatMapCompletable { pictureRepo.create(user.photoUrl!!, picture)}
                    } else {
                        pictureRepo.create(user.photoUrl!!, picture)
                    }
                }
            .toSingleDefault(false)
            .flatMap { getCurrentlyLoggedInUser()}
    }

    open fun updateProfilePictureByUrl(url: String): Single<User> {
        return pictureRepo.getFromExternalUrl(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { picture ->
                updateProfilePicture(picture)
            }
    }

    open fun updateProfile(userEntity: UserEntity): Single<User> {
        /*
        Updates the profile of the user.

            Params: userEntity: UserEntity object representing the fields to be updated. Any fields
                that are null will not be updated.
         */
        return authRepo.getCurrentUser()
                .flatMap { authEntity ->
                    userRepo.get(authEntity.id)
                }
                .flatMapCompletable { user ->
                    userRepo.update(overwrite(user, userEntity))
                }
            .toSingleDefault(false)
            .flatMap { getCurrentlyLoggedInUser() }
    }

    open fun observeUserById(userId: String, picture: Boolean = true): Observable<User> {
        /*
        Observes a user and emits new user objects on changes

            Params:
                userId: String representing Id of the user
                picture: Boolean that is true if we want to get the user's profile picture

            Returns:
                Observable that emits a new User on every change

         */
        return userRepo.observeUserById(userId)
            .flatMap { userEntity ->
                if (userEntity.checkedInBuildingId != null) {
                    buildingUseCases.getBuildingInfoById(userEntity.checkedInBuildingId!!).toObservable()
                        .flatMap { building ->
                            getPictureAndUser(picture, null, building, userEntity).toObservable()
                        }
                } else {
                    getPictureAndUser(picture, null, null, userEntity).toObservable()
                }
            }
    }

    open fun getUser(userId: String?, authEntity: AuthEntity? = null, picture: Boolean = true, userEntity: UserEntity? = null): Single<User> {
        /*
        Retrieves the user matching the userId

            Params:
                userId: String specifying the id of the user to be retrieved
                authEntity: AuthEntity object that is null if the user to be retrieved is not the
                    currently logged in user, and not null otherwise
                picture: Boolean specifying whether the profile picture should also be retrieved

            Returns:
                User object matching the userId
         */
        if (userId != null) {
            return userRepo.get(userId)
                .flatMap { userEntity2 ->
//                Log.d(TAG, "user exists: $userEntity")
                    if (userEntity2.checkedInBuildingId != null) {

                        buildingUseCases.getBuildingInfoById(userEntity2.checkedInBuildingId!!)
                            .flatMap { building ->
//                            Log.d(TAG, "getting user building: $building")
                                getPictureAndUser(picture, authEntity, building, userEntity2)
                            }
                    } else {
//                    Log.d(TAG, "getting user without building")
                        getPictureAndUser(picture, authEntity, null, userEntity2)
                    }
                }
        } else {
            if (userEntity?.checkedInBuildingId != null) {
                return buildingUseCases.getBuildingInfoById(userEntity.checkedInBuildingId!!)
                    .flatMap { building ->
//                            Log.d(TAG, "getting user building: $building")
                        getPictureAndUser(picture, authEntity, building, userEntity)
                    }
            } else {
//                    Log.d(TAG, "getting user without building")
                return getPictureAndUser(picture, authEntity, null, userEntity!!)
            }
        }
    }

    open fun searchUsers(userQuery: UserQuery, visitQuery: VisitQuery, picture: Boolean = true): Single<List<User>> {
        /*
        Queries a list of users based on their visits as well as their user attributes

            Params:
                userQuery: query object for user fields. If any fields of the UserQuery object
                    are null, those fields will not be queried for
                visitQuery:
                    query object for visit fields. If any fields of the UserQuery object
                        are null, those fields will not be queried for
                picture: Boolean specifying whether to retrieve the profile picture of each queried
                    user

            Returns:
                Single emitting a list of User objects on success, or an error if an error occurred
                    during the querry
         */
        Log.d(TAG, "search users invoked")
        if (checkVisitQuery(visitQuery)) {
            if (visitQuery.buildingName != null) {
                return buildingUseCases.getBuildingInfo(visitQuery.buildingName!!)
                    .flatMap { building ->
                        visitQuery.buildingId = building.id
                        visitRepo.query(visitQuery)
                            .flatMap { visitEntities ->
                                Log.d(TAG, visitEntities.toString())
                                Observable.fromIterable(visitEntities)
                                    .flatMap { visitEntity ->
                                        Observable.just(visitEntity.userId)
                                    }
                                    .distinct()
                                    .flatMap { userId ->
                                        userRepo.get(userId).toObservable()
                                    }
                                    .filter { userEntity -> checkUser(userEntity, userQuery)}
                                    .flatMap { userEntity ->
                                        getUser(null, null, true, userEntity).toObservable()
                                    }.toList()
                            }
                    }
            } else {
                return visitRepo.query(visitQuery)
                    .flatMap { visitEntities ->
                        Observable.fromIterable(visitEntities)
                            .flatMap { visitEntity ->
                                Observable.just(visitEntity.userId)
                            }
                            .distinct()
                            .flatMap { userId ->
                                userRepo.get(userId).toObservable()
                            }
                            .filter { userEntity -> checkUser(userEntity, userQuery)}
                            .flatMap { userEntity ->
                                getUser(null, null, true, userEntity).toObservable()
                            }.toList()
                    }
            }
        } else {
            return userRepo.getAll()
                .flatMapObservable { userEntities ->
                    Observable.fromIterable(userEntities)
                }
                .filter { userEntity -> checkUser(userEntity, userQuery)}
                .flatMap { userEntity ->
                    getUser(null, null, true, userEntity).toObservable()
                }.toList()
        }
    }

    open fun observeUsersInBuilding(buildingName: String): Observable<List<User>> {
        Log.d(TAG, "observe users in building")
        return buildingUseCases.getBuildingInfo(buildingName)
            .flatMapObservable { building ->
                userRepo.observeUsersInBuilding(building.id)
                    .flatMap { userEntities ->
                        Observable.fromIterable(userEntities)
                            .flatMap { userEntity ->
                                Log.d(TAG, "calling get picture and user for ${userEntity.id}")
                                getPictureAndUser(true, null, building, userEntity).toObservable()
                            }
                            .flatMap { userEntity ->
                                Log.d(TAG, "got user ${userEntity.id}")
                                Observable.just(userEntity)
                            }
                            .toList().toObservable()
                    }
            }
    }

    private fun getPictureAndUser(picture: Boolean, authEntity: AuthEntity?, building: Building?, userEntity: UserEntity): Single<User> {
        /*
        Helper function that gets the picture if requested and then gets tthe User object

            Params:
                picture: Boolean specifying whether to retrieve the profile pictture
                authEntity: AuthEntity object used for getting the user's email if the userr to be
                    retrieved is the currently logged in user
                building: Building object specifying the currently checked in building of the user.
                    null if not checked in.
                userEntity: UserEntity object of the user

            Returns:
                Single that emits a User object on success or an error otherwise
         */
        return if (picture && userEntity.photoUrl != null) {
            pictureRepo.get(userEntity.photoUrl!!)
                .flatMap { pictureByteArray ->
                    Single.just(buildUser(authEntity, userEntity, building, pictureByteArray))
                }
        } else {
            Single.just(buildUser(authEntity, userEntity, building, null))
        }
    }

    private fun buildUser(authEntity: AuthEntity?, userEntity: UserEntity, building: Building?, picture: ByteArray? = null): User {
    /*
    Converts a list of objects in a User object that will be useful for the UI

        Params:
            authEntity: AuthEntity object. Null if the user to be built is not logged in
            userEntity: UserEntity object corresponding to the user
            building: Building object representing the checked in building of the user. Null
                if the user is not checked in
            picture: Profile picture of the user.

         Returns:
            User object

     */
        return if (authEntity != null) {
            User(userEntity.id!!, userEntity.isStudent,
                authEntity.email, userEntity.firstName,
                userEntity.lastName, userEntity.major,
                building, userEntity.studentId, userEntity.deleted, picture)
        } else {
            User(userEntity.id!!, userEntity.isStudent,
                null, userEntity.firstName,
                userEntity.lastName, userEntity.major,
                building, userEntity.studentId, userEntity.deleted, picture)
        }
    }

    private fun overwrite(curr: UserEntity, truth: UserEntity): UserEntity {
        /*
        Overwrites the curr userEntity with the new fields that are not null from the truth userEntity

            Params:
                curr: UserEntity representing the current fields of the user
                truth: UserEntity representing the new fields of the user. Fields that are null will
                    not overwrite the curr

            Returns:
                overwritten UserEntity object
         */
        return UserEntity(
            truth.id ?: curr.id,
            truth.isStudent ?: curr.isStudent,
            truth.firstName ?: curr.firstName,
            truth.lastName ?: curr.lastName,
            truth.major ?: curr.major,
            truth.checkedInBuildingId ?: curr.checkedInBuildingId,
            truth.studentId ?: curr.studentId,
            truth.deleted ?: curr.deleted,
            truth.photoUrl ?: curr.photoUrl
        )
    }

    private fun checkUser(userEntity: UserEntity, userQuery: UserQuery): Boolean {
        /*
        Checks whether the non-null fields in the userQuery match the fields in the userEntity.

            Params:
                userEntity: UserEntity object to check
                userQuery: UserQuery object whose non-null fields will be used to check
                    whether it matches the userEnti21ec4abd2cbd62e5330af29bfe74fc3beb737d58ty

            Returns:
                Boolean that returns true whether the userQuery matches the userEntity, or
                false otherwise
         */
        Log.d(TAG, "filtering users")
        var result = true
        if (userQuery.firstName != null) {
            if (userEntity.firstName == null) result = false
            else if (userQuery.firstName !in userEntity.firstName!!) result = false
        }
        if (userQuery.lastName != null) {
            if (userEntity.lastName == null) result = false
            else if (userQuery.lastName !in userEntity.lastName!!) result = false
        }
        if (userQuery.lastName != null && userQuery.lastName !in userEntity.lastName!!)
            result = false
        if (userQuery.isCheckedIn != null) {
            if (userEntity.checkedInBuildingId == null && userQuery.isCheckedIn!!)
                result = false
            if (userEntity.checkedInBuildingId != null && !userQuery.isCheckedIn!!)
                result = false
        }
        if (userQuery.major != null && userQuery.major != userEntity.major)
            result = false
        if (userQuery.isStudent != null && userQuery.isStudent != userEntity.isStudent)
            result = false
        if (userQuery.studentId.toBoolean() && userQuery.studentId != userEntity.studentId)
            result = false
        return result
    }

    private fun checkVisitQuery(visitQuery: VisitQuery): Boolean {
        if (visitQuery.buildingName != null) return true
        if (visitQuery.buildingId != null) return true
        if (visitQuery.startCheckIn != null) return true
        if (visitQuery.endCheckIn != null) return true
        return false
    }
}