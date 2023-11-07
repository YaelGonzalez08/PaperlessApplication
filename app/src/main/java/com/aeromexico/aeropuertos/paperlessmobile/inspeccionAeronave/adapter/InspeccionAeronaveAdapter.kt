package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemPendienteInspecAeronaveBinding



class InspeccionAeronaveAdapter(private var inspeccionesList: MutableList<InspeccionAeronaveEntity>, private var listener: InspeccionAeronaveClickListener):
RecyclerView.Adapter<InspeccionAeronaveAdapter.ViewHolder>(){

    //Context
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspeccionAeronaveAdapter.ViewHolder {
        context = parent.context;
        val view = LayoutInflater.from(context).inflate(R.layout.item_pendiente_inspec_aeronave, parent, false);
        return ViewHolder(view);
    }


    override fun onBindViewHolder(holder: InspeccionAeronaveAdapter.ViewHolder, position: Int) {
        val inspeccion = inspeccionesList[position]

        with(holder){
            setListener(inspeccion)
            var ruta: String = inspeccion.Origen + " - " + inspeccion.Destino
            binding.tvtextFechaVuelo.text = inspeccion.FechaVuelo.substring(0,10).replace("-", "/")
            binding.tvNumVuelo.text = inspeccion.NumVuelo
            binding.tvOrigen.text = ruta
            binding.tvtMatricula.text = inspeccion.Matricula
            binding.tvEquipo.text = inspeccion.Equipo

        }
    }

    override fun getItemCount(): Int {
        return inspeccionesList.size;
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemPendienteInspecAeronaveBinding.bind(view)

        fun setListener(inspeccionAeronave: InspeccionAeronaveEntity){
            binding.btnEnviar.setOnClickListener{
                listener.onSend(inspeccionAeronave);
            }

            binding.tvEliminar.setOnClickListener{
                listener.onDelete(inspeccionAeronave)
            }
        }
    }


    //Asignar lista
    fun setList(inspeccionesList: MutableList<InspeccionAeronaveEntity>){
        this.inspeccionesList = inspeccionesList;
        notifyDataSetChanged();
    }

}