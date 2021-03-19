package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.datasource.fake.*
import com.csci310_group29.trojancheckincheckout.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ADataSourceModule {

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindAuthDataSource(authRepo: AuthFakeDataSource): AuthRepository

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindUserDataSource(userRepo: UserFakeDataSource): UserRepository

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindVisitDataSource(visitRepo: VisitFakeDataSource): VisitRepository

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindPicturesDataSource(picturesRepo: PictureFakeDataSource): PicturesRepository

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindBuildingDataSource(buildingRepo: BuildingFakeDataSource): BuildingRepository
}