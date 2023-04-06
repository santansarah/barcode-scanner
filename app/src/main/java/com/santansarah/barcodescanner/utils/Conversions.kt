package com.santansarah.barcodescanner.utils

fun Double.toMgs() = (this * 1000).toInt()
fun Double?.valueOrZero() = this ?: 0
