package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("brand_owner") val brandOwner: String? = null,
    @SerialName("ingredients_text_en") val ingredients: String? = null,
    val nutriments: Nutriments,
    @SerialName("product_name") val productName: String,
    @SerialName("serving_size") val servingSize: String? = null,
    //@SerialName("image_front_small_url") val imgFrontSmall: String? = null,
    //@SerialName("image_front_thumb_url") val imgFrontThumb: String? = null,
    @SerialName("image_front_url") val imgFrontUrl: String? = null,
    //@SerialName("image_nutrition_small_url") val imgNutritionSmall: String? = null,
    //@SerialName("image_nutrition_thumb_url") val imgNutritionThumb: String? = null,
    @SerialName("image_nutrition_url") val imgNutritionUrl: String? = null,
    //@SerialName("image_small_url") val imgSmallUrl: String? = null,
    //@SerialName("image_thumb_url") val imgThumbUrl: String? = null,
    //@SerialName("image_url") val imgUrl: String? = null,
) {
    companion object {
        val fields = listOf(
            "brand_owner",
            "ingredients_text_en",
            "nutriments", "product_name", "serving_size",
            "image_front_url", "image_nutrition_url"
        )
    }
}