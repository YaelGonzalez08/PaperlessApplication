package com.aeromexico.aeropuertos.paperlessmobile.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemMenuModuleBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemMenuTopBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModule

class menuTopViewHolder(
    var binding: ItemMenuTopBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): menuTopViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemMenuTopBinding.inflate(layoutInflater, parent, false)
            return menuTopViewHolder(itemBinding)
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