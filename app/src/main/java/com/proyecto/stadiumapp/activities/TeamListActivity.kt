package com.proyecto.stadiumapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.stadiumapp.adapter.TeamAdapter
import com.proyecto.stadiumapp.api.ApiTeam
import com.proyecto.stadiumapp.api.RetrofitClient
import com.proyecto.stadiumapp.databinding.ActivityTeamListBinding
import com.proyecto.stadiumapp.model.Team
import kotlinx.coroutines.launch

class TeamListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamListBinding
    private lateinit var teamAdapter: TeamAdapter
    private val teamList = mutableListOf<Team>()

    /**
     * Se llama cuando la actividad es creada.
     * Inicializa la vista, habilita el modo Edge-to-Edge,
     * configura el RecyclerView y carga los datos de los equipos desde la API.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeamListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        loadTeamsFromApi()
    }

    /**
     * Configura el RecyclerView, incluyendo el adaptador y el LayoutManager.
     * Define la acción a realizar al hacer clic en un elemento de la lista.
     */
    private fun setupRecyclerView() {
        teamAdapter = TeamAdapter(teamList) { team ->
            navigateToDetail(team)
        }
        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(this@TeamListActivity)
            adapter = teamAdapter
        }
    }

    /**
     * Carga la lista de equipos desde la API de forma asíncrona utilizando Coroutines.
     * Muestra una barra de progreso mientras se cargan los datos.
     */
    private fun loadTeamsFromApi() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val apiResponse = RetrofitClient.instance.getTeamsByLeague("Spanish La Liga")
                val mappedList = mapApiTeamsToTeams(apiResponse.teams)
                teamList.clear()
                teamList.addAll(mappedList)
                teamAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("TeamListActivity", "Error al cargar los equipos", e)
                Toast.makeText(this@TeamListActivity, "Error al cargar los equipos", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Mapea la lista de equipos de la API ([ApiTeam]) a la lista de equipos de la app ([Team]).
     * Asigna coordenadas y limpia las URLs de los logos.
     */
    private fun mapApiTeamsToTeams(apiTeams: List<ApiTeam>): List<Team> {
        return apiTeams.mapNotNull { apiTeam ->
            if (apiTeam.name == null || apiTeam.stadium == null) {
                Log.w("TeamListActivity", "Equipo o estadio nulo en la API: $apiTeam")
                null
            } else {
                val (lat, lon) = getCoordinatesFromApiTeam(apiTeam.name)
                val logoUrlLimpia = (apiTeam.logoUrl ?: "").replace("/preview", "")
                Team(
                    name = apiTeam.name,
                    stadium = apiTeam.stadium,
                    logoUrl = logoUrlLimpia,
                    latitude = lat,
                    longitude = lon,
                    webUrl = apiTeam.website ?: ""
                )
            }
        }
    }

    /**
     * Devuelve las coordenadas (latitud y longitud) para un equipo específico.
     * @param teamName El nombre del equipo.
     * @return Un par de Doubles con la latitud y la longitud.
     */
    private fun getCoordinatesFromApiTeam(teamName: String): Pair<Double, Double> {
        return when (teamName) {
            "Real Madrid" -> Pair(40.453054, -3.688344)       // Santiago Bernabéu
            "Barcelona" -> Pair(41.380896, 2.122820)         // Spotify Camp Nou
            "Athletic Bilbao" -> Pair(43.2641, -2.9493)       // San Mamés
            "Atletico Madrid" -> Pair(40.436162, -3.599187)   // Cívitas Metropolitano
            "Sevilla" -> Pair(37.3841, -5.9721)               // Ramón Sánchez-Pizjuán
            "Real Betis" -> Pair(37.3565, -5.9822)            // Benito Villamarín
            "Valencia" -> Pair(39.4746, -0.3587)              // Mestalla
            "Real Sociedad" -> Pair(43.3014, -1.9736)         // Reale Arena
            "Villarreal" -> Pair(39.9431, -0.1037)           // Estadio de la Cerámica
            "Celta Vigo" -> Pair(42.2119, -8.7402)            // Balaídos
            "Osasuna" -> Pair(42.8000, -1.6369)               // El Sadar
            "Getafe" -> Pair(40.3270, -3.7150)                // Coliseum Alfonso Pérez
            "Rayo Vallecano" -> Pair(40.3917, -3.6596)        // Campo de Fútbol de Vallecas
            "Mallorca" -> Pair(39.5898, 2.6293)               // Estadi Mallorca Son Moix
            "Almería" -> Pair(36.8497, -2.4331)               // Power Horse Stadium
            "Cádiz" -> Pair(36.5033, -6.2713)                 // Estadio Nuevo Mirandilla
            "Granada" -> Pair(37.1589, -3.6050)               // Nuevo Los Cármenes
            "Las Palmas" -> Pair(28.1006, -15.4566)           // Estadio de Gran Canaria
            "Deportivo Alavés" -> Pair(42.8394, -2.6875)     // Mendizorrotza
            "Girona" -> Pair(41.9634, 2.8226)                 // Estadi de Montilivi
            "Levante" -> Pair(39.4946, -0.3586)              // Ciutat de València
            else -> Pair(0.0, 0.0) // Coordenadas por defecto
        }
    }

    /**
     * Navega a la [DetailActivity] para mostrar los detalles del equipo seleccionado.
     * @param team El objeto [Team] que se pasará a la siguiente actividad.
     */
    private fun navigateToDetail(team: Team) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("EXTRA_TEAM", team)
        startActivity(intent)
    }
}
