package com.aeromexico.aeropuertos.paperlessmobile.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModule

class AdapterMenuPrincipal(var listModules: ArrayList<MenuModule>) : RecyclerView.Adapter<menuPrincipalViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuPrincipalViewHolder {
        return menuPrincipalViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: menuPrincipalViewHolder, position: Int) {
        holder.bind(
            listModules[position]
        )
    }

    override fun getItemCount(): Int {
        return listModules.size
    }

}
