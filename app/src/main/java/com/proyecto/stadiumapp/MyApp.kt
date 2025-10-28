package com.proyecto.stadiumapp

import android.app.Application
import org.osmdroid.config.Configuration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //Configuracion para OSMdroid
        Configuration.getInstance().load(
           applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        );
        //Identifica la app en el servidor de mapas
        Configuration.getInstance().userAgentValue = "com.proyecto.stadiumapp";

    }
}