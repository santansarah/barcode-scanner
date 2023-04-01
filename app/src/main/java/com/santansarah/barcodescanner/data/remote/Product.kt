package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val allergens: String,
    val brands: String,
    @SerialName("ingredients_text_with_allergens_en") val ingredients: String,
    val nutriments: Nutriments,
    @SerialName("product_name") val productName: String,
    @SerialName("serving_size") val servingSize: String,
    @SerialName("image_front_small_url") val imgFrontSmall: String? = null,
    @SerialName("image_front_thumb_url") val imgFrontThumb: String? = null,
    @SerialName("image_front_url") val imgFrontUrl: String? = null,
    @SerialName("image_nutrition_small_url") val imgNutritionSmall: String? = null,
    @SerialName("image_nutrition_thumb_url") val imgNutritionThumb: String? = null,
    @SerialName("image_nutrition_url") val imgNutritionUrl: String? = null,
    @SerialName("image_small_url") val imgSmallUrl: String? = null,
    @SerialName("image_thumb_url") val imgThumbUrl: String? = null,
    @SerialName("image_url") val imgUrl: String? = null,
)