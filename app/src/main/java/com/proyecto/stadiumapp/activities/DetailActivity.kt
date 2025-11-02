package com.proyecto.stadiumapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.proyecto.stadiumapp.R
import com.proyecto.stadiumapp.databinding.ActivityDetailBinding
import com.proyecto.stadiumapp.model.Team
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var team: Team? = null
    private lateinit var mapView: MapView

    /**
     * Se llama cuando se crea la actividad.
     * Inicializa la vista, habilita el modo Edge-to-Edge, configura la Toolbar,
     * carga los datos del equipo y configura el mapa y los botones.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajustar padding para el Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar la Toolbar
        setupToolbar()

        // Configuración de OSMdroid
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        // 1. Obtener el objeto Team del Intent
        loadTeamData()

        if (team == null) {
            Toast.makeText(this, "No se encontraron datos del equipo", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. Poblar las Vistas
        populateViews()

        // 3. Configurar el Mapa
        setupMap()

        // 4. Configurar el botón para abrir la web
        setupOpenWebButton()
    }

    /**
     * Configura la Toolbar, añadiendo un listener al botón de navegación
     * para volver a la actividad anterior.
     */
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Carga los datos del equipo desde el Intent.
     * Es compatible con las nuevas y antiguas versiones de Android para obtener el objeto Serializable.
     */
    private fun loadTeamData() {
        try {
            team = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("EXTRA_TEAM", Team::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("EXTRA_TEAM") as? Team
            }
        } catch (e: Exception) {
            Log.e("DetailActivity", "Error al obtener el objeto Team", e)
        }
    }

    /**
     * Rellena las vistas de la actividad con los datos del equipo,
     * como el nombre, el estadio y el logo.
     */
    private fun populateViews() {
        team?.let {
            binding.toolbar.title = it.name
            binding.tvTeamNameDetail.text = it.name
            binding.tvStadiumNameDetail.text = it.stadium
            binding.ivTeamLogoDetail.load(it.logoUrl) {
                placeholder(R.mipmap.ic_launcher_round)
                error(R.mipmap.ic_launcher_round)
            }
        }
    }

    /**
     * Configura el mapa de OpenStreetMaps (OSMdroid).
     * Centra el mapa en las coordenadas del estadio y añade un marcador.
     */
    private fun setupMap() {
        team?.let {
            mapView = binding.mapView
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)

            val mapController = mapView.controller
            val stadiumLocation = GeoPoint(it.latitude, it.longitude)
            mapController.setZoom(16.5)
            mapController.setCenter(stadiumLocation)

            val stadiumMarker = Marker(mapView)
            stadiumMarker.position = stadiumLocation
            stadiumMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            stadiumMarker.title = it.stadium

            mapView.overlays.add(stadiumMarker)
        }
    }

    /**
     * Configura el listener de clic para el botón que abre la página web del equipo.
     * Lanza la [WebActivity] pasando la URL correspondiente.
     */
    private fun setupOpenWebButton() {
        binding.btnOpenWeb.setOnClickListener {
            team?.let {
                if (it.webUrl.isNotBlank()) {
                    val intent = Intent(this, WebActivity::class.java).apply {
                        putExtra("EXTRA_URL", it.webUrl)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No hay sitio web disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Se llama cuando la actividad vuelve a estar en primer plano.
     * Reanuda el mapa para que se actualice correctamente.
     */
    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) {
            mapView.onResume()
        }
    }

    /**
     * Se llama cuando la actividad pasa a segundo plano.
     * Pausa el mapa para ahorrar recursos.
     */
    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) {
            mapView.onPause()
        }
    }
}