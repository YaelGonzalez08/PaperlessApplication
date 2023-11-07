package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs

import CuestionarioComunicado
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters.RetroPreguntasAdapter
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentRetroAlimentacionBinding

class RetroAlimentacionFragment(
    var cuestionarioComunicado: ArrayList<CuestionarioComunicado>,
    var getFolio: () -> Int
) :
    Fragment() {
    lateinit var adaptar: RetroPreguntasAdapter
    lateinit var binding: FragmentRetroAlimentacionBinding
    var folio = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRetroAlimentacionBinding.inflate(layoutInflater)
        adaptar = RetroPreguntasAdapter(cuestionarioComunicado)
        if(cuestionarioComunicado.isNullOrEmpty()){
            binding.msj.visibility = View.GONE
        }
        binding.recyclerQuestions.adapter = adaptar
        binding.root.visibility = View.GONE
        binding.btnRegresar.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.folio.text = "${resources.getString(R.string.folio)} ${getFolio.invoke()}"
        binding.root.visibility = View.VISIBLE
    }
}