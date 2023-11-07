package com.aeromexico.aeropuertos.paperlessmobile.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemMenuModuleBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModule

class menuPrincipalViewHolder(
    var binding: ItemMenuModuleBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): menuPrincipalViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemMenuModuleBinding.inflate(layoutInflater, parent, false)
            return menuPrincipalViewHolder(itemBinding)
        }
    }

    fun bind(menuModule: MenuModule) {

        binding.apply {
           imagen.setBackgroundResource(menuModule.image)
            tvModulo.text = menuModule.name
            root.setOnClickListener {
                menuModule.destination.invoke(menuModule)
            }
        }


    }
}