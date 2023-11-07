package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import Documentos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemFilelistBinding

public class FilesListViewHolder(var binding: ItemFilelistBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): FilesListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemFilelistBinding.inflate(layoutInflater, parent, false)
            return FilesListViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Documentos,
        actionClick: (item: Documentos) -> Unit,

        ) {
        binding.apply {
            textTitleDocument.setText("${item.titulo}.${item.extension}")
            btnViewDocument.setOnClickListener {
                actionClick.invoke(item)
            }


        }

    }
}