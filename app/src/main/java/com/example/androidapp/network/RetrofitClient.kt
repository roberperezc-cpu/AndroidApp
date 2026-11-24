package com.example.androidapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Reemplaza 192.168.1.15 por tu IPv4 local (o 10.0.2.2 si usas emulador)
    private const val BASE_URL_MICROSERVICE = "http://192.168.1.9:8080/api/"
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