package com.aeromexico.aeropuertos.paperlessmobile.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemVueloBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.google.gson.internal.bind.util.ISO8601Utils.format
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PaperlessAdapter(private var vuelos: List<Vuelos>, private var listener: OnClickListener) :
        RecyclerView.Adapter<PaperlessAdapter.ViewHolder>(){
            private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaperlessAdapter.ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_vuelo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vuelo = vuelos.get(position)

        with(holder){
            setListener(vuelo)
            /*
            if(position%2 == 0){
                binding.ContentItemVuelo.setBackgroundResource(R.color.color_gray_white_background_checklist)
            }*/
            binding.tvNumVuelo.text = vuelo.numVuelo.toString()
            binding.tvAerolinea.text = vuelo.aerolinea.toString()
            binding.tvtextFechaVuelo.text = stringToFechaMinutos(vuelo.fechaVueloLocal)
            binding.tvOrigen.text = vuelo.origen.toString() + " - " + vuelo.destino.toString()
            //binding.tvDestino.text = vuelo.destino.toString()
            binding.tvETA.text = stringToFechaMinutos(vuelo.eTA)
            binding.tvETD.text = stringToFechaMinutos(vuelo.eTD)
            binding.tvEquipo.text = vuelo.equipo.toString()
            binding.tvtMatricula.text = vuelo.matricula.toString()

        }
    }

    fun String.toDate(): () -> String = {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val formated = formattter.format(parser.parse(this))
        formated.toString()
    }
   open fun stringToFecha(fechs: String): String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattter = SimpleDateFormat("dd/MM/yyyy")
        val formated = formattter.format(parser.parse(fechs))
        return formated
    }

    fun stringToFechaMinutos(fechs: String): String{
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


    
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemVueloBinding.bind(view)
        
        fun setListener(flightEntity: Vuelos){
            with(binding.root){

                setOnClickListener { listener.onClick(flightEntity) }
            }
        }
    }

    override fun getItemCount(): Int  = vuelos. size

    fun setFlights(vuelos: List<Vuelos>){
        this.vuelos = vuelos
        notifyDataSetChanged()
    }
    fun cleanFlightList() {
        this.vuelos = mutableListOf()
        notifyDataSetChanged()
    }
}