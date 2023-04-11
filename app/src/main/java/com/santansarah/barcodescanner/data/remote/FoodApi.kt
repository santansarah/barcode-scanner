package com.santansarah.barcodescanner.data.remote

import okhttp3.Credentials
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    
    companion object {
        val basic: String = Credentials.basic("off", "off")
    }

    @GET("product/{barCode}")
    suspend fun getInfoByBarCode(
        @Header("Authorization") authorization: String = basic,
        @Path("barCode") barCode: String,
        @Query("fields") fields: String): ItemListing

    //https://world.openfoodfacts.org/cgi/search.pl?search_terms=great+value+chips&search_simple=1
    // &action=process&json=true&fields=brand_owner,product_name,image_front_small_url
    @GET("https://us.openfoodfacts.org/cgi/search.pl?json=true")
    suspend fun searchProducts(
        @Header("Authorization") authorization: String = basic,
        @Query("search_terms") searchText: String,
        @Query("fields") fields: String,
        @Query("page") page: Int): SearchResults

}