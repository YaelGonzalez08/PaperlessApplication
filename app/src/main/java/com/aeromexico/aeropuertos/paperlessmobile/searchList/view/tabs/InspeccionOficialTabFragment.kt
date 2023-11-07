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
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionOficialTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.allBoxesSelected
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.llenarFromularioSerchLis
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.InsertSearchList
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.InspeccionOficial
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.PreguntaCheckList
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.ResponseGetInfo
import com.aeromexico.aeropuertos.paperlessmobile.searchList.viewModel.SerarchListViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class InspeccionOficialTabFragment(private val guid: String?, private val lpd: Boolean) : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentInspeccionOficialTabBinding
    lateinit var optionsBoxes: Array<CheckBox>
    lateinit var optionsBoxesMal: Array<CheckBox>
    lateinit var oficial: InspeccionOficial
    private var buttonState: Boolean = true
    lateinit var buttons: Array<View>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var modelSerch: SerarchListViewModel
    lateinit var preguntas30a35: MutableList<PreguntaCheckList>
    lateinit var itemsToHide: Array<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspeccionOficialTabBinding.inflate(layoutInflater, container, false)
        oficial = InspeccionOficial()
        model = PrimerVueloDiaViewModel()
        modelSerch = SerarchListViewModel()
        preguntas30a35 = mutableListOf()

        for (i in 30..35) preguntas30a35.add(PreguntaCheckList(0, i))

        binding.apply {
            optionsBoxes = arrayOf(
                cbPregunta4bueno,
                cbPregunta5bueno,
                cbPregunta6bueno,
                cbPregunta7bueno,
                cbPregunta8bueno,
                cbPregunta9bueno
            )
            optionsBoxesMal = arrayOf(
                cbPregunta4malo,
                cbPregunta5malo,
                cbPregunta6malo,
                cbPregunta7malo,
                cbPregunta8malo,
                cbPregunta9malo
            )
            buttons = arrayOf(btnConsultarOficial, btnFirmar, btnGuardarLocal)
            itemsToHide = arrayOf(pregunta4, cbPregunta4bueno, cbPregunta4malo, pregunta5, cbPregunta5malo, cbPregunta5bueno)

            optionsBoxesMal.forEach { it.setOnClickListener(this@InspeccionOficialTabFragment) }
            optionsBoxes.forEach { it.setOnClickListener(this@InspeccionOficialTabFragment) }
            buttons.forEach { it.setOnClickListener(this@InspeccionOficialTabFragment) }
            switchCompat.setOnClickListener(this@InspeccionOficialTabFragment)

        }
        validateInfo()
        if(!lpd) isLPD()
        return binding.root
    }

    private fun isLPD() {
        itemsToHide.forEach { it.visibility = View.GONE }
        optionsBoxesMal[0].isChecked = true
        optionsBoxesMal[1].isChecked = true
    }

    private fun validateInfo() {
        modelSerch.getInfo(guid!!)
        modelSerch.responseGetInfo.observe(viewLifecycleOwner, { response ->
            if(response.result.searchList.oficial.nombre != "")
                checkOficial(response)
        })
    }

    private fun checkOficial(response: ResponseGetInfo) {
        binding.apply {
            var aux = 30
            var i = 0
            response.result.searchList.preguntas.forEach {
                if (it.idpregunta >= aux && aux <= 35) {
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

            etInfoSeguridadValue.setText(response.result.searchList.oficial.informacionrelativa)
            actvNumeroOficial.setText(response.result.searchList.oficial.numEmpleado)
           // tvNombreOficial.text = response.result.searchList.oficial.nombre
            ivFirmaPilotos.setImageBitmap(
                if(response.result.searchList.oficial.firmaB64 != "")
                    CreateImageFile.setBitmapFromB64String(response.result.searchList.oficial.firmaB64)
            else
                null)
            diableForm()
        }
    }

    private fun diableForm() {
        binding.apply {
            optionsBoxes.forEach { it.isEnabled = false }
            optionsBoxesMal.forEach { it.isEnabled = false }
            etInfoSeguridadValue.isEnabled = false
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
                        oficial.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
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

        if (binding.switchCompat.isChecked) {
            oficial.nombre = binding.actvOficialNombreOficial.text.toString()
            oficial.numEmpleado = binding.actvNumeroOficial.text.toString()
            oficial.creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
        }
        if (allBoxesSelected(optionsBoxes, optionsBoxesMal) && oficial.firmaB64 != "" && oficial.nombre != "") {
            oficial.apply {
                fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                llenarFromularioSerchLis(optionsBoxes, preguntas30a35)

                preguntas = preguntas30a35
            }
            re.oficial = oficial
            diag.mostrarCargando("Enviando formulario")
            modelSerch.insertSearchList(re).observeOnce { response ->
                diag.apply {
                    btnCerrar.visibility = View.GONE
                    btnAceptar.visibility = View.VISIBLE
                    btnAceptar.setOnClickListener(this@InspeccionOficialTabFragment)
                }
                diag.Ocultar()
                if (response != null) {
                    if (response.error) {
                        diag.mostrarError("Error al enviar formulario", "${response.errorMessage}")
                        diag.btnAceptar.setOnClickListener { diag.Ocultar() }
                    } else {
                        diag.apply {
                            mostrarMensajeConfirmacion(
                                "Se envio correctamente",
                                "Folio ${response.result.searchList.idSearchList}"
                            )
                            btnCerrar.visibility = View.GONE
                            btnAceptar.setOnClickListener{diag.Ocultar()}
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
            else if (oficial.firmaB64 == "") "El formulario debe estar firmado"
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
                            oficial.apply {
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
                                "Oficial no encontrado",
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
        oficial.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
        }
    }
}