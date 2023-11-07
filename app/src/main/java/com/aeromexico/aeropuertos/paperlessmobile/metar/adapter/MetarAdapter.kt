package com.aeromexico.aeropuertos.paperlessmobile.metar.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.metar.objects.Metar

class MetarAdapter(
    var lista: List<Metar>
) : RecyclerView.Adapter<MetarViewHolder>() {
    private lateinit var _holder: MetarViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MetarViewHolder {
        return MetarViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MetarViewHolder, position: Int) {
        _holder = holder
        holder.bind(lista[position])

    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun updateList(newlista: List<Metar>?) {
        if (newlista != null) {
            lista = newlista
            notifyDataSetChanged()
        }

    }

}
