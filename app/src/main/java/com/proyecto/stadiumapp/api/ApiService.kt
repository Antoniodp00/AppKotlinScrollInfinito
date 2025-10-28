package com.proyecto.stadiumapp.api

import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "123"

interface ApiService {

    // Endpoint para obtener todos los equipos de una liga específica
    @GET("$API_KEY/search_all_teams.php")
    suspend fun getTeamsByLeague(@Query("l") league: String): TeamApiResponse
}