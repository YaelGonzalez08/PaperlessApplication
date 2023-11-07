package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCheckListConsentimientoTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore


class CheckListConsentimientoTab(val operador: UsuarioCore,val supervisor: UsuarioCore) : Fragment() {

    lateinit var binding : FragmentCheckListConsentimientoTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckListConsentimientoTabBinding.inflate(inflater,container,false)

        binding.txtNumOperador.text = operador.employeeNumber.toString()
        binding.txtNameOperador.text = "${operador.name} ${operador.apellidoMaterno}"

        binding.txtNumeroSupervisor.text = supervisor.employeeNumber.toString()
        binding.txtNombreSupervisor.text =  "${supervisor.name} ${supervisor.apellidoMaterno}"

        return binding.root
    }


}