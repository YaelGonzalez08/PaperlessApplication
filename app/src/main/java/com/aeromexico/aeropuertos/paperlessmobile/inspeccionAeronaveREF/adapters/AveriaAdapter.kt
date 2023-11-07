package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.adapters

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia

class AveriaAdapter(
    var lista: ArrayList<Averia>,
    var addOPhoto:(Averia,Int,String?)-> Unit,
    var viewPhoto: (Bitmap) -> Unit,
    var addDanio:(Averia,Int,Int)-> Unit

) : RecyclerView.Adapter<AveriaViewHolder>() {
    private lateinit var _holder: AveriaViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AveriaViewHolder {
        return AveriaViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AveriaViewHolder, position: Int) {
        _holder = holder
        holder.bind(
            lista[position],position,
            ::update,addOPhoto,::updateSilence,viewPhoto,addDanio
        )

    }
    fun updateSilence(item: Averia, position: Int) {
        lista[position] = item

    }
     fun update(item: Averia, position: Int) {
        lista[position] = item
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun updateList(newlista: ArrayList<Averia>) {
        lista = newlista
        notifyDataSetChanged()
    }

    fun addPhotoInItem(newlista: ArrayList<Averia>) {
        lista = newlista
        notifyDataSetChanged()
    }
}
