package com.venezuela.venetasa.di

import com.venezuela.venetasa.db.RateDao
import com.venezuela.venetasa.remote.RateRemoteDataSource
import com.venezuela.venetasa.repository.RateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideCarouselRepository(rateDao: RateDao, remoteDataSource: RateRemoteDataSource) =
        RateRepository(rateDao, remoteDataSource)
}