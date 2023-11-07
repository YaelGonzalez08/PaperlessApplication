package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import CuestionarioComunicado
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas

class preguntasAdapter(
    var list: ArrayList<CuestionarioComunicado> = arrayListOf<CuestionarioComunicado>(),
    var respuestaSeleccionada:(respuesta: Respuestas, position:Int) -> Unit
) : RecyclerView.Adapter<preguntaAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): preguntaAdapterViewHolder {
        return preguntaAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: preguntaAdapterViewHolder, position: Int) {
        holder.bind(item = list[position], ::onRespuestaAbierta,position,respuestaSeleccionada)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newlista: ArrayList<CuestionarioComunicado>) {
        list = newlista
        notifyDataSetChanged()
    }
    private fun onRespuestaAbierta(item: CuestionarioComunicado, pos:Int){
        list[pos] = item
    }

    fun getLista(): ArrayList<CuestionarioComunicado> {
        return list
    }


}