package com.example.androidapp.network

data class PersonajeResponse(
    val results: List<Personaje>
)

data class Personaje(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String
)
