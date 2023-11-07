package com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCondicionesMeteorologicasBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.gson.Gson

class CondicionesMeteorologicasFragment(val setScrollState: (ScrollState?) -> Unit): Fragment() {
    lateinit var binding:FragmentCondicionesMeteorologicasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCondicionesMeteorologicasBinding.inflate(layoutInflater)

        return binding.root
    }
    fun comprobacion(){
        var allOk = false

    }


}