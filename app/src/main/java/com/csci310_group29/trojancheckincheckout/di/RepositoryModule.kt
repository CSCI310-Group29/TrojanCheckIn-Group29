package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.repo.*
import com.csci310_group29.trojancheckincheckout.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepo(userRepo: UserRepoImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindVisitRepo(visitRepo: VisitRepoImpl): VisitRepository

    @Binds
    @Singleton
    abstract fun bindPicturesRepo(picturesRepo: PicturesRepoImpl): PicturesRepository

    @Binds
    @Singleton
    abstract fun bindBuildingRepo(buildingRepo: BuildingRepoImpl): BuildingRepository
}