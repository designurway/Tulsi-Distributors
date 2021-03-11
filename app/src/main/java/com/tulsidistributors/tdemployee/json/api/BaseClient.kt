package com.tulsidistributors.tdemployee.json

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseClient {
    private val BASE_URL = "http://192.168.4.176:8000/api/sales_executive/";

    val getInstance: TDApi by lazy {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(TDApi::class.java)
    }
}