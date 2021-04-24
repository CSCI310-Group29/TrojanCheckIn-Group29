package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.repo.*
import com.csci310_group29.trojancheckincheckout.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepository

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindUserRepo(userRepo: UserRepoImpl): UserRepository

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindVisitRepo(visitRepo: VisitRepoImpl): VisitRepository

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindPicturesRepo(picturesRepo: PicturesRepoImpl): PicturesRepository

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindBuildingRepo(buildingRepo: BuildingRepoImpl): BuildingRepository

    @Binds
    @Singleton
    @Named("Repo")
    abstract fun bindMessagingRepo(messagingRepo: MessagingRepoImpl): MessagingRepository
}