package com.proyecto.stadiumapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.proyecto.stadiumapp.R
import com.proyecto.stadiumapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Declarar binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configurar binding y establecer vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configurar onClick del boton
        binding.btnViewTeams.setOnClickListener {

            //navegar siguiente pantalla
            val intent = Intent(this, TeamListActivity::class.java)
            //arrancar actividad
            startActivity(intent)
        }

    }
}