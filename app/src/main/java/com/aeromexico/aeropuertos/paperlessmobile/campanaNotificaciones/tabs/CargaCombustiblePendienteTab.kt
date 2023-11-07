package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters.CombustibleCargaPendienteAdapter
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCargaCombustiblePendienteBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.viewModel.OrdenCargaViewModel
import com.google.gson.Gson
import ng.softcom.android.utils.ui.showToast


class CargaCombustiblePendienteTab : Fragment() {
    lateinit var binding: FragmentCargaCombustiblePendienteBinding
    lateinit var model: OrdenCargaViewModel
    var lista: ArrayList<CargacombustibleEntity> = arrayListOf()
    lateinit var adapter: CombustibleCargaPendienteAdapter
    lateinit var dialogo: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCargaCombustiblePendienteBinding.inflate(layoutInflater)
        model = OrdenCargaViewModel()
        adapter = CombustibleCargaPendienteAdapter(lista, ::btnEnviar, ::clickDeleteFromBD)
        dialogo = Dialogo(requireContext())
        binding.apply {
            msjSinPendientes.textSinpendientes.text = "Hlis"
            recyclerDeshielo.adapter = this@CargaCombustiblePendienteTab.adapter
        }

        model.allCargaCombustible.observe(viewLifecycleOwner, Observer {
            lista.clear()

            for (c in it) {
                lista.add(c)
            }

            if (!lista.isNullOrEmpty()) {
                adapter.updateList(lista)
                //showToast("${it.size}->> in ORDERNNES BD")
                binding.msjSinPendientes.root.visibility = View.GONE
            } else {
                binding.msjSinPendientes.textSinpendientes.text =
                    "No hay Ordenes de carga de Combustible Pendientes a Enviar"
                binding.msjSinPendientes.root.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    private fun btnEnviar(cargacombustibleEntity: CargacombustibleEntity) {

        model.insertarOrdenCarga(Gson().fromJson(cargacombustibleEntity.request,RequestOrdenCarga::class.java)).observe(viewLifecycleOwner,{
            reponse->
            if (reponse != null) {
                dialogo.Ocultar()
                reponse.result.let {
                    dialogo = Dialogo(requireContext())
                    dialogo.mostrarMensajeConfirmacion(
                        "Envio Exitoso",
                        "Se envio correctamente folio: ${it?.ordenCarga?.idOrdenCarga}"
                    )
                    dialogo.btnAceptar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                    model.deleteCargaCombustiblecheckById(cargacombustibleEntity.id)
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

    private fun clickDeleteFromBD(cargacombustibleEntity: CargacombustibleEntity) {
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
            model.deleteCargaCombustiblecheckById(cargacombustibleEntity.id)

        }
    }

}