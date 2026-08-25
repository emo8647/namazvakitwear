package com.emo8647.namazvakitwear.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ApiResponse(val data: ApiData)
data class ApiData(val timings: Map<String, String>)

interface AladhanApi {
    @GET("v1/timingsByCity?city=Mustafakemalpaşa&country=Turkey&method=13")
    suspend fun getTimings(): ApiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.aladhan.com/"

    val instance: AladhanApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AladhanApi::class.java)
        }
}
