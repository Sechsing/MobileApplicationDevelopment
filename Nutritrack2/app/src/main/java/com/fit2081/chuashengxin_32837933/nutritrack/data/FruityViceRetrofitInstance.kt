package com.fit2081.chuashengxin_32837933.nutritrack.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FruityViceRetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.fruityvice.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: FruityViceApiService = retrofit.create(FruityViceApiService::class.java)
}
