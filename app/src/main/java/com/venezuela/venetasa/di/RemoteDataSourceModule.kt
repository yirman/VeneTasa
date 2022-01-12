package com.venezuela.venetasa.di

import com.venezuela.venetasa.remote.RateRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Singleton
    @Provides
    fun provideRateRemoteDataSource(connection: Connection) = RateRemoteDataSource(connection)


    @Singleton
    @Provides
    fun provideWebPageConnection() = Jsoup.connect("https://www.ivenezuela.travel/precio-dolar-venezuela-dolartoday-monitor-oficial-bcv-hoy-tipo-de-cambio/").timeout(90 * 1000)
}