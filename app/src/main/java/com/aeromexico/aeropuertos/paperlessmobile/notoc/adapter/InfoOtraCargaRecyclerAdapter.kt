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
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemInfoMercanciaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemOtraCargaBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Bultos
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.OtraCarga

class InfoOtraCargaRecyclerAdapter(private var infoNotoc: List<OtraCarga>,
                                   private var listaPosiciones: List<String>,
                                   var onSetPosicion:(posicion: String, i: Int) -> Unit,
                                   var setupRecyclerBultos:(recyclerView: RecyclerView) -> Unit,
                                   var setBultos:(bultos: List<Bultos>) -> Unit,
                                    var edit:Boolean)
    : RecyclerView.Adapter<InfoOtraCargaRecyclerAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_otra_carga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val infoMercancia = infoNotoc.get(position)

        with(holder){
            //setListener(infoMercancia,position)
            //binding.numGuia.text = infoMercancia.cargaEspecial.guiaAerea.toString()
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

            binding.tvGuiaAereaTitulo.text = infoMercancia.guiaAerea.toString()

            binding.tvGuiaAerea.text = infoMercancia.guiaAerea.toString()
            binding.tvContenidoDescripcion.text = infoMercancia.descripcion
            binding.tvInfoComplementaria.text = infoMercancia.informacionComplementaria.toString()
            binding.tvCodigo.text = infoMercancia.codigo.toString()
            binding.tvIdUld.text = infoMercancia.iDULD.toString()
            //llenar posiciones
            if (!edit)
                binding.tilPosicion.isEnabled = false
            setDropDown(listaPosiciones,position,onSetPosicion, infoMercancia.posicion)
            //llenar recycler de bultos
            setupRecyclerBultos.invoke(binding.recyclerView)
            setBultos.invoke(infoMercancia.bultos)

        }
    }

    override fun getItemCount(): Int = infoNotoc.size

    fun setInfo(vuelos: List<OtraCarga>, lista: List<String>){
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
        val binding = ItemOtraCargaBinding.bind(view)



        fun setDropDown(lista: List<String>, posicionArregloRecycler:Int, onSetPosicion:(posicion: String, i: Int) -> Unit, seleccionado:Int){
            val adapter= ArrayAdapter(context,R.layout.list_item,lista ?: listOf())
            var dropdown = (binding.tilPosicion.editText as? AutoCompleteTextView)
            dropdown?.setAdapter(adapter)
            dropdown?.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
                val value = adapter.getItem(i) ?: ""
                //Toast.makeText(context,value, Toast.LENGTH_SHORT).show()
                onSetPosicion.invoke(value,posicionArregloRecycler)
            }
            if (seleccionado > 0) {
                var seleccion = seleccionado - 1
                Log.i("Seleccionado",seleccion.toString())
                dropdown?.setText(adapter.getItem(seleccion))
            }
            /*(binding.tilPosicion.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.tilPosicion.editText as? AutoCompleteTextView)?.
            setOnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
                val value = adapter.getItem(i) ?: ""
                Toast.makeText(context,value, Toast.LENGTH_SHORT).show()
                onSetPosicion.invoke(value,posicionArregloRecycler)


            }*/
        }
    }
}