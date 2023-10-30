package com.yunushan.letgocasestudy.domain.di

import com.yunushan.letgocasestudy.data.repository.datasource.remote.RemoteDataSource
import com.yunushan.letgocasestudy.data.repository.datasource.remote.RemoteDataSourceImpl
import com.yunushan.letgocasestudy.data.repository.MainApiService
import com.yunushan.letgocasestudy.data.repository.Repository
import com.yunushan.letgocasestudy.data.repository.RepositoryImpl
import com.yunushan.letgocasestudy.domain.interceptor.HttpInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().authenticator(HttpInterceptor()).addInterceptor(logging).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.kacasatar.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MainApiService =
        retrofit.create(MainApiService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: MainApiService): RemoteDataSource =
        RemoteDataSourceImpl(apiService)


    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = RepositoryImpl(remoteDataSource)

}