package com.proyecto.stadiumapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.stadiumapp.databinding.ItemTeamBinding
import com.proyecto.stadiumapp.model.Team

/**
 * Adaptador para el RecyclerView que muestra la lista de equipos.
 * @param teamList La lista de equipos a mostrar.
 * @param onItemClick Una función lambda que se ejecuta al hacer clic en un equipo.
 */
class TeamAdapter(
    private val teamList: List<Team>,
    private val onItemClick: (Team) -> Unit
) : RecyclerView.Adapter<TeamViewHolder>() {

    /**
     * Se llama cuando el RecyclerView necesita un nuevo [TeamViewHolder].
     * Infla el layout del item y crea una instancia del ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TeamViewHolder(binding, onItemClick) // Pasa la función de clic al ViewHolder
    }

    /**
     * Se llama para mostrar los datos en una posición específica.
     * Vincula los datos del equipo con las vistas del ViewHolder.
     */
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teamList[position]
        holder.bind(team)
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount(): Int {
        return teamList.size
    }
}