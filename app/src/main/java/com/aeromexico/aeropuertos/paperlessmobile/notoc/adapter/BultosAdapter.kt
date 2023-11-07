package com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemBultosBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Bultos

class BultosAdapter(private var infoBultos: List<Bultos>): RecyclerView.Adapter<BultosAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BultosAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_bultos, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BultosAdapter.ViewHolder, position: Int) {
        val bulto = infoBultos.get(position)
        with(holder){
            binding.cantBulto.text = bulto.cantidad.toString()
            binding.numBultos.text = bulto.numeroDeBultos.toString()
        }
    }

    override fun getItemCount(): Int = infoBultos.size

    fun setInfo(bultos: List<Bultos>){
        this.infoBultos = bultos
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemBultosBinding.bind(view)
    }
}