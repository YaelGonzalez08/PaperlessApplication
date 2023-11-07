package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemPrimerVueloPendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.google.gson.Gson

class PrimerVueloDiaPendienteAdapter(
    private var lista: ArrayList<CheckPrimeVueloEntity>,
    val sendClick: (item_list: CheckPrimeVueloEntity) -> Unit,
    val deleteClick: (item_list: CheckPrimeVueloEntity) -> Unit
) : RecyclerView.Adapter<PrimerVueloDiaPendienteAdapter.AdapterViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val layOutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPrimerVueloPendienteBinding.inflate(layOutInflater, parent, false)
        return AdapterViewHolder(itemBinding, sendClick, deleteClick)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = lista[position]
        holder.bind(item)
    }

    override fun getItemCount() = lista.size

    fun updateList(newLista: ArrayList<CheckPrimeVueloEntity>) {
        lista = newLista
        notifyDataSetChanged()
    }

    class AdapterViewHolder(
        val itemBinding: ItemPrimerVueloPendienteBinding,
        val sendClick: (item_list: CheckPrimeVueloEntity) -> Unit,
        val deleteClick: (item_list: CheckPrimeVueloEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        var item: CheckPrimeVueloEntity? = null

        init {
            itemBinding.apply {
                contenedorEnviar.setOnClickListener {
                    item?.let{
                        sendClick.invoke(it) }
                }

                btnEnviar.setOnClickListener {
                    item?.let {
                        sendClick.invoke(it)
                    }
                }

                btnEliminar.setOnClickListener{
                    item?.let{
                        deleteClick.invoke(it)
                    }
                }
            }
        }

        fun bind(itemObj: CheckPrimeVueloEntity) {
            var item = Gson().fromJson(itemObj.request, RequestFirstFlightForm::class.java)

            this.item = itemObj
            itemBinding.apply {
                txtDescripcion.text = "ID Vuelo: ${item.flightReferenceNumber}"
                txtFechaRev.text = Fecha().stringToFecha((if (item.piloto.nombre.isNotEmpty()) item.piloto.fechaCreacion
                else if (item.cocinas.nombre.isNotEmpty()) {item.cocinas.fechaCreacion}
            else if (item.sobrecargo.nombre.isNotEmpty()) {item.sobrecargo.fechaCreacion}
            else if (item.oficial.nombre.isNotEmpty()) {item.oficial.fechaCreacion}
                        else "").toString())

            }
        }
    }

}