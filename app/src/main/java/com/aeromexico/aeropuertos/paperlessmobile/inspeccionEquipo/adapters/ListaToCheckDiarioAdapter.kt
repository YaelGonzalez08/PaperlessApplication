package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Preguntas

class ListaToCheckDiarioAdapter(
    var lista: List<Preguntas>, var onclicAddPhotoHelper: (item: Preguntas, position: Int) -> Unit,var onclicAddDateHelper: (item: Preguntas, position: Int) -> Unit
) : RecyclerView.Adapter<ListaToCheckDiarioViewHolder>() {
    private lateinit var _holder: ListaToCheckDiarioViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaToCheckDiarioViewHolder {
        return ListaToCheckDiarioViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListaToCheckDiarioViewHolder, position: Int) {
        _holder = holder
        holder.bind(lista[position], onclicAddPhotoHelper,onclicAddDateHelper, position)

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun addPhotoInItem(ewlista: List<Preguntas>) {
        lista = ewlista
        notifyDataSetChanged()
    }

}

