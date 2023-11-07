package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.Pax
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemClientesControlAbordajeBinding

class PasajerosAdapterViewHolder(var binding: ItemClientesControlAbordajeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): PasajerosAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemClientesControlAbordajeBinding.inflate(layoutInflater, parent, false)
            return PasajerosAdapterViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Pax,
        onclicAddEvidenciaHelper: (item: Pax, position: Int) -> Unit,
        onclicCancelado: (item: Pax, position: Int) -> Unit,
        onclicConfirmar: (item: Pax, position: Int) -> Unit,
        position: Int,
        enabled:Boolean
    ) {
        binding.btnDown.setOnClickListener {
            if (binding.contenedorDown.visibility == View.VISIBLE)
                binding.contenedorDown.visibility = View.GONE
            else
                binding.contenedorDown.visibility = View.VISIBLE
        }
        binding.apply {
            if(enabled){
                btnAddEvidenciaPhoto.visibility = View.VISIBLE
                btnCancelado.visibility = View.GONE
                mbEnviar.visibility = View.VISIBLE
            }else{
                btnAddEvidenciaPhoto.visibility = View.GONE
                btnCancelado.visibility = View.GONE
                mbEnviar.visibility = View.GONE
            }

            txtNombreApellido.text = "${item.name} ${item.lastName}"
            textNumeroEtiqueta.text = "${item.bagTags?:""}"
            textPiezas.text = "${item.bagCount?:""} / ${item.bagWeight}"
            textControl.text = "${item.checkInNumber?:""}"
            textDestino.text = "${item.destino?:""}"
            textConexion.text = "${item.conexion ?: ""}"

            if (!item.avih.isNullOrEmpty()) {
                item.avih.let {
                    btnAvih.text = "${it}"
                    btnAvih.isVisible = true
                }
            }
            if (!item.arpl.isNullOrEmpty()) {
                item.arpl.let {
                    btnARPL.text = "${it}"
                    btnARPL.isVisible = true
                }
            }
            if (!item.evidencia.isNullOrEmpty()) {
                item.evidencia.let {
                    isPhotoCaptured.isVisible = true
                }
            }

            btnAddEvidenciaPhoto.setOnClickListener {
                onclicAddEvidenciaHelper.invoke(item, position)
            }
            btnCancelado.setOnClickListener {
                onclicCancelado.invoke(item, position)
            }
            mbEnviar.setOnClickListener {
                onclicConfirmar.invoke(item, position)
            }
        }
    }
}