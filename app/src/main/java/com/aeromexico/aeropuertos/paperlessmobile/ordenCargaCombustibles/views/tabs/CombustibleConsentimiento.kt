package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.views.tabs

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCombustibleConsentimientoBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.google.android.material.snackbar.Snackbar
import ng.softcom.android.utils.ui.showSnackBar
import java.util.*


class CombustibleConsentimiento(
    val vuelo: Vuelos?,
    var enviarForm: () -> Unit,
    var rqOrdenCarga: RequestOrdenCarga
) : Fragment(), View.OnClickListener {

    lateinit var arrayUno: List<String>
    lateinit var arrayDos: List<String>
    lateinit var secccionMecanico: List<View>
    lateinit var model: PrimerVueloDiaViewModel
    var buttonOficialState = true
    var buttonMecanicoState = true
    var c = Calendar.getInstance()
    lateinit var binding: FragmentCombustibleConsentimientoBinding
    var firmaMecanico = ""
    var firmaOperaciones = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCombustibleConsentimientoBinding.inflate(layoutInflater)
        arrayUno = arrayListOf("Solicitud del capitan por", "Error en la carga de combustible", "Mantenimiento", "Operaciones")
        arrayDos = arrayListOf("Desvios ruta / altitud", "WX ruta / deshielo / alterno", "Afectaciones CTA / aeropuertos")
        model = PrimerVueloDiaViewModel()

        if(rqOrdenCarga != null){
            if (rqOrdenCarga.isPendingToSend || rqOrdenCarga.isForService){
                mostrarDatos()

            }else{
                inicializarNormal()
            }
        }else{
            inicializarNormal()

        }

        return binding.root
    }

    private fun mostrarDatos() {
            binding.apply {
                btnConsultarOficial.isVisible = false
                switchCompat.isVisible = false
                ivFirmaOficial.isVisible = false
                btnFirmar.isVisible = false
                switchCompatMecanico.isVisible = false
                btnConsultarMecanico.isVisible = false
                ivFirmaMecanico.visibility = View.INVISIBLE
                btnFirmarMecanico.isVisible = false
                tilUno.isVisible = false
                btnFirmarHeaderMecanico.isVisible = false
                btnFirmarHeaderOficial.isVisible = false
                mecanicoExternoLabel.isVisible = false
                tilExterno.isVisible = false
                tilNumeroMecanico.prefixText = ""
                tilNumeroOficial.prefixText = ""
                //textView23

                actvNumeroOficial.setText(rqOrdenCarga.noEmpleadoOficialOperaciones)
                actvNumeroOficial.isEnabled = false
                tvNombreOficial.setText(rqOrdenCarga.nombreOficialOperaciones)

                etObservacionesValue.setText(rqOrdenCarga.observacionesOficial)
                etObservacionesValue.isEnabled = false

                //gone mecanico
                if(rqOrdenCarga.noEmpleadoMecanico.isNullOrEmpty()){
                    tvSwitchMecanicoHeader.visibility = View.GONE
                    mecanicoExternoLabel.visibility = View.GONE
                    switchCompatMecanico.visibility = View.GONE
                    tilNumeroMecanico.visibility = View.GONE
                    tilNombreMecanicoManual.visibility = View.GONE
                    ivFirmaMecanico.visibility = View.GONE
                    tilMecanicoHour.visibility = View.GONE
                    btnFirmarMecanico.visibility = View.GONE
                    tilObservacionesMecanico.visibility = View.GONE

                }
                actvNumeroMecanico.setText(rqOrdenCarga.noEmpleadoMecanico)
                actvNumeroMecanico.isEnabled = false

                tvNombreMecanico.setText(rqOrdenCarga.nombreMecanico)

                if (!rqOrdenCarga.firmaB64Mecanico.isNullOrEmpty())
                    firmaMecanico = rqOrdenCarga.firmaB64Mecanico

                if(!rqOrdenCarga.firmaB64Operaciones.isNullOrEmpty())
                    firmaOperaciones = rqOrdenCarga.firmaB64Operaciones

                etObservacionesMecanicoValue.setText(rqOrdenCarga.observacionesMecanico)
                etObservacionesMecanicoValue.isEnabled = false

                textView23.setText(rqOrdenCarga.extraFuel)
                if(!rqOrdenCarga.horaLlegadaMecanico.isNullOrEmpty())
                tietMecanicoHour.setText(rqOrdenCarga.horaLlegadaMecanico.substringAfter(" "))

                btnEnviarForm.isVisible = rqOrdenCarga.veces<5

                btnEnviarForm.setOnClickListener {

                    if(!vuelo?.companiaEquipo.equals("5D") && tietMecanicoHour.text.isNullOrEmpty())
                        showSnackBar(binding.root,"Falta hora llegada")
                    else
                    enviarForm.invoke()
                }
            }
    }

    private fun inicializarNormal() {
        binding.apply {
            btnEnviarForm.setOnClickListener {
                if(!vuelo?.companiaEquipo.equals("5D") && tietMecanicoHour.text.isNullOrEmpty())
                    showSnackBar(binding.root,"Falta hora llegada")
                else
                enviarForm.invoke()
            }
            secccionMecanico = listOf(tilMecanicoHour, btnFirmarHeaderMecanico, tvSwitchMecanicoHeader, switchCompatMecanico, containerMecanico, btnConsultarMecanico, tvNombreMecanico, ivFirmaMecanico, btnFirmarMecanico, tilObservacionesMecanico, mecanicoExternoLabel)

            tilNumeroOficial.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
            }
            tilNumeroMecanico.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
            }
            spinUno.setOnClickListener {
                configSpinners()
            }
            configSpinners()

            btnFirmar.setOnClickListener(this@CombustibleConsentimiento)
            btnFirmarMecanico.setOnClickListener(this@CombustibleConsentimiento)
            switchCompat.setOnClickListener(this@CombustibleConsentimiento)
            switchCompatMecanico.setOnClickListener(this@CombustibleConsentimiento)
            btnConsultarOficial.setOnClickListener(this@CombustibleConsentimiento)
            btnConsultarMecanico.setOnClickListener(this@CombustibleConsentimiento)
            tietMecanicoHour.setOnClickListener(this@CombustibleConsentimiento)
        }
        ocultarMecanico()
    }

    private fun configSpinners() {
        binding.apply {
            spinUno.setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, arrayUno))
            spinUno.showDropDown()
            spinUno.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    if (position == 0) {
                        tilDos.visibility = View.VISIBLE
                        spinDos.setText("")
                    } else {
                        tilDos.visibility = View.GONE
                        spinDos.setText("")
                    }


                }


            spinDos.apply {
                setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, arrayDos))
            }
        }
    }

    override fun onClick(v: View?) {
        binding.apply {

            when(v?.id){

                tietMecanicoHour.id -> showSelectHour(tietMecanicoHour, parentFragmentManager)

                switchCompat.id -> showAMOficial(switchCompat.isChecked)

                switchCompatMecanico.id -> showAMMecanico(switchCompatMecanico.isChecked)

                btnFirmarMecanico.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaMecanico.setImageBitmap(bitmap)
                        firmaMecanico = CreateImageFile.getB64FromBitmap(bitmap)
                    } .show(it, "signatureFragment")
                }

                btnFirmar.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaOficial.setImageBitmap(bitmap)
                        firmaOperaciones = CreateImageFile.getB64FromBitmap(bitmap)
                    }.show(it, "SignatureFragment")
                }

                btnConsultarOficial.id -> if(buttonOficialState) findOficial(v) else resetOficial()

                btnConsultarMecanico.id -> if(buttonMecanicoState) findMecanico(v) else resetMecanico()
            }
        }
    }

    private fun findMecanico(v: View) {
        v.hideKeyboard()
        val dialog = Dialogo(requireContext())
        dialog.btnCerrar.setOnClickListener{dialog.Ocultar()}
        binding.apply {
            if(actvNumeroMecanico.text.isNullOrEmpty() || actvNumeroMecanico.text.length < 4)
                tilNumeroOficial.error = "Ingresa un número de empleado valido"
            else {
                dialog.mostrarCargando("Buscando mecánico, espere.")
                tilNumeroMecanico.isErrorEnabled = false
                model.getEmpleado(actvNumeroMecanico.text.toString()).observe(viewLifecycleOwner, { mecanicoResponse ->
                    dialog.Ocultar()
                    if(mecanicoResponse == null)
                        Snackbar.make(this.root,"Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (mecanicoResponse.status == RequestState.REQ_OK) {
                            tvNombreMecanico.text = "${mecanicoResponse.result.usuarioCore.name} ${mecanicoResponse.result.usuarioCore.apellidoPaterno} ${mecanicoResponse.result.usuarioCore.apellidoMaterno}"
                            unableSignatureImageView(false, ivFirmaMecanico)
                            btnFirmarMecanico.visibility = View.VISIBLE
                            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                            img?.setBounds(0, 0, 60, 60)
                            btnConsultarMecanico.setCompoundDrawablesRelative(img, null, null, null)
                            buttonMecanicoState =! buttonMecanicoState
                            actvNumeroMecanico.apply {
                                isEnabled = false
                                setTextColor(resources.getColor(R.color.black))
                            }
                        } else {
                            dialog.mostrarError(
                                "Mecánico no encontrado",
                                "Verifica que el número de empleado ${actvNumeroMecanico.text} sea correcto."
                            )
                            dialog.btnCerrar.setOnClickListener {
                                dialog.Ocultar()
                            }
                        }
                    }
                })

            }
        }
        model = PrimerVueloDiaViewModel()
    }

    private fun findOficial(v: View) {
        v.hideKeyboard()
        val dialog = Dialogo(requireContext())
        dialog.btnCerrar.setOnClickListener{dialog.Ocultar()}
        binding.apply {
            if(actvNumeroOficial.text.isNullOrEmpty() || actvNumeroOficial.text.length < 4)
                tilNumeroOficial.error = "Ingresa un número de empleado valido"
            else {
                dialog.mostrarCargando("Buscando oficial, espere.")
                tilNumeroOficial.isErrorEnabled = false
                model.getEmpleado(actvNumeroOficial.text.toString()).observe(viewLifecycleOwner, { oficialResponse ->
                    dialog.Ocultar()
                    if(oficialResponse == null)
                        Snackbar.make(this.root,"Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (oficialResponse.status == RequestState.REQ_OK) {
                            tvNombreOficial.text = "${oficialResponse.result.usuarioCore.name} ${oficialResponse.result.usuarioCore.apellidoPaterno} ${oficialResponse.result.usuarioCore.apellidoMaterno}"
                            unableSignatureImageView(false, ivFirmaOficial)
                            btnFirmar.visibility = View.VISIBLE
                            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                            img?.setBounds(0, 0, 60, 60)
                            btnConsultarOficial.setCompoundDrawablesRelative(img, null, null, null)
                            buttonOficialState =! buttonOficialState
                            actvNumeroOficial.apply {
                                isEnabled = false
                                setTextColor(resources.getColor(R.color.black))
                            }
                        } else {
                            dialog.mostrarError(
                                "Oficial no encontrado",
                                "Verifica que el número de empleado ${actvNumeroOficial.text} sea correcto."
                            )
                            dialog.btnCerrar.setOnClickListener {
                                dialog.Ocultar()
                            }
                        }
                    }
                })

            }
        }
        model = PrimerVueloDiaViewModel()
    }

    private fun showAMOficial(checked: Boolean) {
        binding.apply {
            if (checked) {
                btnConsultarOficial.visibility = View.GONE
                tilNombreOficialManual.visibility = View.VISIBLE
                tvNombreOficial.visibility = View.GONE
                tilNumeroOficial.prefixText = ""
                unableSignatureImageView(false, ivFirmaOficial)
                resetOficial()
                actvNombreOficial.setText("")
                etObservacionesValue.setText("")
                buttonOficialState = true
            } else {
                btnConsultarOficial.visibility = View.VISIBLE
                tilNombreOficialManual.visibility = View.GONE
                tvNombreOficial.visibility = View.VISIBLE
                actvNombreOficial.setText("")
                etObservacionesValue.setText("")
                resetOficial()
                buttonOficialState = true
            }
        }
    }

    private fun showAMMecanico(checked: Boolean) {
        binding.apply {
            if (checked) {
                btnConsultarMecanico.visibility = View.GONE
                tilNombreMecanicoManual.visibility = View.VISIBLE
                tvNombreMecanico.visibility = View.GONE
                tilNumeroMecanico.prefixText = ""
                unableSignatureImageView(false, ivFirmaMecanico)
                resetMecanico()
                actvOficialNombreMecanico.setText("")
                etObservacionesMecanicoValue.setText("")
                buttonMecanicoState = true
            } else {
                btnConsultarMecanico.visibility = View.VISIBLE
                tilNombreMecanicoManual.visibility = View.GONE
                tvNombreMecanico.visibility = View.VISIBLE
                actvOficialNombreMecanico.setText("")
                etObservacionesMecanicoValue.setText("")
                buttonMecanicoState = true
                resetOficial()
            }
        }
    }

    private fun resetMecanico() {
        binding.apply{
            actvNumeroMecanico.apply {
                setText("")
                isEnabled = true
            }
            tvNombreMecanico.text = ""
            ivFirmaMecanico.setImageBitmap(null)
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarMecanico.setCompoundDrawablesRelative(img, null, null, null)
            buttonMecanicoState =! buttonMecanicoState
            firmaMecanico = ""
        }
    }

    private fun resetOficial() {
        binding.apply {
            actvNumeroOficial.apply {
                setText("")
                isEnabled = true
            }
            tvNombreOficial.text = ""
            ivFirmaOficial.setImageBitmap(null)
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarOficial.setCompoundDrawablesRelative(img, null, null, null)
            buttonOficialState =! buttonOficialState
            firmaOperaciones = ""
        }
    }

    private fun ocultarMecanico() {
        binding.apply {
            if (vuelo?.companiaEquipo.equals("5D")) {
                secccionMecanico.forEach{ it.visibility = View.GONE}
            }
        }
    }

    fun setRqValues(rqOrdenCarga : RequestOrdenCarga): RequestOrdenCarga {
        binding.apply {
            rqOrdenCarga.apply {
                noEmpleadoOficialOperaciones = actvNumeroOficial.text.toString()
                nombreOficialOperaciones = if(switchCompat.isChecked)
                    actvNombreOficial.text.toString()
                else
                    tvNombreOficial.text.toString()
                noEmpleadoMecanico = actvNumeroMecanico.text.toString()
                nombreMecanico = if(switchCompatMecanico.isChecked) "${actvOficialNombreMecanico.text}" else "${tvNombreMecanico.text}"
                extraFuel = if(spinDos.visibility == View.VISIBLE) "${spinUno.text} ${spinDos.text}" else "${spinUno.text}"
                if (extraFuel.contains(getString(R.string.selecciona_una_opci_n))) {
                    extraFuel = ""
                }
                observacionesOficial = "${etObservacionesValue.text}"
                observacionesMecanico = "${etObservacionesMecanicoValue.text}"
                horaLlegadaMecanico = tietMecanicoHour.text.toString()
                firmaB64Mecanico = firmaMecanico
                firmaB64Operaciones = firmaOperaciones
            }
        }
        return rqOrdenCarga
    }

}