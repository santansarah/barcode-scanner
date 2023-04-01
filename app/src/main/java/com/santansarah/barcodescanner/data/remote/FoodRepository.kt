package com.santansarah.barcodescanner.data.remote

import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: FoodApi
) {

    suspend fun getInfoByBarCode(barCode: String): Listing {
        return try {
            val result = api.getInfoByBarCode(barCode=barCode)
            Timber.d(result.toString())
            return result
        } catch(e: IOException) {
            e.printStackTrace()
            return Listing("", 0, "")
        } catch(e: HttpException) {
            e.printStackTrace()
            Listing("", 0, "")
        }
    }
}