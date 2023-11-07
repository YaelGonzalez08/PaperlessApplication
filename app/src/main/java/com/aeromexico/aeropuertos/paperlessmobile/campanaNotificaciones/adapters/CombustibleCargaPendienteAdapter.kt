package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemEquipoDiarioPendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.google.gson.Gson

class CombustibleCargaPendienteAdapter(
    private var lista: List<CargacombustibleEntity>,
    val actionClick: (item_list: CargacombustibleEntity) -> Unit,
    val deleteClick: (item_list: CargacombustibleEntity) -> Unit
) : RecyclerView.Adapter<CombustibleCargaPendienteAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CombustibleCargaPendienteAdapter.AdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemEquipoDiarioPendienteBinding.inflate(layoutInflater, parent, false)
        return CombustibleCargaPendienteAdapter.AdapterViewHolder(
            itemBinding,
            actionClick,
            deleteClick
        )
    }

    override fun onBindViewHolder(
        holder: CombustibleCargaPendienteAdapter.AdapterViewHolder,
        position: Int
    ) {
        val item = lista[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun updateList(newLista: List<CargacombustibleEntity>) {
        lista = newLista
        notifyDataSetChanged()
    }


    class AdapterViewHolder(
        val binding: ItemEquipoDiarioPendienteBinding,
        val actionClick: (item_list: CargacombustibleEntity) -> Unit,
        val deleteClick: (item_list: CargacombustibleEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: CargacombustibleEntity? = null

        init {
            binding.apply {
                btnEnviar.setOnClickListener {
                    item?.let { actionClick.invoke(it) }
                }
                btnEliminar.setOnClickListener {
                    item?.let { deleteClick.invoke(it) }
                }
            }

        }

        fun bind(itemObj: CargacombustibleEntity) {
            var item = Gson().fromJson(itemObj.request.toString(), RequestOrdenCarga::class.java)
            this.item = itemObj
            binding.apply {
                if(!item.isPendingToSend){
                    btnEnviar.visibility = View.INVISIBLE
                    txtFalataCompletar.visibility = View.VISIBLE
                }
                txtDescripcion.text =
                    "ID Vuelo: ${item.guidVuelo}" // me hace falta esta info en el RQ
                txtFechaRev.text = "${item?.fechaCreacion}"
                txtOperadorName.text =
                    "Mecanico: ${item?.nombreMecanico}"
                txtSupervisorName.text =
                    "Oficial: ${item?.nombreOficialOperaciones}"

            }

        }
    }
}