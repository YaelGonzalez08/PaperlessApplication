package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Comunicados

class comunicadoAdapter(
    var list: ArrayList<Comunicados> = arrayListOf<Comunicados>(),
    var onclicComunicado: (item: Comunicados) -> Unit
) : RecyclerView.Adapter<comincadoAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comincadoAdapterViewHolder {
        return comincadoAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: comincadoAdapterViewHolder, position: Int) {
        holder.bind(item = list[position], onclicComunicado)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newlista: ArrayList<Comunicados>) {
        list = newlista
        notifyDataSetChanged()
    }
}