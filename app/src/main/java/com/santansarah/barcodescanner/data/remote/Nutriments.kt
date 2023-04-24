package com.santansarah.barcodescanner.data.remote

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The API could come back with:
 * - "carbohydrates_serving":2
 * - or, if it was missing "carbohydrates_serving", then for some reason
 *   it comes back as: "carbohydrates_serving":""
 *   which gives the error: kotlinx.serialization.json.internal.JsonDecodingException:
 *   Unexpected JSON token at offset 354: Failed to parse type 'double' for input '' at path:
 *   $.product.nutriments.carbohydrates_serving
 *   JSON input: .....2.3,"carbohydrates_serving":"","carbohydrates_unit":"g","car.
 *   This only seems to happen from Retrofit; this works fine:
 *   val item = Json {
 *                         ignoreUnknownKeys = true
 *                         isLenient = true
 *                         encodeDefaults = true
 *                         explicitNulls = false
 *                     }.decodeFromString<ItemListing>(cocMilkReal)
 */
@Serializable
data class Nutriments(
    //@SerialName("carbohydrates_serving") val carbohydrates: StringDouble? = null,
    @SerializedName("carbohydrates_serving") val carbohydrates: Double = 0.00,
    @SerialName("energy-kcal_serving") val calories: StringDouble? = null,
    @SerialName("cholesterol_serving") val cholesterol: StringDouble? = null,
    @SerialName("fat_serving") val fat: StringDouble? = null,
    @SerialName("saturated-fat_serving") val saturatedFat: StringDouble? = null,
    @SerialName("monounsaturated-fat_serving") val monounsaturatedFat: StringDouble? = null,
    @SerialName("polyunsaturated-fat_serving") val polyunsaturatedFat: StringDouble? = null,
    @SerialName("proteins_serving") val protein: StringDouble? = null,
    @SerialName("fiber_serving") val fiber: StringDouble? = null,
    @SerialName("sodium_serving") val sodium: StringDouble? = null,
    @SerialName("sugars_serving") val sugar: StringDouble? = null,
    @SerialName("potassium_serving") val potassium: StringDouble? = null,
)

@Serializable
@JvmInline
value class StringDouble(private val jsonValue: String = "0.00") {
    val asDouble: Double
        get() = try {
            jsonValue.toDouble()
        } catch (e: Exception) {
            0.00
        }
}

fun StringDouble?.formatToGrams(): String = "%.1f".format(this?.asDouble ?: 0.00)
fun StringDouble?.formatToMgs(): String = "%.1f".format((this?.asDouble ?: 0.00).times(1000))


/*

object ColorAsStringSerializer : KSerializer<Double> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Double", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: Double) {
        val string = value.rgb.toString(16).padStart(6, '0')
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Color {
        val string = decoder.decodeString()
        return Color(string.toInt(16))
    }
}
*/
