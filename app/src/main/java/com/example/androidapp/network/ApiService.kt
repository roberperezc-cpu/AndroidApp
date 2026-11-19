package com.example.androidapp.network

import com.example.androidapp.datos.local.OportunidadEntity
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Microservicios CRUD para Oportunidades
    @GET("oportunidades")
    suspend fun obtenerOportunidades(): List<OportunidadEntity>

    @POST("oportunidades")
    suspend fun crearOportunidad(@Body oportunidad: OportunidadEntity): OportunidadEntity

    @PUT("oportunidades/{id}")
    suspend fun actualizarOportunidad(@Path("id") id: Int, @Body oportunidad: OportunidadEntity): OportunidadEntity

    @DELETE("oportunidades/{id}")
    suspend fun eliminarOportunidad(@Path("id") id: Int): Response<Void>

    // API Externa (Rick and Morty API)
    @GET("character")
    suspend fun obtenerPersonajesExternos(): PersonajeResponse
}
