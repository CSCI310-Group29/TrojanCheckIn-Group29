package com.csci310_group29.trojancheckincheckout.di

import com.csci310_group29.trojancheckincheckout.data.datasource.fake.BuildingFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.datasource.fake.PictureFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.datasource.fake.VisitFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.AuthFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.PictureFirebaseDataSource
import com.csci310_group29.trojancheckincheckout.data.datasource.remote.UserFirebaseDataSource
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
    abstract fun bindAuthDataSource(authRepo: AuthFirebaseDataSource): AuthRepository

    @Binds
    @Singleton
    @Named("Data")
    abstract fun bindUserDataSource(userRepo: UserFirebaseDataSource): UserRepository

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