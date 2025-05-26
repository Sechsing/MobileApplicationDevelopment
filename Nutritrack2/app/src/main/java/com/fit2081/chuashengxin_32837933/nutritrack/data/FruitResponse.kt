package com.fit2081.chuashengxin_32837933.nutritrack.data

data class FruitResponse(
    val name: String,
    val family: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)
