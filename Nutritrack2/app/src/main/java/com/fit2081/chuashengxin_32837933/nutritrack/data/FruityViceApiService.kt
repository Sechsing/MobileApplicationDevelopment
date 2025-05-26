package com.fit2081.chuashengxin_32837933.nutritrack.data

import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApiService {
    @GET("api/fruit/{name}")
    suspend fun getFruitByName(@Path("name") fruitName: String): FruitResponse
}
