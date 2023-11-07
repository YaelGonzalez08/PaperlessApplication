package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.EmpleadoInfo
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.InsertInfoOficial
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.repository.controlAbordajeRepository
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.viewmodel.ControlAbordajeViewModel
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentOficialCtrlAbordajeBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast
import java.util.*

class OficialCtrlAbordajeFragment(var fligthReference: String, var continuebtn: () -> Unit) :
    Fragment() {
    lateinit var binding: FragmentOficialCtrlAbordajeBinding
    lateinit var model: ControlAbordajeViewModel
    var oficinalOperaciones: UsuarioCore? = null
    var ll: UsuarioCore? = null
    var firmaoficinalOperaciones: String? = null
    var firmaLL: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOficialCtrlAbordajeBinding.inflate(layoutInflater)
        model = ControlAbordajeViewModel(
            (CoreRepository(WebServiceApi().getCoreApi())),
            controlAbordajeRepository(WebServiceApi().getControlAbordajeApi())
        )

        model.getEmployeeInfo(fligthReference).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.result != null) {

                    if(it.result.empleadoInfo?.oficial != null){
                        CargarDatos(it.result.empleadoInfo)
                    }else{
                        DarFunciones()
                    }

                } else {
                    DarFunciones()
                }

            }else {
                DarFunciones()
            }
        })

        return binding.root
    }

    private fun DarFunciones() {

        // showAeromexicoOficial()
        binding.apply {
            datosOficialOperaciones.btnConsultarEmpleado.setOnClickListener {
                if (datosOficialOperaciones.txtNumEmpleado.text.toString().isNotEmpty()) {
                    getOficial(datosOficialOperaciones.txtNumEmpleado.text.toString())
                } else {
                    datosOficialOperaciones.txtNumEmpleado.error =
                        getString(R.string.error_campo_invalido)
                }
            }
            datosOficialOperaciones.btnFirma.setOnClickListener {
                getFirmaOficial()
            }
            datosOficialOperaciones.ivFirma.setOnClickListener {
                getFirmaOficial()
            }
            datosOficialOperaciones.swEmpExterno.setOnClickListener {
                if (datosOficialOperaciones.swEmpExterno.isChecked)
                    showNoAeromexicoOficial()
                else
                    showAeromexicoOficial()
            }
            tvHoraTecnica.setOnClickListener {
                showSelectHour(tvHoraTecnica, parentFragmentManager)
            }
            tvHoraServicio.setOnClickListener {
                showSelectHour(tvHoraServicio, parentFragmentManager)
            }
            tvHoraSoliProcSeg.setOnClickListener {
                showSelectHour(tvHoraSoliProcSeg, parentFragmentManager)
            }
            tvHoraEntregaEquipaje.setOnClickListener {
                showSelectHour(tvHoraEntregaEquipaje, parentFragmentManager)
                activarLL()
            }
            tvHoraRecepcionEquipaje.setOnClickListener {
                showSelectHour(tvHoraRecepcionEquipaje, parentFragmentManager)
                activarLL()
            }
            mbEnviar.setOnClickListener {
                subirDatos()
            }


        }
    }

    private fun subirDatos() {
        binding.apply {
            if (tvHoraTecnica.text.toString().contains(getString(R.string.msj_selecciona_hora))) {
                showToast("${getString(R.string.msj_selecciona_hora)} en Técnica")
            } else if (tvHoraServicio.text.toString()
                    .contains(getString(R.string.msj_selecciona_hora))
            ) {
                showToast("${getString(R.string.msj_selecciona_hora)} en Servicio")
            } else if (tvHoraSoliProcSeg.text.toString()
                    .contains(getString(R.string.msj_selecciona_hora))
            ) {
                showToast("${getString(R.string.msj_selecciona_hora)} en Tiempo")
            } else if ((tvHoraEntregaEquipaje.text.toString() != getString(R.string.msj_selecciona_hora) ||
                        tvHoraRecepcionEquipaje.text.toString() != getString(R.string.msj_selecciona_hora)) &&
                datosLL.txtNumEmpleado.text.toString().isNullOrEmpty()
            ) {
                datosLL.txtNumEmpleado.error = "Campo invalido"
            } else if ((tvHoraEntregaEquipaje.text.toString() != getString(R.string.msj_selecciona_hora) ||
                        tvHoraRecepcionEquipaje.text.toString() != getString(R.string.msj_selecciona_hora)) &&
                datosLL.actvOficialNombre.text.toString().isNullOrEmpty()
            ) {
                datosLL.actvOficialNombre.error = "Campo invalido"
            } else if ((tvHoraEntregaEquipaje.text.toString() != getString(R.string.msj_selecciona_hora) ||
                        tvHoraRecepcionEquipaje.text.toString() != getString(R.string.msj_selecciona_hora)) &&
                firmaLL.isNullOrEmpty()
            ) {
                showToast("Firma LL es requerida")
            } else if (datosOficialOperaciones.txtNumEmpleado.text.toString().isNullOrEmpty()) {
                datosOficialOperaciones.txtNumEmpleado.error = "Campo invalido"
            } else if (datosOficialOperaciones.actvOficialNombre.text.toString().isNullOrEmpty()) {
                datosOficialOperaciones.actvOficialNombre.error = "Campo invalido"
            } else if (firmaoficinalOperaciones.isNullOrEmpty()) {
                showToast("Firma Oficial es requerida")
            } else {
                var datos: InsertInfoOficial = InsertInfoOficial(
                    creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!,
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance()),
                    flightReferenceNumber = fligthReference,
                    tecnica = tvHoraTecnica.text.toString(),
                    servicio = tvHoraServicio.text.toString(),
                    solicitudProcedimientoSeguridad = tvHoraSoliProcSeg.text.toString(),
                    entregaEquipajeLL = getentregaLL(),
                    recepcionEquipajeLL = getRecepcionLL(),
                    lL = datosLL.actvOficialNombre.text.toString() ?: "",
                    numeroLL = datosLL.txtNumEmpleado.text.toString() ?: "",
                    oficial = datosOficialOperaciones.actvOficialNombre.text.toString() ?: "",
                    numeroOficial = datosOficialOperaciones.txtNumEmpleado.text.toString() ?: "",
                    observacionesOficial = etRemarks.text.toString() ?: "",
                    firmaB64LL = firmaLL.toString() ?: "",
                    firmaB64Oficial = firmaoficinalOperaciones.toString()
                )
                if (!datosLL.swEmpExterno.isChecked) {
                    datos.numeroLL = "AM${datosLL.txtNumEmpleado.text.toString() ?: ""}"
                }
                if (!datosOficialOperaciones.swEmpExterno.isChecked) {
                    datos.numeroOficial =
                        "AM${datosOficialOperaciones.txtNumEmpleado.text.toString() ?: ""}"
                }

                showToast("Enviando .. ")

                isertOficial(datos)
            }

        }


    }

    private fun isertOficial(datos: InsertInfoOficial) {
        var d = Dialogo(requireContext())
        d.mostrarCargando(getString(R.string.cargando))
        model.InsertInfoOficial(datos).observe(viewLifecycleOwner, Observer {
            d.Ocultar()
            if (it != null) {
                d.mostrarMensajeConfirmacion("¡Enviado!", it.message)
                d.btnAceptar.setOnClickListener {
                    d.Ocultar()
                    continuebtn.invoke()
                }
                d.btnCerrar.setOnClickListener {
                    d.Ocultar()
                    continuebtn.invoke()
                }
            }
        })
    }

    private fun getRecepcionLL(): String {
        if (binding.tvHoraRecepcionEquipaje.text.toString()
                .contains(getString(R.string.msj_selecciona_hora))
        ) {
            return ""
        } else {
            return binding.tvHoraRecepcionEquipaje.text.toString()
        }
    }

    private fun getentregaLL(): String {
        if (binding.tvHoraEntregaEquipaje.text.toString()
                .contains(getString(R.string.msj_selecciona_hora))
        ) {
            return ""
        } else {
            return binding.tvHoraEntregaEquipaje.text.toString()
        }

    }

    private fun CargarDatos(empleado: EmpleadoInfo) {
        binding.apply {
            mbEnviar.setOnClickListener {
                continuebtn.invoke()
            }

            empleado.apply {
                tecnica.let { tvHoraTecnica.text = it }
                servicio.let { tvHoraServicio.text = it }
                solicitudProcedimientoSeguridad.let { tvHoraSoliProcSeg.text = it }

                if (lL != null) {
                    datosLL.txtTitulo.text = "Responsable LL"
                    datosLL.root.isVisible = true
                    datosLL.txtNumEmpleado.isEnabled = false
                    datosLL.tilNumEmpleado.isVisible = true
                    datosLL.txtNumEmpleado.setText(numeroLL.toString())
                    entregaEquipajeLL.let { tvHoraEntregaEquipaje.text = it }
                    recepcionEquipajeLL.let { tvHoraRecepcionEquipaje.text = it }
                }
                if (numeroLL != null) {
                    datosLL.root.isVisible = true
                    datosLL.tilNombreEmpleadoManual.isVisible = true
                    datosLL.swEmpExterno.isVisible = false
                    datosLL.actvOficialNombre.isEnabled = false
                    datosLL.actvOficialNombre.setText(lL.toString())
                }
                observacionesOficial.let { etRemarks.setText(it) }

                numeroOficial.let {
                    datosOficialOperaciones.root.isVisible = true
                    datosOficialOperaciones.swEmpExterno.isVisible = false
                    datosOficialOperaciones.txtNumEmpleado.isEnabled = false
                    datosOficialOperaciones.tilNumEmpleado.isVisible = true
                    datosOficialOperaciones.txtNumEmpleado.setText(it.toString())
                }
                oficial.let {
                    datosOficialOperaciones.root.isVisible = true
                    datosOficialOperaciones.tilNombreEmpleadoManual.isVisible = true
                    datosOficialOperaciones.actvOficialNombre.isEnabled = false
                    datosOficialOperaciones.actvOficialNombre.setText(it.toString())
                }


            }
        }
    }

    private fun showSelectHour(view: TextView, parentFragmentManager: FragmentManager) {
        val timePicker = TimePickerDIalogHelper { onTimeSelected(it, view) }
        timePicker.show(parentFragmentManager, "time")
    }

    private fun onTimeSelected(str: String, view: TextView) {
        view.setText(str)
    }

    private fun getFirmaOficial() {
        binding.apply {
            parentFragmentManager.let {
                DialogFragmentSignature() { bitmap ->
                    datosOficialOperaciones.ivFirma.setImageBitmap(bitmap)
                    firmaoficinalOperaciones = CreateImageFile.getB64FromBitmap(bitmap)
                    mbEnviar.apply {
                        visibility = View.VISIBLE
                        isEnabled = true
                    }
                }.show(it, "SignatureFragment")
            }
        }
    }

    private fun getOficial(numEmpleado: String) {
        model.getEmpleadoAM(numEmpleado).observe(viewLifecycleOwner, Observer {
            it.let {
                binding.apply {
                    datosOficialOperaciones.apply {
                        oficinalOperaciones = it.result.usuarioCore
                        ivFirma.visibility = View.VISIBLE
                        btnFirma.visibility = View.VISIBLE
                        tilNombreEmpleadoManual.visibility = View.VISIBLE
                        mbEnviar.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                        actvOficialNombre.setText("${oficinalOperaciones?.name} ${oficinalOperaciones?.apellidoPaterno} ${oficinalOperaciones?.apellidoMaterno}")
                    }
                }
            }
        })
    }

    private fun showAeromexicoOficial() {
        binding.apply {
            datosOficialOperaciones.apply {
                tilNombreEmpleadoManual.visibility = View.GONE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.VISIBLE

                unableSignatureImageView(false, ivFirma)
                ivFirma.visibility = View.GONE

                btnFirma.apply {
                    visibility = View.GONE
                    isEnabled = true
                }
                actvOficialNombre.apply {
                    isEnabled = false
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = "AM"
                }
            }
        }

        oficinalOperaciones = null
        firmaoficinalOperaciones = null
    }

    private fun showNoAeromexicoOficial() {
        binding.apply {
            datosOficialOperaciones.apply {
                tilNombreEmpleadoManual.visibility = View.VISIBLE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.GONE

                unableSignatureImageView(false, ivFirma)

                btnFirma.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                actvOficialNombre.apply {
                    isEnabled = true
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = ""
                }
            }
        }

        oficinalOperaciones = null
        firmaoficinalOperaciones = null
    }

    private fun activarLL() {
        binding.apply {
            datosLL.root.isVisible = true
            datosLL.txtTitulo.text = "Responsable LL"

            datosLL.btnConsultarEmpleado.setOnClickListener {
                if (datosLL.txtNumEmpleado.text.toString().isNotEmpty()) {
                    getLL(datosLL.txtNumEmpleado.text.toString())
                } else {
                    datosLL.txtNumEmpleado.error = getString(R.string.error_campo_invalido)
                }
            }
            datosLL.btnFirma.setOnClickListener {
                getFirmaLL()
            }
            datosLL.ivFirma.setOnClickListener {
                getFirmaLL()
            }
            datosLL.swEmpExterno.setOnClickListener {
                if (datosLL.swEmpExterno.isChecked)
                    showNoAeromexicoLL()
                else
                    showAeromexicoLL()
            }
        }

    }

    private fun showAeromexicoLL() {
        binding.apply {
            datosLL.apply {
                tilNombreEmpleadoManual.visibility = View.GONE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.VISIBLE

                unableSignatureImageView(false, ivFirma)
                ivFirma.visibility = View.GONE

                btnFirma.apply {
                    visibility = View.GONE
                    isEnabled = true
                }
                actvOficialNombre.apply {
                    isEnabled = false
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = "AM"
                }
            }
        }

        ll = null
        firmaLL = null
    }

    private fun showNoAeromexicoLL() {
        binding.apply {
            datosLL.apply {
                tilNombreEmpleadoManual.visibility = View.VISIBLE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.GONE

                unableSignatureImageView(false, ivFirma)

                btnFirma.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                actvOficialNombre.apply {
                    isEnabled = true
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = ""
                }
            }
        }

        ll = null
        firmaLL = null
    }

    private fun getFirmaLL() {
        binding.apply {
            parentFragmentManager.let {
                DialogFragmentSignature() { bitmap ->
                    datosLL.ivFirma.setImageBitmap(bitmap)
                    firmaLL = CreateImageFile.getB64FromBitmap(bitmap)
                }.show(it, "SignatureFragment")
            }
        }

    }

    private fun getLL(empleadoLL: String) {
        model.getEmpleadoAM(empleadoLL).observe(viewLifecycleOwner, Observer {
            it.let {
                binding.apply {
                    datosLL.apply {
                        ll = it.result.usuarioCore
                        ivFirma.visibility = View.VISIBLE
                        btnFirma.visibility = View.VISIBLE
                        tilNombreEmpleadoManual.visibility = View.VISIBLE
                        actvOficialNombre.setText("${ll?.name} ${ll?.apellidoPaterno} ${ll?.apellidoMaterno}")
                    }
                }
            }
        })
    }

}