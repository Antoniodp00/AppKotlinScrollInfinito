package com.proyecto.stadiumapp.model

import java.io.Serializable

data class Team(
    val name: String,
    val stadium: String,
    val logoUrl: String, // La URL del logo desde la API
    val latitude: Double, // Coordenadas para el mapa
    val longitude: Double, // Coordenadas para el mapa
    val webUrl: String // URL de la web oficial del equipo
) : Serializable // Para pasar el objeto a DetailActivity!