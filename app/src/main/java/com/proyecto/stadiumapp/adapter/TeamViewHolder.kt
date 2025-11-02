package com.proyecto.stadiumapp.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.proyecto.stadiumapp.R
import com.proyecto.stadiumapp.databinding.ItemTeamBinding
import com.proyecto.stadiumapp.model.Team

/**
 * ViewHolder para un elemento de la lista de equipos.
 * Se encarga de vincular los datos de un [Team] con las vistas del layout `item_team.xml`.
 * @param binding El objeto de ViewBinding para el layout del item.
 * @param onItemClick La función lambda que se ejecuta al hacer clic en el item.
 */
class TeamViewHolder(
    private val binding: ItemTeamBinding,
    private val onItemClick: (Team) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Vincula los datos de un [Team] a las vistas del ViewHolder.
     * @param team El objeto [Team] que contiene los datos a mostrar.
     */
    fun bind(team: Team) {
        // Carga la imagen del logo usando Coil desde la URL
        binding.ivTeamLogo.load(team.logoUrl) {
            placeholder(R.mipmap.ic_launcher_round) // Icono temporal mientras carga
            error(R.mipmap.ic_launcher_round) // Icono si falla la carga
        }
        binding.tvTeamName.text = team.name // Establece el nombre del equipo
        binding.tvStadiumName.text = team.stadium // Establece el nombre del estadio

        // Configura el listener de clic para toda la tarjeta (MaterialCardView)
        binding.root.setOnClickListener {
            onItemClick(team) // Llama a la función lambda pasada desde la Activity
        }
    }
}