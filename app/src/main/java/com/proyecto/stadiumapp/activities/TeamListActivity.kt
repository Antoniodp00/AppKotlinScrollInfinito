package com.proyecto.stadiumapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log // Para ver errores en el Logcat
import android.view.View // Para manipular la visibilidad del ProgressBar
import android.widget.Toast // Para mostrar mensajes al usuario
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Para Coroutines
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadTeamsFromApi()

    }

    private fun setupRecyclerView() {
        // Inicializa el adaptador, pasando la lista de equipos y la función de clic
        teamAdapter = TeamAdapter(teamList) { team ->
            navigateToDetail(team) // Llama a esta función cuando se haga clic en un equipo
        }

        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(this@TeamListActivity) // Layout manager vertical
            adapter = teamAdapter // Asigna el adaptador al RecyclerView
        }
    }

    private fun loadTeamsFromApi() {
        binding.progressBar.visibility = View.VISIBLE // Muestra el ProgressBar

        lifecycleScope.launch {
            try {
                val apiResponse = RetrofitClient.instance.getTeamsByLeague("Spanish La Liga")

                val mappedList = mapApiTeamsToTeams(apiResponse.teams)

                Log.d("TeamListActivity", "Lista de equipos mapeada (${mappedList.size} equipos): $mappedList")
                teamList.clear()
                teamList.addAll(mappedList)
                teamAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("TeamListActivity", "Error al cargar los equipos", e)
                Toast.makeText(
                    this@TeamListActivity,
                    "Error al cargar los equipos",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE // Oculta el ProgressBar
            }
        }
    }

    private fun mapApiTeamsToTeams(apiTeams: List<ApiTeam>): MutableList<Team> {
        val appTeams = mutableListOf<Team>()

        for (apiTeam in apiTeams) {
            if (apiTeam.name == null || apiTeam.stadium == null) {
                Log.w("TeamListActivity", "Equipo o estadio nulo en la API: $apiTeam")
                continue
            }

            val (lat, lon) = getCoordinatesFromApiTeam(apiTeam.name)
            val logoUrlLimpia = (apiTeam.logoUrl ?: "").replace("/preview", "")

            Log.d("TeamListActivity", "Mapeando: ${apiTeam.name}, Logo final: $logoUrlLimpia")
            appTeams.add(
                Team(
                    name = apiTeam.name,
                    stadium = apiTeam.stadium,
                    logoUrl = logoUrlLimpia,
                    latitude = lat,
                    longitude = lon,
                    webUrl = apiTeam.website ?: ""
                )
            )
        }
        return appTeams
    }


    private fun getCoordinatesFromApiTeam(teamName: String): Pair<Double, Double> {
        return when (teamName) {
            "Real Madrid" -> Pair(40.453054, -3.688344) // Santiago Bernabéu
            "Barcelona" -> Pair(41.380896, 2.122820) // Spotify Camp Nou
            "Athletic Bilbao" -> Pair(43.2641, -2.9493) // San Mamés
            "Atletico Madrid" -> Pair(40.436162, -3.599187) // Cívitas Metropolitano
            "Sevilla" -> Pair(37.384, -5.9723) // Ramón Sánchez-Pizjuán
            "Real Betis" -> Pair(37.3562, -5.9922) // Benito Villamarín
            "Valencia" -> Pair(39.4746, -0.3587) // Mestalla
            "Real Sociedad" -> Pair(43.3006, -1.9739) // Anoeta
            "Villarreal" -> Pair(39.9431, -0.1037) // Estadio de la Cerámica
            "Celta Vigo" -> Pair(42.2132, -8.7615) // Balaídos
            "Osasuna" -> Pair(42.8055, -1.637) // El Sadar
            "Getafe" -> Pair(40.294, -3.708) // Coliseum Alfonso Pérez
            "Rayo Vallecano" -> Pair(40.4093, -3.6657) // Campo de Fútbol de Vallecas
            "Mallorca" -> Pair(39.5855, 2.6288) // Estadi de Son Moix
            "Almería" -> Pair(36.8532, -2.4332) // Power Horse Stadium
            "Cádiz" -> Pair(36.5165, -6.2731) // Estadio Nuevo Mirandilla
            "Granada" -> Pair(37.1524, -3.614) // Nuevo Los Cármenes
            "Las Palmas" -> Pair(28.1065, -15.4264) // Estadio de Gran Canaria
            "Deportivo Alavés" -> Pair(42.8465, -2.671) // Mendizorroza
            "Girona" -> Pair(41.9796, 2.8016) // Estadi de Montilivi
            "Levante" -> Pair(39.4936, -0.3319)
            else -> Pair(0.0, 0.0) // Coordenadas por defecto si no se encuentra el equipo
        }
    }

    // Función para navegar a DetailActivity, pasando el objeto Team
    private fun navigateToDetail(team: Team) {
        val intent = Intent(this, DetailActivity::class.java)
        // Pasa el objeto Team completo como un extra en el Intent
        intent.putExtra("EXTRA_TEAM", team)
        startActivity(intent)
    }
}

