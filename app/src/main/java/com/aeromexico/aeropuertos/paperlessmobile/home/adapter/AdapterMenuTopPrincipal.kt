package com.aeromexico.aeropuertos.paperlessmobile.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModuleRecientes

class AdapterMenuTopPrincipal(
    var listModules: ArrayList<MenuModuleRecientes>,
    var destination: (String) -> Unit
) : RecyclerView.Adapter<menuTopViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuTopViewHolder {
        return menuTopViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: menuTopViewHolder, position: Int) {
        holder.bind(
            listModules[position], destination
        )
    }

    override fun getItemCount(): Int {
        return listModules.size
    }

}
