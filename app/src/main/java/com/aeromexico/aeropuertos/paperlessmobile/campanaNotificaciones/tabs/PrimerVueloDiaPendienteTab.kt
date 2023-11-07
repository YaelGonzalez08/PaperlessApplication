package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters.PrimerVueloDiaPendienteAdapter
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaPendienteTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.google.gson.Gson


class PrimerVueloDiaPendienteTab : Fragment() {

    lateinit var binding: FragmentPrimerVueloDiaPendienteTabBinding
    lateinit var model: PrimerVueloDiaViewModel
    var lista: ArrayList<CheckPrimeVueloEntity> = arrayListOf()
    lateinit var adapter: PrimerVueloDiaPendienteAdapter
    lateinit var dialogo: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrimerVueloDiaPendienteTabBinding.inflate(inflater, container, false)
        model = PrimerVueloDiaViewModel()
        adapter = PrimerVueloDiaPendienteAdapter(lista, ::btnEnviar, ::clickDeleteFromBD)
        dialogo = Dialogo(requireContext())

        binding.apply {
            sinPendientes.textSinpendientes.text = ""
            recyclerPrimerVuelo.adapter = adapter
            model.getAllForms().observe(viewLifecycleOwner, {
                lista.clear()
                it.forEach { m -> lista.add(m) }

                if(!lista.isNullOrEmpty()){
                    adapter.updateList(lista)
                    sinPendientes.root.visibility = View.GONE
                } else {
                    sinPendientes.apply {
                        textSinpendientes.text = "No hay formatos de Primer Vuelo del Día pendientes a enviar."
                        root.visibility = View.VISIBLE
                    }
                }
            })
        }
        return binding.root
    }

    fun btnEnviar(primerVueloEntity: CheckPrimeVueloEntity) {
        dialogo = Dialogo(requireContext())
        dialogo.mostrarCargando("Espere")
        model.insertPrimerVuelo(Gson().fromJson(primerVueloEntity.request, RequestFirstFlightForm::class.java)).observe(viewLifecycleOwner, { response ->
            if(response != null) {
                dialogo.Ocultar()
                response.let {
                    dialogo = Dialogo((requireContext()))
                    dialogo.mostrarMensajeConfirmacion("Envio Exitoso",
                        "Se envio correctamente folio: ${it?.result.primerVuelo.idPrimerVuelo}")
                    dialogo.btnAceptar.visibility = View.VISIBLE
                    dialogo.btnCerrar.visibility = View.GONE
                    dialogo.btnAceptar.setOnClickListener{ dialogo.Ocultar()}
                    model.deletePtimerVueloCheckById(primerVueloEntity.id)
                }
            } else {
                dialogo.Ocultar()
                dialogo = Dialogo(requireContext())
                dialogo.mostrarError(
                    "Error de Conexión ",
                    "Ocurrio un Error Intenta mas tarde !"
                )
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()

                }
            }
        })
    }

    fun clickDeleteFromBD(primerVueloEntity: CheckPrimeVueloEntity) {
        dialogo = Dialogo(requireContext())
        dialogo.mostrarPregunta(
            "¡Atencion!",
            "Aun no se ha enviado, ¿Estas Seguro de ELIMINAR este ID Vuelo?, No se enviara ningun cambio,  y no podras revertir esto"
        )
        dialogo.btnCerrar.setOnClickListener {
            dialogo.Ocultar()
        }
        dialogo.btnAceptar.setOnClickListener {
            dialogo.Ocultar()
            model.deletePtimerVueloCheckById(primerVueloEntity.id)

        }
    }
}