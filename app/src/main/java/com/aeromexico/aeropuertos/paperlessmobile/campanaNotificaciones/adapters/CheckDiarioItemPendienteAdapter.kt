package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemEquipoDiarioPendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.google.gson.Gson

class CheckDiarioItemPendienteAdapter(
    private var lista: List<CheckEquipoDiarioEntity>,
    val actionClick: (item_list: CheckEquipoDiarioEntity) -> Unit,
    val deleteClick: (item_list: CheckEquipoDiarioEntity) -> Unit
) :
    RecyclerView.Adapter<CheckDiarioItemPendienteAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemEquipoDiarioPendienteBinding.inflate(layoutInflater, parent, false)
        return AdapterViewHolder(itemBinding, actionClick, deleteClick)

    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = lista[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun updateList(newLista: List<CheckEquipoDiarioEntity>) {
        lista = newLista
        notifyDataSetChanged()
    }

    class AdapterViewHolder(
        val binding: ItemEquipoDiarioPendienteBinding,
        val actionClick: (item_list: CheckEquipoDiarioEntity) -> Unit,
        val deleteClick: (item_list: CheckEquipoDiarioEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: CheckEquipoDiarioEntity? = null

        init {
            binding.apply {
                contenedorEnviar.setOnClickListener {
                    item?.let { actionClick.invoke(it) }
                }
                btnEnviar.setOnClickListener {
                    item?.let { actionClick.invoke(it) }
                }
                btnEliminar.setOnClickListener {
                    item?.let { deleteClick.invoke(it) }
                }
            }

        }

        fun bind(itemObj: CheckEquipoDiarioEntity) {
            var item = Gson().fromJson(itemObj.datos.toString(), CheckToServer::class.java)
            this.item = itemObj
            binding.apply {
                txtDescripcion.text =
                    "${item.equipo?.numeroActivo} - ${item.equipo?.descripcion}"
                txtFechaRev.text = "${item.fecha}"
                txtOperadorName.text =
                    "Operador: ${item.operador?.name} ${item.operador?.apellidoPaterno}"
                txtSupervisorName.text =
                    "Supervisor: ${item.supervisor?.name} ${item.operador?.apellidoPaterno}"

            }

        }
    }

}