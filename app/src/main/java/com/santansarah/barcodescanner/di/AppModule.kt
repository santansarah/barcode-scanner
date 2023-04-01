package com.santansarah.barcodescanner.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.data.remote.FoodApi
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.domain.STAGINGAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.MediaType

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    fun provideBarcodeScanner(@ApplicationContext activity: Context)
    = BarcodeScanner(activity)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(): FoodApi {
        val contentType = MediaType.parse("application/json")
        val jsonBuilder = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(STAGINGAPI)
            .addConverterFactory(jsonBuilder
                .asConverterFactory(contentType!!))
            .build()
            .create(FoodApi::class.java)
    }

    @Provides
    fun provideFoodRepository(foodApi: FoodApi) =
        FoodRepository(foodApi)

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher
