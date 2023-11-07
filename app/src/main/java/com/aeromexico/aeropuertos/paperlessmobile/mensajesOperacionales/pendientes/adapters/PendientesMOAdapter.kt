package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemPendienteMensajeOperacionalBinding
import java.text.SimpleDateFormat
import java.util.*

class PendientesMOAdapter(private var pendientes: List<ModificarDetalleLirEntity>, private var listener: OnClickListenerMOPendientes):
    RecyclerView.Adapter<PendientesMOAdapter.ViewHolder>(){
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendientesMOAdapter.ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_pendiente_mensaje_operacional, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemPendienteMensajeOperacionalBinding.bind(view)

        fun setListener(pendiente: ModificarDetalleLirEntity){
            with(binding.root){

                setOnClickListener { listener.onClick(pendiente) }
                binding.cbBorrarPendMO.setOnClickListener {
                    listener.onDelete(pendiente)
                }
                /*setOnLongClickListener {
                    listener.onDelete(pendiente)
                    true
                }*/
            }
        }
    }

    override fun getItemCount(): Int  = pendientes. size

    fun setPendientes(pendientes: List<ModificarDetalleLirEntity>){
        this.pendientes = pendientes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PendientesMOAdapter.ViewHolder, position: Int) {
        val pendiente = pendientes.get(position)

        with(holder){
            setListener(pendiente)
            binding.tvNumVuelo.text = pendiente.NumVuelo.toString()
            binding.tvAerolinea.text = pendiente.Aerolinea.toString()
            binding.tvtextFechaVuelo.text = pendiente.FechaVueloLocal
            binding.tvOrigen.text = pendiente.Origen.toString()
            binding.tvDestino.text = pendiente.Destino.toString()
            binding.tvEquipo.text = pendiente.equipo.toString()
            binding.tvtMatricula.text = pendiente.matricula.toString()

        }
    }

    fun String.toDate(): () -> String = {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val formated = formattter.format(parser.parse(this))
        formated.toString()
    }
    fun stringToFecha(fechs: String): String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val formated = formattter.format(parser.parse(fechs))
        return formated
    }

    fun Date.toStringDate(): () -> String = {

        val pattern= "yyyy-MM-dd HH:mm"
        var simpleDateFormat= SimpleDateFormat(pattern)
        var date= simpleDateFormat.format(this)
        date.toString()

    }

}