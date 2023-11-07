package com.aeromexico.aeropuertos.paperlessmobile.metar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemMetarBinding
import com.aeromexico.aeropuertos.paperlessmobile.metar.objects.Metar

class MetarViewHolder(var binding: ItemMetarBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): MetarViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemMetarBinding.inflate(layoutInflater, parent, false)
            return MetarViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Metar,

        ) {
        binding.apply {
            textDate.text = "Actualización: "+ Fecha().stringToFecha(item.fechaCreacion.toString())
            Contenido.text = item.contenido
            txtSource.text = "Fuente: "+item.fuente
        }
    }
}