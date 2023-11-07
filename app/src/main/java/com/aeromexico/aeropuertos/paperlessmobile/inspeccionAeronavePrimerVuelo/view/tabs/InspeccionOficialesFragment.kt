package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionOficialesBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Oficial
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Pregunta
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.*
import kotlin.reflect.KFunction0


class InspeccionOficialesFragment(
    private var getBindingprimerVuelo: KFunction0<FragmentPrimerVueloDiaBinding>,
    private var getRequestBody: () -> RequestFirstFlightForm,
    var enviarForm:() -> Unit
) : Fragment(), View.OnClickListener, ObservableScrollViewCallbacks {
    lateinit var binding: FragmentInspeccionOficialesBinding
    lateinit var optionsBoxesBueno: Array<CheckBox>
    lateinit var optionsBoxesMalo: Array<CheckBox>
    lateinit var buttons: Array<View>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var oficial: Oficial
    lateinit var diag: Dialogo
    lateinit var preguntas26to28: MutableList<Pregunta>
    private var buttonState: Boolean = true
    private var mActivity: MainActivity? = null
    lateinit var itemsToHide: Array<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspeccionOficialesBinding.inflate(inflater, container, false)
        model = PrimerVueloDiaViewModel()
        oficial = Oficial("", "", "", "", "", "")
        preguntas26to28 = mutableListOf()
        mActivity = activity as? MainActivity
        diag = Dialogo(requireContext())

        diag.apply {
            btnCerrar.visibility = View.GONE
            btnAceptar.visibility = View.VISIBLE
            btnAceptar.setOnClickListener {diag.Ocultar()}
        }

        getBindingprimerVuelo.invoke().apply {
            itemsToHide = arrayOf(
                tvDateFlightValue,
                flightDate,
                tvEnrollment,
                tvEnrollmentValue,
                tvFlightNumberValue,
                fligthID,
                tvRouteValue,
                route,
                percnota,
                pdf,
                dividerTop,
                tabLayout
            )
        }

        binding.apply {
            btnEnviarForm.setOnClickListener {
                enviarForm.invoke()
            }

            optionsBoxesBueno = arrayOf(cbPregunta4bueno, cbPregunta5bueno, cbPregunta6bueno)
            optionsBoxesMalo = arrayOf(cbPregunta4malo, cbPregunta5malo, cbPregunta6malo)
            buttons = arrayOf(btnConsultarOficial, btnFirmar, btnGuardarLocal)

            buttons.forEach { it.setOnClickListener(this@InspeccionOficialesFragment)}
            optionsBoxesMalo.forEach { it.setOnClickListener(this@InspeccionOficialesFragment)}
            optionsBoxesBueno.forEach { it.setOnClickListener(this@InspeccionOficialesFragment)}
            switchCompat.setOnClickListener(this@InspeccionOficialesFragment)
            tilNombreOficial.prefixTextView.apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
            }
            //scrollView.setScrollViewCallbacks(this@InspeccionOficialesFragment)
            for (i in 26 .. 28) preguntas26to28.add(Pregunta(0, i))

            actvNumeroOficial.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(actvNumeroOficial.text.toString().isNotEmpty())
                        if(actvNumeroOficial.text.toString()[0] == '0' && actvNumeroOficial.text.toString().length > 1) {
                            actvNumeroOficial.setText(
                                actvNumeroOficial.text.toString().toInt().toString()
                            )
                            actvNumeroOficial.setSelection(actvNumeroOficial.text.toString().length)
                        }
                }

            })
        }
        loadSS()
        return binding.root
    }

    override fun onClick(v: View?) {

        binding.apply {
            when(v?.id){

                switchCompat.id -> {
                    if (switchCompat.isChecked)
                        showNoAeromexicoOficial()
                    else
                        showAeromexicoOficial()
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

                btnGuardarLocal.id -> onSaveLocal(switchCompat.isChecked)

                else -> setCheckBoxesLogic(optionsBoxesMalo, optionsBoxesBueno, v, requireContext())
            }
        }
    }

    private fun loadSS() {
        var entra = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach out@{
                entra = true
                var rq = Gson().fromJson(it.request, RequestFirstFlightForm::class.java)
                if (rq.flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber){
                    if (rq.oficial.nombre != "" && getRequestBody.invoke().oficial.nombre.isNullOrEmpty())//cambio GET FORMULARIO LLENADO)
                        Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                            loadFromDB(this)
                            return@out
                        }
                    else if (rq.oficial.nombre.isNotEmpty() && getRequestBody.invoke().oficial.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())
//                        addOficialToDB(getRequestBody.invoke().oficial)
                    }
                    else if (rq.oficial.nombre.isNullOrEmpty() && getRequestBody.invoke().oficial.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())
                }
                else if (getRequestBody.invoke().oficial.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                    loadFromDB(getRequestBody.invoke())
                }
            }
        }
        if (!entra && getRequestBody.invoke().oficial.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
            loadFromDB(getRequestBody.invoke())

        model = PrimerVueloDiaViewModel()
    }

    private fun loadFromDB(requestFirstFlightForm: RequestFirstFlightForm) {
        var aux = 26
        var i = 0
        binding.apply {
            requestFirstFlightForm.apply {
                preguntas.forEach {
                    if (it.idpregunta >= aux && aux < 29) {
                        if (it.condicion == 0) {
                            optionsBoxesMalo[i].isChecked = true
                            i++
                            aux++
                        } else {
                            optionsBoxesBueno[i].isChecked = true
                            i++
                            aux++
                        }
                    }
                }
                etDiscrepanciasValue.setText("${oficial.discrepancia}")
                actvNumeroOficial.setText("${oficial.numEmpleado}")
                tilNombreOficial.prefixText = ""
                tvNombreOficial.text = "${oficial.nombre}"
                ivFirmaPilotos.apply {
                    visibility = View.GONE
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(oficial.firmaB64))
                }
                this@InspeccionOficialesFragment.oficial = oficial
            }
        }
        disableForm()
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

    private fun findOficial(v: View) {
        binding.apply {
            v.hideKeyboard()
            val diag = Dialogo(requireContext())
            if (actvNumeroOficial.text.isNullOrEmpty() || actvNumeroOficial.text.length < 4) {
                tilNombreOficial.error = "Ingresa un número de oficial valido"
            } else {
                diag.mostrarCargando("Buscando oficial, espere.")
                tilNombreOficial.isErrorEnabled = false
                model.getEmpleado(actvNumeroOficial.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT)
                            .show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombreOficial.text  = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            oficial.apply {
                                creadoPor =
                                    MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                                unableSignatureImageView(false, ivFirmaPilotos)
                                btnFirmar.visibility = View.VISIBLE
                                val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                                img?.setBounds(0, 0, 60, 60)
                                btnConsultarOficial.setCompoundDrawablesRelative(img, null, null, null)
                                buttonState =! buttonState
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

    private fun onSaveLocal(aeEmployee: Boolean) {
        val diag = Dialogo(requireContext())
        var discre = false

        optionsBoxesMalo.forEach { if (it.isChecked) discre = true }
        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener { diag.Ocultar() }
        binding.apply {
            if (!allBoxesSelected(optionsBoxesBueno, optionsBoxesMalo))
                diag.mostrarError(
                    "Preguntas vacias",
                    "Para poder guardar el formulario debes hacer check en todo el formulario"
                )
            else if (discre && etDiscrepanciasValue.text.toString().length < 5) {
                diag.mostrarError(
                    "Discrepancia vacia",
                    "Debes ingresar detalles de la discrepancia que has marcado.\nIngresa por lo menos 5 caracteres."
                )
            } else if (!aeEmployee && actvNumeroOficial.text.toString() == "")
                diag.mostrarError(
                    "Oficial no valido",
                    "Para poder guardar el formulario debes ingresar un numero de oficial."
                )
            else if (aeEmployee && (actvNumeroOficial.text.toString() == "" || actvOficialNombreOficial.text.toString() == ""))
                diag.mostrarError(
                    "Oficial no valido",
                    "Para poder guardar el formulario debes ingresar un numero y un nombre de oficial."
                )
            else if (oficial.firmaB64 == "")
                diag.mostrarError(
                    "Firma vacia",
                    "Para poder guardar el formulario debe estar firmado"
                )
            else
                oficial.apply {
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                    discrepancia = "${binding.etDiscrepanciasValue.text}"
                    llenarFromulario(optionsBoxesBueno, preguntas26to28)
                    if (switchCompat.isChecked) {
                        nombre = actvOficialNombreOficial.text.toString()
                        numEmpleado = actvNumeroOficial.text.toString()
                        creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                    }
                    if (getRequestBody.invoke().sobrecargo.nombre != "" || getRequestBody.invoke().piloto.nombre != "" || getRequestBody.invoke().cocinas.nombre != "" )
                        addOficialToDB(this) else guardarFormBD(this)
                    disableForm()
                }
        }
    }

    private fun disableForm() {
        var boxes: Array<CheckBox> = optionsBoxesBueno + optionsBoxesMalo
        boxes.forEach { it.isEnabled = false }
        buttons.forEach { it.isEnabled = false }
        binding.apply {
            etDiscrepanciasValue.isEnabled = false
            editText.endIconMode = TextInputLayout.END_ICON_NONE
            actvNumeroOficial.isEnabled = false
            tvNombreOficial.isEnabled = false
            switchCompat.isEnabled = false
            actvOficialNombreOficial.isEnabled = false
            ivFirmaPilotos.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.border_for_image_view_signature_gray,
                null
            )
            buttons.forEach {
                it.isEnabled = false
                it.visibility = View.GONE
            }
            tilNombreOficial.layoutParams.apply {

            }
        }
    }

    private fun addOficialToDB(oficial: Oficial) {
        var entra = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                if(Gson().fromJson(it.request, RequestFirstFlightForm::class.java).flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber)
                    Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                        entra = true
                        this.oficial = oficial
                        preguntas.addAll(preguntas26to28)
                        updateBD(CheckPrimeVueloEntity(request = Gson().toJson(this).toString(), id = it.id))
                        return@forEach
                    }

            }
            if (!entra)
                guardarFormBD(oficial)
        }
    }

    private fun updateBD(checkPrimeVueloEntity: CheckPrimeVueloEntity) {
        model.updateForm(checkPrimeVueloEntity).observeOnce{
            Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
            binding.btnEnviarForm.isVisible = true
        }
    }

    private fun guardarFormBD(oficial: Oficial) {
        getRequestBody.invoke().apply {
            this.oficial = oficial
            preguntas.addAll(preguntas26to28)
            flightReferenceNumber = getRequestBody.invoke().flightReferenceNumber
            model.addFormToDB(this).observe(viewLifecycleOwner, {
                Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
                binding.btnEnviarForm.isVisible = true
            })
        }
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

        resetOficialValues()
    }

    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {}

    override fun onDownMotionEvent() {}

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
        val max = binding.rootView.maxHeight

        if (scrollState == ScrollState.UP) {
            mActivity?.supportActionBar?.apply {
                if (mActivity?.supportActionBar?.isShowing!!) {
                    itemsToHide.forEach { it.visibility = View.GONE }
                    hide()
                }
            }
        } else if (scrollState == ScrollState.DOWN ) {
            mActivity?.supportActionBar?.apply {
                if (binding.scrollView.scrollY < max) {
                    show()
                    itemsToHide.forEach { it.visibility = View.VISIBLE }
                }
            }
        }
    }
}