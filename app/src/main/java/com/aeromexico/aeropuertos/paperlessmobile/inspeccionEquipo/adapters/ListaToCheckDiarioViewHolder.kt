package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemCheckDiarioBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Preguntas

class ListaToCheckDiarioViewHolder(var binding: ItemCheckDiarioBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ListaToCheckDiarioViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCheckDiarioBinding.inflate(layoutInflater, parent, false)
            return ListaToCheckDiarioViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Preguntas,
        onclicAddPhotoHelper: (item: Preguntas, position: Int) -> Unit,
        onclicAddDateHelper: (item: Preguntas, position: Int) -> Unit,
        position: Int
    ) {

        // filtro para security
        if (item.titulo == "SECURITY") {
            binding.apply {
                titleBien.text = binding.root.context.getString(R.string.label_si)
                titleMal.text = binding.root.context.getString(R.string.label_no)
                titleNA.visibility = View.GONE
                checkNA.visibility = View.GONE
                btnAddPhoto.visibility = View.GONE
                btnAddObvservaciones.visibility = View.GONE
            }
        }
        if (item.titulo == "EXTINTOR") {
            binding.addDate.visibility = View.VISIBLE
            if(item.observation.toString().contains("#")){
                binding.addDate.text = "${binding.root.resources.getString(R.string.fecha_de_vencimiento)} ${item.observation.toString().substringBefore("#")}"
            }
        }
        // logic obvsercaiones edittext
        if (!item.observation.isNullOrEmpty()) {
            if(item.observation.toString().substringAfter("#") != "null"){
                binding.contentTextObservaciones.visibility = View.VISIBLE
                binding.textObservaciones.setText(item.observation.toString().substringAfter("#"))
            }

        }

        // datos por default
        binding.txtTiitle.text = item.titulo
        binding.textDescripcion.text = item.descripcion
        //buttons
        binding.btnAddObvservaciones.setOnClickListener {
            binding.contentTextObservaciones.visibility = View.VISIBLE
        }
        binding.addDate.setOnClickListener {
            item?.let { onclicAddDateHelper.invoke(item, position) }
        }
        binding.btnAddPhoto.setOnClickListener {
            item?.let { onclicAddPhotoHelper.invoke(item, position) }

            //   onclicAddPhotoHelper.onclicAddPhotoHelper(item, position)
        }
        //logic photo
        if (!item.photo.isNullOrEmpty()) {
            binding.photoPreview.visibility = View.VISIBLE

        }

        binding.textObservaciones.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(item.observation.toString().contains("#")){
                    item.observation = item.observation.toString().substringBefore("#") +"#"+ binding.textObservaciones.text.toString().trim()
                }else{
                    item.observation =  binding.textObservaciones.text.toString().trim()
                }

            }
        })

        //Checkbox logic  start
        if (item.estate != null) {
            println("item val " + item.estate)
            when (item.estate) {
                1 -> {
                    binding.checkBien.isChecked = true
                    binding.checkMal.isChecked = false
                    binding.checkNA.isChecked = false
                }
                2 -> {
                    binding.checkMal.isChecked = true
                    binding.checkBien.isChecked = false
                    binding.checkNA.isChecked = false
                }
                3 -> {
                    binding.checkNA.isChecked = true
                    binding.checkBien.isChecked = false
                    binding.checkMal.isChecked = false
                }
            }
        }

        binding.checkBien.setOnClickListener {
            if (binding.checkBien.isChecked) {
                item.estate = 1
            } else {
                item.estate = 0
            }

            if (item.estate != null) {
                println("item val " + item.estate)
                when (item.estate) {
                    1 -> {
                        binding.checkMal.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    2 -> {
                        binding.checkBien.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    3 -> {
                        binding.checkBien.isChecked = false
                        binding.checkMal.isChecked = false
                    }
                }
            }
        }
        binding.checkMal.setOnClickListener {
            if (binding.checkMal.isChecked) {
                item.estate = 2
            } else {
                item.estate = 0
            }
            if (item.estate != null) {
                println("item val " + item.estate)
                when (item.estate) {
                    1 -> {
                        binding.checkMal.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    2 -> {
                        binding.checkBien.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    3 -> {
                        binding.checkBien.isChecked = false
                        binding.checkMal.isChecked = false
                    }
                }
            }
        }
        binding.checkNA.setOnClickListener {
            if (binding.checkNA.isChecked) {
                item.estate = 3
            } else {
                item.estate = 0
            }
            if (item.estate != null) {
                println("item val " + item.estate)
                when (item.estate) {
                    1 -> {
                        binding.checkMal.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    2 -> {
                        binding.checkBien.isChecked = false
                        binding.checkNA.isChecked = false
                    }
                    3 -> {
                        binding.checkBien.isChecked = false
                        binding.checkMal.isChecked = false
                    }
                }
            }
        }
        //Checkbox logic  END


    }


}