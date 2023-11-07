package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemResponsableEstibaBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponsablesEstiba


class ResponsableEstibaAdapter(
    private var responsablesEstiba: ArrayList<ResponsablesEstiba>,
    private var listener: ResponsableEstibaClickListener
) :
    RecyclerView.Adapter<ResponsableEstibaAdapter.ViewHolder>() {

    //Context
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResponsableEstibaAdapter.ViewHolder {
        context = parent.context;
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_responsable_estiba, parent, false);
        return ViewHolder(view);
    }


    override fun onBindViewHolder(holder: ResponsableEstibaAdapter.ViewHolder, position: Int) {
        val responsableEstiba = responsablesEstiba[position]

        with(holder) {
            setListener(responsableEstiba)
            binding.tvNombreRespEstiba.text = responsableEstiba.nombre
            binding.tvNoEmpRespEstiba.text = responsableEstiba.noEmpleado
        }
    }

    override fun getItemCount(): Int {
        return responsablesEstiba.size;
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemResponsableEstibaBinding.bind(view)

        fun setListener(responsableEstiba: ResponsablesEstiba) {
            binding.btnBorrarResponsableEstiba.setOnClickListener {
                listener.onClick(responsableEstiba);
            }
        }
    }


    //Asignar lista
    fun setList(responsablesEstiba: ArrayList<ResponsablesEstiba>) {
        this.responsablesEstiba = responsablesEstiba;
        notifyDataSetChanged();
    }

}