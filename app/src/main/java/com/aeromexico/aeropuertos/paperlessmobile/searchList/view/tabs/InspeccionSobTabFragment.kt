package com.aeromexico.aeropuertos.paperlessmobile.searchList.view.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.res.ResourcesCompat
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionSobTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.allBoxesSelected
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.llenarFromularioSerchLis
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.*
import com.aeromexico.aeropuertos.paperlessmobile.searchList.viewModel.SerarchListViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class InspeccionSobTabFragment(val guid: String?, val lpd: Boolean) : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentInspeccionSobTabBinding
    lateinit var optionsBoxes: Array<CheckBox>
    lateinit var optionsBoxesMal: Array<CheckBox>
    lateinit var inspeccionSob: InspeccionSob
    private var buttonState: Boolean = true
    lateinit var buttons: Array<View>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var modelSerch: SerarchListViewModel
    lateinit var preguntas37a40: MutableList<PreguntaCheckList>
    lateinit var itemsToHide: Array<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspeccionSobTabBinding.inflate(inflater, container, false)

        inspeccionSob = InspeccionSob()
        model = PrimerVueloDiaViewModel()
        modelSerch = SerarchListViewModel()
        preguntas37a40 = mutableListOf()
        for (i in 37..40) preguntas37a40.add(PreguntaCheckList(0, i))

        binding.apply {
            optionsBoxes = arrayOf(
                cbPregunta4bueno,
                cbPregunta5bueno,
                cbPregunta6bueno,
                cbPregunta7bueno,
            )
            optionsBoxesMal = arrayOf(
                cbPregunta4malo,
                cbPregunta5malo,
                cbPregunta6malo,
                cbPregunta7malo,
            )
            buttons = arrayOf(btnConsultarOficial, btnFirmar, btnGuardarLocal)
            itemsToHide = arrayOf(pregunta6, cbPregunta6bueno, cbPregunta6malo, pregunta7, cbPregunta7bueno, cbPregunta7malo)
            optionsBoxesMal.forEach { it.setOnClickListener(this@InspeccionSobTabFragment) }
            optionsBoxes.forEach { it.setOnClickListener(this@InspeccionSobTabFragment) }
            buttons.forEach { it.setOnClickListener(this@InspeccionSobTabFragment) }
            switchCompat.setOnClickListener(this@InspeccionSobTabFragment)

        }

        validateInfo()
        if(!lpd) isLPD()
        return binding.root
    }

    private fun isLPD() {
        itemsToHide.forEach { it.visibility = View.GONE }
        optionsBoxesMal[2].isChecked = true
        optionsBoxesMal[3].isChecked = true
    }

    private fun validateInfo() {
        modelSerch.getInfo(guid!!)
        modelSerch.responseGetInfo.observe(viewLifecycleOwner, { response ->
            if(response.result.searchList.inspeccionSob.nombre != "")
                checkOficial(response)
        })
    }

    private fun checkOficial(response: ResponseGetInfo) {
        binding.apply {
            var aux = 37
            var i = 0
            response.result.searchList.preguntas.forEach {
                if (it.idpregunta >= aux && aux <= 40) {
                    if (it.condicion) {
                        optionsBoxes[i].isChecked = true
                        i++
                        aux++
                    } else {
                        optionsBoxesMal[i].isChecked = true
                        i++
                        aux++
                    }
                }
            }

            actvNumeroOficial.setText(response.result.searchList.inspeccionSob.numEmpleado)
          //  tvNombreOficial.text = response.result.searchList.oficial.nombre
            //ivFirmaPilotos.setImageBitmap(CreateImageFile.setBitmapFromB64String(response.result.searchList.oficial.firmaB64))
            diableForm()
        }
    }

    private fun diableForm() {
        binding.apply {
            optionsBoxes.forEach { it.isEnabled = false }
            optionsBoxesMal.forEach { it.isEnabled = false }
            actvNumeroOficial.isEnabled = false
            btnConsultarOficial.visibility = View.GONE
            btnFirmar.visibility = View.GONE
            btnGuardarLocal.visibility = View.GONE
            ivFirmaPilotos.visibility = View.GONE
            switchCompat.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {

                switchCompat.id -> {
                    if (switchCompat.isChecked) {
                        showNoAeromexicoOficial()
                        resetOficialValues()
                    } else {
                        showAeromexicoOficial()
                        resetOficialValues()
                    }
                }

                btnConsultarOficial.id -> if (buttonState) findOficial(v) else clearForm()

                btnFirmar.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaPilotos.setImageBitmap(bitmap)
                        inspeccionSob.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                    }.show(it, "SignatureFragment")
                }

                btnGuardarLocal.id -> enviarForm()

                else -> setCheckBoxesLogic(optionsBoxesMal, optionsBoxes, v, requireContext())
            }
        }
    }

    private fun enviarForm() {
        var re = InsertSearchList(guidVuelo = this.guid, lPD = lpd)
        var diag = Dialogo(requireContext())

        inspeccionSob.apply {
            if (binding.switchCompat.isChecked) {
                nombre = binding.actvOficialNombreOficial.text.toString()
                numEmpleado = binding.actvNumeroOficial.text.toString()
                creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
            }
        }

            if (allBoxesSelected(optionsBoxes, optionsBoxesMal) && inspeccionSob.firmaB64 != "" && inspeccionSob.nombre != "" && inspeccionSob.numEmpleado != "") {
                inspeccionSob.apply {
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                    llenarFromularioSerchLis(optionsBoxes, preguntas37a40)

                    preguntas = preguntas37a40
                }
                re.inspeccionSob = inspeccionSob
                diag.mostrarCargando("Enviando formulario")
                modelSerch.insertSearchList(re).observeOnce { response ->
                    diag.apply {
                        btnCerrar.visibility = View.GONE
                        btnAceptar.visibility = View.VISIBLE
                        btnAceptar.setOnClickListener(this@InspeccionSobTabFragment)
                    }
                    diag.Ocultar()
                    if (response != null) {
                        if (response.error)
                            diag.mostrarError("Error al enviar formulario", "${response.errorMessage}")
                        else {
                            diag.apply {
                                mostrarMensajeConfirmacion(
                                    "Se envio correctamente",
                                    "Folio ${response.result.searchList.idSearchList}"
                                )
                                btnCerrar.visibility = View.GONE
                                btnAceptar.setOnClickListener {
                                    diag.Ocultar()
                                    diableForm()
                                }
                            }
                            diableForm()
                        }
                    } else {
                        diag.mostrarError(
                            "Sin conexión",
                            "Verifica tu enlace a internet"
                        )
                        diag.btnCerrar.setOnClickListener{ diag.Ocultar()}
                    }
                }
            } else {
                diag.mostrarError(
                    "Formulario incompleto",
                    if (!allBoxesSelected(optionsBoxes, optionsBoxesMal))
                        "Debes completar todos los checks para poder enviar"
                    else if (inspeccionSob.firmaB64 == "") "El formulario debe estar firmado"
                    else "Debes ingresar numero y nombre de empleado."
                )
                diag.btnCerrar.visibility = View.VISIBLE
                diag.btnCerrar.setOnClickListener{ diag.Ocultar()}
            }
        }

    private fun findOficial(v: View) {
        binding.apply {
            v.hideKeyboard()
            val diag = Dialogo(requireContext())
            if (actvNumeroOficial.text.isNullOrEmpty() || actvNumeroOficial.text.length < 4) {
                tilNombreOficial.error = "Ingresa un número de oficial valido"
            } else {
                diag.mostrarCargando("Buscando empleado, espere.")
                tilNombreOficial.isErrorEnabled = false
                model.getEmpleado(actvNumeroOficial.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT)
                            .show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombreOficial.text =
                                "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            inspeccionSob.apply {
                                creadoPor =
                                    MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                                unableSignatureImageView(false, ivFirmaPilotos)
                                btnFirmar.visibility = View.VISIBLE
                                val img = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_clear,
                                    null
                                )
                                img?.setBounds(0, 0, 60, 60)
                                btnConsultarOficial.setCompoundDrawablesRelative(
                                    img,
                                    null,
                                    null,
                                    null
                                )
                                buttonState = !buttonState
                                actvNumeroOficial.apply {
                                    isEnabled = false
                                    setTextColor(resources.getColor(R.color.black))
                                }
                            }
                        } else {
                            diag.mostrarError(
                                "Empleado no encontrado",
                                "Verificar que el número de empleado ${actvNumeroOficial.text} sea correcto."
                            )
                            diag.btnCerrar.setOnClickListener {
                                diag.Ocultar()
                            }
                        }
                    }
                })
            }
        }
        model = PrimerVueloDiaViewModel()
    }

    private fun showNoAeromexicoOficial() {
        binding.apply {
            tilNombreOficialManual.visibility = View.VISIBLE
            tilNombreOficial.isErrorEnabled = false
            actvOficialNombreOficial.setText("")
            actvNumeroOficial.setText("")
            btnConsultarOficial.visibility = View.GONE
            tvNombreOficial.visibility = View.GONE
            btnFirmar.visibility = View.VISIBLE
            unableSignatureImageView(false, ivFirmaPilotos)
            btnConsultarOficial.visibility = View.GONE
            btnConsultarOficial.isEnabled = false
            actvNumeroOficial.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNombreOficial.prefixTextView.apply {
                text = ""
            }
        }
        resetOficialValues()
    }

    private fun showAeromexicoOficial() {
        binding.apply {
            tilNombreOficialManual.visibility = View.GONE
            actvNumeroOficial.setText("")
            btnConsultarOficial.visibility = View.VISIBLE
            btnConsultarOficial.isEnabled = true
            tvNombreOficial.apply {
                visibility = View.VISIBLE
                text = ""
            }
            btnFirmar.visibility = View.GONE
            unableSignatureImageView(true, ivFirmaPilotos)
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            binding.btnConsultarOficial.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = true
            actvNumeroOficial.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNombreOficial.prefixTextView.apply {
                text = "AM"
            }
        }
    }

    private fun clearForm() {
        binding.apply {
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarOficial.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = !buttonState
            resetOficialValues()
            actvNumeroOficial.setText("")
            tvNombreOficial.text = ""
            unableSignatureImageView(true, ivFirmaPilotos)
            btnFirmar.visibility = View.GONE
            actvNumeroOficial.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            btnGuardarLocal.apply {
                visibility = View.GONE
                isEnabled = false
            }
        }
    }

    private fun resetOficialValues() {
        inspeccionSob.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
        }
    }
}