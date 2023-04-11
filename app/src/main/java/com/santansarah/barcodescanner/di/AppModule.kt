package com.santansarah.barcodescanner.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
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
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


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
        val contentType: MediaType = "application/json".toMediaType()
        val jsonBuilder = Json {
            ignoreUnknownKeys = true
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient =
            OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
            .baseUrl(STAGINGAPI)
            .addConverterFactory(jsonBuilder
                .asConverterFactory(contentType!!))
            .client(client)
            .build()
            .create(FoodApi::class.java)
    }

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()

    @Provides
    fun provideFoodRepository(foodApi: FoodApi) =
        FoodRepository(foodApi)

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

