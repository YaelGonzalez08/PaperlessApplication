package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.Pax

class PasajerosAdapter(
    var lista: ArrayList<Pax>, var onclicAddEvidenciaHelper: (item: Pax, position: Int) -> Unit,
    var onclicCancelado: (item: Pax, position: Int) -> Unit,
    var onclicConfirmar: (item: Pax, position: Int) -> Unit,
    var enabled: Boolean
) : RecyclerView.Adapter<PasajerosAdapterViewHolder>() {
    private lateinit var _holder: PasajerosAdapterViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasajerosAdapterViewHolder {
        return PasajerosAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PasajerosAdapterViewHolder, position: Int) {
        _holder = holder
        holder.bind(
            lista[position],
            onclicAddEvidenciaHelper,
            onclicCancelado,
            onclicConfirmar,
            position, enabled
        )

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun updateList(newlista: ArrayList<Pax>) {
        lista = newlista
        notifyDataSetChanged()

    }

    fun addPhotoInItem(newlista: ArrayList<Pax>) {
        lista = newlista
        notifyDataSetChanged()
    }
}
