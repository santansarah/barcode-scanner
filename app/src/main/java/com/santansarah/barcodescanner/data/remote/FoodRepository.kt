package com.santansarah.barcodescanner.data.remote

import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.utils.ServiceResult
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: FoodApi
) {

    suspend fun getInfoByBarCode(barCode: String): ServiceResult<ItemListing> {
        return try {
            val result = api.getInfoByBarCode(barCode = barCode)
            Timber.d(result.toString())
            ServiceResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(ErrorCode.API_ERROR)
        }
    }
}