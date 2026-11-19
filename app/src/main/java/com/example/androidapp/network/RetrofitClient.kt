package com.example.androidapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_MICROSERVICE = "https://microservice.example.com/api/"
    private const val BASE_URL_EXTERNAL = "https://rickandmortyapi.com/api/"

    val microserviceApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MICROSERVICE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val externalApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_EXTERNAL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
