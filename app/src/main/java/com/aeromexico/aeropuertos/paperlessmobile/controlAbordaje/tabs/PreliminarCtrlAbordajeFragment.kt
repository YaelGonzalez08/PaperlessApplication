package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.MediaCameraPictureDialog
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.TimePickerDIalogHelper
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.adapters.PasajerosAdapter
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.PasajerosQuitarCheckInRequest
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.Pax
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.repository.controlAbordajeRepository
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.viewmodel.ControlAbordajeViewModel
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentClientesCtrlAbordajeBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast

class PreliminarCtrlAbordajeFragment(var fligthReference: String) : Fragment() {
    lateinit var binding: FragmentClientesCtrlAbordajeBinding
    lateinit var model: ControlAbordajeViewModel
    lateinit var adapter: PasajerosAdapter
    lateinit var d: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientesCtrlAbordajeBinding.inflate(layoutInflater)
        model = ControlAbordajeViewModel(
            (CoreRepository(WebServiceApi().getCoreApi())),
            controlAbordajeRepository(WebServiceApi().getControlAbordajeApi())
        )
        adapter = PasajerosAdapter(
            arrayListOf(),
            ::onclicAddEvidenciaHelper,
            ::onclicCancelado,
            ::onclicConfirmar,false
        )
        binding.recyclerChecklist.adapter = adapter
        binding.btnUpdate.setOnClickListener {
            updatePasajeros()
        }
        updatePasajeros()

        return binding.root
    }

    private fun onclicConfirmar(pax: Pax, i: Int) {
        TimePickerDIalogHelper { time ->
            pax.HoraConfirmacion = time
            setChecinPax(pax)
        }.show(parentFragmentManager, "time")
        adapter.lista.removeAt(i)
        adapter.notifyDataSetChanged()
    }

    private fun setChecinPax(pax: Pax) {
        model.setChecinPax(
            PasajerosQuitarCheckInRequest(
                pax = pax,
                flightReferenceNumber = fligthReference
            )
        ).observe(viewLifecycleOwner, Observer {
            d = Dialogo(requireContext())
            if (it.error) {
                d.mostrarError(getString(R.string.ups), "${it.message.toString()}")
            } else {
                d.mostrarMensajeConfirmacion("Envio Exitoso", "${it.message.toString()}")
            }
            d.btnAceptar.setOnClickListener {
                d.Ocultar()
            }
            d.btnCerrar.setOnClickListener {
                d.Ocultar()
            }

        })
    }

    private fun onclicCancelado(pax: Pax, i: Int) {
        adapter.lista.removeAt(i)
        adapter.notifyDataSetChanged()
    }

    private fun onclicAddEvidenciaHelper(pax: Pax, i: Int) {
        // mandar aqui la peticion a la camara
        parentFragmentManager.let {
            MediaCameraPictureDialog() { foto ->
                // //remplazar adapter por bitmap o b64
                adapter.lista[i].evidencia = CreateImageFile.getB64FromBitmap(foto)
                adapter.addPhotoInItem(adapter.lista)
            }.show(it, "MediaCameraPictureDialog")
        }
    }

    private fun updatePasajeros() {
        d = Dialogo(requireContext())

        d.mostrarCargando(getString(R.string.cargando))
        model.getNotBoardedPasajeros(fligthReference).observe(viewLifecycleOwner, Observer {
            d.Ocultar()
            if(it!=null) {
                var resp = it
                if (resp !=null) {
                    resp.result?.pax.let { list ->
                        if (list!!.isEmpty()) {
                            adapter.updateList(arrayListOf())
                            showToast("${it.message}")
                        } else {
                            adapter.updateList(list)
                        }
                    }
                    resp.result.let {
                        binding.textJ.text = "J: ${resp.result.claseJ.toString()}"
                        binding.textY.text = "Y: ${resp.result.claseY.toString()}"
                    }

                } else{
                    showToast("${it.message}")
                    adapter.updateList(arrayListOf())
                }

            }
        })
    }
}