package com.proyecto.stadiumapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load // Importación para Coil
import com.proyecto.stadiumapp.R // Importación CORRECTA del R (de tu paquete principal)
import com.proyecto.stadiumapp.databinding.ItemTeamBinding // Importación CORRECTA de ViewBinding
import com.proyecto.stadiumapp.model.Team

class TeamAdapter(
    private val teamList: List<Team>,
    private val onItemClick: (Team) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    // ViewHolder: Contiene las referencias a las vistas de cada elemento de la lista (item_team.xml)
    inner class TeamViewHolder(val binding: ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        // Función para "enlazar" los datos de un objeto Team con las vistas
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

    // onCreateViewHolder: Se llama cuando el RecyclerView necesita una nueva vista (fila)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        // Infla el layout 'item_team.xml' utilizando ViewBinding
        val binding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context), // Contexto para inflar
            parent,
            false // No adjuntar a la raíz inmediatamente
        )
        return TeamViewHolder(binding) // Devuelve una nueva instancia de ViewHolder
    }

    // onBindViewHolder: Se llama para mostrar los datos en una posición específica
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teamList[position] // Obtiene el objeto Team de la lista
        holder.bind(team) // Enlaza los datos al ViewHolder
    }

    // getItemCount: Devuelve el número total de elementos en la lista de datos
    override fun getItemCount(): Int {
        return teamList.size
    }
}


