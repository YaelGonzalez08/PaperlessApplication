package com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.showSelectHour
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInformacionServicioBinding
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar

class InformacionServicioFragment(val setScrollState: (ScrollState?) -> Unit) : Fragment(),
    ObservableScrollViewCallbacks {
    lateinit var binding :FragmentInformacionServicioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInformacionServicioBinding.inflate(inflater,container,false)
        init()

        return binding.root
    }

    fun comprobacion() : Boolean {
        var allOk = false

        binding.apply {

            if(tipoFluido.spinner.selectedItem.toString().isNullOrEmpty()){
                Snackbar.make(root,"Selecciona un tipo de  fluido", Snackbar.LENGTH_SHORT)
                    .show();
            }else if(editMezclaFluido.text.toString().isNullOrEmpty()){
                editMezclaFluido.error = "Ingresa un valor"
            }else if(textTimeHoraComienzo.text.toString().isNullOrEmpty()){
                textTimeHoraComienzo.error = getString(R.string.msj_selecciona_hora)
            }else if(textTimeHoraTermino.text.toString().isNullOrEmpty()){
                textTimeHoraTermino.error = "Selecciona una hora"
            }else if(editTemperaturaAmbiente.text.toString().isNullOrEmpty()){
                editTemperaturaAmbiente.error = "Ingresa un valor"
            }else if (spinnerTemperatura.spinner.selectedItem.toString().isNullOrEmpty()){
                Snackbar.make(root,"Selecciona los grados de la temperatura ambiente", Snackbar.LENGTH_SHORT)
                    .show();
            }else if(editTemperaturaCongelamiento.text.toString().isNullOrEmpty()){
                editTemperaturaCongelamiento.error = "Ingresa un valor"
            }else if(spinnerTemperaturaCongelamiento.spinner.selectedItem.toString().isNullOrEmpty()){
                Snackbar.make(root,"Selecciona los grados de la temperatura de congelamiento", Snackbar.LENGTH_SHORT)
                    .show();
            }else if(textHoldoverTime.text.toString().isNullOrEmpty()){
                textHoldoverTime.error = "Ingresa un valor"
            }else{
                allOk = true
            }
        }

        return allOk
    }

    private fun init() {

        binding.apply {
            scrollView.setScrollViewCallbacks(this@InformacionServicioFragment)
            tipoFluido.spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayOf("","I","II","III","IV"))
            spinnerTemperatura.spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayOf("","°C","°F"))
            spinnerTemperaturaCongelamiento.spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayOf("","°C","°F"))

            btnHoracomienzoSetTime.setOnClickListener {
                showSelectHour(textTimeHoraComienzo,parentFragmentManager)
            }
            textTimeHoraComienzo.setOnClickListener {
                showSelectHour(textTimeHoraComienzo,parentFragmentManager)
            }
            btnHoraterminoSetTime.setOnClickListener {
                showSelectHour(textTimeHoraTermino,parentFragmentManager)
            }
            textTimeHoraTermino.setOnClickListener {
                showSelectHour(textTimeHoraTermino,parentFragmentManager)
            }

        }


    }

    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {

    }

    override fun onDownMotionEvent() {

    }

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
        if(scrollState == ScrollState.DOWN){
            if (binding.scrollView.scrollY < 200) {
                setScrollState.invoke(scrollState)
            }
        }else{
            setScrollState.invoke(scrollState)
        }

    }

}