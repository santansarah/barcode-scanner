package com.santansarah.barcodescanner.data.remote

data class EcoscoreData(
    val adjustments: Adjustments,
    val ecoscore_not_applicable_for_category: String,
    val scores: Scores,
    val status: String
)