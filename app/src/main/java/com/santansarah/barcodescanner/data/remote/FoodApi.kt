package com.santansarah.barcodescanner.data.remote

import okhttp3.Credentials
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FoodApi {
    
    companion object {
        val basic: String = Credentials.basic("off", "off")
    }

    @GET("product/{barCode}")
    suspend fun getInfoByBarCode(
        @Header("Authorization") authorization: String = basic,
        @Path("barCode") barCode: String): Listing

}