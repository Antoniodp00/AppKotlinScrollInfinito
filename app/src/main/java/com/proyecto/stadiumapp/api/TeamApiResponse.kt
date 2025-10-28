package com.proyecto.stadiumapp.api

import com.google.gson.annotations.SerializedName

data class TeamApiResponse(
    val teams: List<ApiTeam>
)

data class ApiTeam(
    @SerializedName("strTeam") // Mapea el campo "strTeam" del JSON a "name" en Kotlin
    val name: String?,

    @SerializedName("strStadium") // Mapea "strStadium" a "stadium"
    val stadium: String?,

    @SerializedName("strTeamBadge") // Mapea "strTeamBadge" (URL del escudo) a "logoUrl"
    val logoUrl: String?,

    @SerializedName("strWebsite") // Mapea "strWebsite" a "website"
    val website: String?

// NOTA: La API de TheSportsDB no proporciona coordenadas de latitud/longitud fiables directamente en este endpoint.
// Las añadiremos "a mano" en TeamListActivity al mapear los datos para nuestro mapa.
)