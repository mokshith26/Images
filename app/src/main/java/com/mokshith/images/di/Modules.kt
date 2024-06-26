package com.mokshith.images.di

import com.mokshith.images.common.ViewUtils.Companion.BASE_URL
import com.mokshith.images.data.remote.WebServices
import com.mokshith.images.data.repository.RepositoryImpl
import com.mokshith.images.domain.repository.Repository
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
object Modules {
    @Provides
    @Singleton
    fun getRepository(webServices: WebServices): Repository {
        return RepositoryImpl(webServices)
    }
    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            //Set the Level to NONE in release mode
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .build()
    }
    @Provides
    @Singleton
    fun getRetrofitInstance(okHttpClient: OkHttpClient): WebServices {
        return Retrofit.Builder()
            .baseUrl(
                BASE_URL
            )
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebServices::class.java)
    }
}