package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemFotoInspecAeronaveBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemPendienteInspecAeronaveBinding


class FotoAdapter(private var fotosList: MutableList<String>, private var listener: FotoClickListener):
    RecyclerView.Adapter<FotoAdapter.ViewHolder>() {

    //Context
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoAdapter.ViewHolder {
        context = parent.context;
        val view = LayoutInflater.from(context).inflate(R.layout.item_foto_inspec_aeronave, parent, false);
        return ViewHolder(view);
    }


    override fun onBindViewHolder(holder: FotoAdapter.ViewHolder, position: Int) {
        val foto = fotosList[position]

        with(holder){
            setListener(foto)

            binding.tvFoto.text = "Imagen #"+ (fotosList.indexOf(foto)+1)

          /*  var ruta: String = inspeccion.Origen + " - " + inspeccion.Destino


            binding.tvtextFechaVuelo.text = inspeccion.FechaVuelo.substring(0,10).replace("-", "/")
            binding.tvNumVuelo.text = inspeccion.NumVuelo
            binding.tvOrigen.text = ruta
            binding.tvtMatricula.text = inspeccion.Matricula
            binding.tvEquipo.text = inspeccion.Equipo*/
        }
    }



    override fun getItemCount(): Int {
        return fotosList.size;
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemFotoInspecAeronaveBinding.bind(view)

        fun setListener(foto: String){
            binding.btnBorrarFoto.setOnClickListener{
                listener.onDelete(foto);
            }

            binding.btnVerFoto.setOnClickListener{
                listener.onView(foto)
            }
        }
    }


    //Asignar lista
    fun setList(fotos: MutableList<String>){
        this.fotosList = fotos;
        notifyDataSetChanged();
    }


}