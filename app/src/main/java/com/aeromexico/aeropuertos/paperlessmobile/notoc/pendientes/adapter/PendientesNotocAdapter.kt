package com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemEquipoDiarioPendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.google.gson.Gson

class PendientesNotocAdapter(private var notoc: List<NotocEntity>,private var listener: OnClickListener): RecyclerView.Adapter<PendientesNotocAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendientesNotocAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_equipo_diario_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendientesNotocAdapter.ViewHolder, position: Int) {
        val entity = notoc.get(position)
        val n = Gson().fromJson(entity.request, RequestNotoc::class.java)
        with(holder){
            setListener(entity)
            binding.txtDescripcion.text = "Pendiente de enviar"
            binding.txtFechaRev.text = n.fechaCreacion
            binding.txtSupervisorName.text = ""
            binding.txtOperadorName.text = ""

        }
    }

    override fun getItemCount(): Int = notoc.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemEquipoDiarioPendienteBinding.bind(view)
        fun setListener(pendiente: NotocEntity){
            with(binding.root){

                binding.btnEnviar.setOnClickListener { listener.onClick(pendiente) }
                binding.btnEliminar.setOnClickListener {
                    listener.onDelete(pendiente)
                }

            }
        }
    }
    fun setPendientes(pendientes: List<NotocEntity>){
        this.notoc = pendientes
        notifyDataSetChanged()
    }
}