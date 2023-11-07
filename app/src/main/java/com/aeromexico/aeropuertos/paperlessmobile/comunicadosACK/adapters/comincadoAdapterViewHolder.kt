package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Comunicados
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemComunicadoBinding
import java.util.*

public class comincadoAdapterViewHolder(var binding: ItemComunicadoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): comincadoAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemComunicadoBinding.inflate(layoutInflater, parent, false)
            return comincadoAdapterViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Comunicados,
        onclicComunicado: (item: Comunicados) -> Unit,
    ) {
        binding.apply {
            tittleComunicado.text = item.titulo.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            if (item.prioritario) {
                textView222.visibility = View.VISIBLE
            }
            contenido.text = item.cuerpoMensaje
            fechaValido.text = Fecha().stringToFechaOnly(item.vigencia)
            textView22.root.setOnClickListener {
                onclicComunicado.invoke(item)
            }
            textView36.setOnClickListener {
                onclicComunicado.invoke(item)
            }
            ee.setOnClickListener {
                onclicComunicado.invoke(item)
            }
        }

    }
}