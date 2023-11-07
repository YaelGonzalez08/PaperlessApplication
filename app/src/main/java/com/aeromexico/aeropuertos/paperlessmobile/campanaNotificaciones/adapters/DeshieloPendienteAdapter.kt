package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemEquipoDiarioPendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.google.gson.Gson

class DeshieloPendienteAdapter(
    private var lista: List<DeshieloEntity>,
    val actionClick: (item_list: DeshieloEntity) -> Unit,
    val deleteClick: (item_list: DeshieloEntity) -> Unit
) :
    RecyclerView.Adapter<DeshieloPendienteAdapter.AdapterViewHolder>() {

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

    fun updateList(newLista: List<DeshieloEntity>) {
        lista = newLista
        notifyDataSetChanged()
    }

    class AdapterViewHolder(
        val binding: ItemEquipoDiarioPendienteBinding,
        val actionClick: (item_list: DeshieloEntity) -> Unit,
        val deleteClick: (item_list: DeshieloEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: DeshieloEntity? = null

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

        fun bind(itemObj: DeshieloEntity) {
            var item = Gson().fromJson(itemObj.datos.toString(), DeshieloToServer::class.java)
            this.item = itemObj
            binding.apply {
                txtDescripcion.text =
                    "${item?.vuelo?.numVuelo}  ${item?.vuelo?.origen}-${item?.vuelo?.destino}"
                txtFechaRev.text = "${item?.fechaCreacion}"
                txtOperadorName.text =
                    "Aplicador: ${item?.aplicador?.name} ${item?.aplicador?.apellidoPaterno}"
                txtSupervisorName.text =
                    "Oficial: ${item?.oficialOperaciones?.name} ${item?.oficialOperaciones?.apellidoPaterno}"

            }

        }
    }

}