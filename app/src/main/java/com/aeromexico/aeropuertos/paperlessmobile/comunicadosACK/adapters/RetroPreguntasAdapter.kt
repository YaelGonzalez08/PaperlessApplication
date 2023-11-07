package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import CuestionarioComunicado
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RetroPreguntasAdapter(
    var list: ArrayList<CuestionarioComunicado> = arrayListOf<CuestionarioComunicado>()
) : RecyclerView.Adapter<RetroPreguntaAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetroPreguntaAdapterViewHolder {
        return RetroPreguntaAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RetroPreguntaAdapterViewHolder, position: Int) {
        holder.bind(item = list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newlista: ArrayList<CuestionarioComunicado>) {
        list = newlista
        notifyDataSetChanged()
    }
}