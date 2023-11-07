package com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemInfoMercanciaBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.CatPosicion
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.InformacionMercancia
import java.text.SimpleDateFormat
import java.util.*

class InfoMercanciaRecyclerAdapter(private var infoNotoc: List<InformacionMercancia>,
                                   private var listaPosiciones: List<String>,
                                   var onSetPosicion:(posicion: String, i: Int) -> Unit,
                                   var edit: Boolean,)
    : RecyclerView.Adapter<InfoMercanciaRecyclerAdapter.ViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfoMercanciaRecyclerAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_info_mercancia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoMercanciaRecyclerAdapter.ViewHolder, position: Int) {
        val infoMercancia = infoNotoc.get(position)

        with(holder){
            //setListener(infoMercancia,position)
            binding.btnDown.setOnClickListener {
                if(binding.contenedorDown.visibility == View.GONE){

                    binding.contenedorDown.visibility = View.VISIBLE
                    binding.contenedorHead.visibility = View.GONE
                }
                else{
                    binding.contenedorDown.visibility = View.GONE
                    binding.contenedorHead.visibility = View.VISIBLE
                }
            }
            binding.btnDownIn.setOnClickListener {
                if(binding.contenedorDown.visibility == View.VISIBLE){
                    binding.contenedorDown.visibility = View.GONE
                    binding.contenedorHead.visibility = View.VISIBLE
                }
                else{
                    binding.contenedorDown.visibility = View.VISIBLE
                    binding.contenedorHead.visibility = View.GONE
                }
            }
            binding.tvGuiaAereaTitulo.text = infoMercancia.numeroGuiaAerea.toString()

            binding.tvGuiaAerea.text = infoMercancia.numeroGuiaAerea.toString()
            binding.tvNombreApropiadoExpedicion.text = infoMercancia.nombreExpedicion
            binding.tvClaseODivision.text = infoMercancia.clave.toString()
            binding.tvOnuId.text = infoMercancia.oNUID.toString()
            binding.tvRiesgoSecundario.text = infoMercancia.peligroSecundario
            binding.tvCantidadBultos.text = infoMercancia.cantidadBultos.toString()
            binding.tvGrupoEmbalaje.text = infoMercancia.grupoEmbalaje.toString()
            binding.tvCodigo.text = infoMercancia.codigo
            binding.tvCRE.text = infoMercancia.cRE.toString()

            //radiactivos
            binding.tvCantidadNetaIndiceTransporte.text = infoMercancia.cantidadNeta.toString()
            binding.tvCategoria.text = infoMercancia.categoria

            //estibado
            binding.tvIdUld.text = infoMercancia.iDULD.toString()
            if (!edit) {
                binding.tilPosicion.isEnabled = false

            }
            setDropDown(listaPosiciones,position,onSetPosicion,infoMercancia.posicion)
        }
    }

    override fun getItemCount(): Int = infoNotoc.size


    fun setInfo(vuelos: List<InformacionMercancia>, lista: List<String> ){
        this.infoNotoc = vuelos
        listaPosiciones = lista
        notifyDataSetChanged()
    }

    fun setEditable(editable:Boolean){
        this.edit = editable
        notifyDataSetChanged()
    }

    fun cleanInfoList() {
        this.infoNotoc = mutableListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemInfoMercanciaBinding.bind(view)


        /*fun setListener(flightEntity: infoMercancia, position: Int){
            with(binding.root){

                setOnClickListener { listener.onClick(flightEntity,position) }
            }
        }*/

        fun setDropDown(lista: List<String>, posicionArregloRecycler:Int, onSetPosicion:(posicion: String, i: Int) -> Unit, seleccionado:Int){

            val adapter= ArrayAdapter(context,R.layout.list_item,lista ?: listOf())
            var dropdown = (binding.tilPosicion.editText as? AutoCompleteTextView)
            dropdown?.setAdapter(adapter)
            dropdown?.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
                val value = adapter.getItem(i) ?: ""
                //Toast.makeText(context,value, Toast.LENGTH_SHORT).show()
                onSetPosicion.invoke(value,posicionArregloRecycler)
            }
            /*(binding.tilPosicion.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.tilPosicion.editText as? AutoCompleteTextView)?.
            setOnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
                val value = adapter.getItem(i) ?: ""
                Toast.makeText(context,value, Toast.LENGTH_SHORT).show()
                onSetPosicion.invoke(value,posicionArregloRecycler)


            }*/

            //Toast.makeText(context,"Seleccionado ${seleccionado}",Toast.LENGTH_SHORT).show()
            if (seleccionado > 0) {
                    var seleccion = seleccionado - 1
                Log.i("Seleccionado",seleccion.toString())
                dropdown?.setText(adapter.getItem(seleccion))
//                dropdown?.setSelection(seleccion)
            }
        }
    }
}