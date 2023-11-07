package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import Documentos
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity

class FilesListAdapter(
    var list: ArrayList<Documentos> = arrayListOf<Documentos>(),
    val actionClick: (item: Documentos) -> Unit,

    ) : RecyclerView.Adapter<FilesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesListViewHolder {
        return FilesListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FilesListViewHolder, position: Int) {
        holder.bind(item = list[position],actionClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newlista: ArrayList<Documentos>) {
        list = newlista
        notifyDataSetChanged()
    }
}