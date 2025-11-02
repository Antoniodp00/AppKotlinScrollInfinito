package com.proyecto.stadiumapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.stadiumapp.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding

    /**
     * Se llama cuando la actividad es creada.
     * Obtiene la URL del Intent, la valida y la carga en un WebView.
     * Habilita JavaScript en el WebView para una correcta visualización.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("EXTRA_URL")

        // Si la URL es nula o vacía, muestra un error y cierra la actividad
        if (url.isNullOrBlank()) {
            Toast.makeText(this, "No se proporcionó una URL válida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configura el WebView
        binding.webViewDetail.settings.javaScriptEnabled = true
        binding.webViewDetail.webViewClient = WebViewClient() // Para que los enlaces se abran en el WebView
        binding.webViewDetail.loadUrl(url)
    }
}