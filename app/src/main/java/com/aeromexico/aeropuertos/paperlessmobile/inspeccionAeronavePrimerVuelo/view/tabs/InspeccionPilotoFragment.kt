package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.datastore.preferences.protobuf.Internal
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionPilotoBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Oficial
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Piloto
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Pregunta
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.*

class InspeccionPilotoFragment(
    private var getBindingprimerVuelo: () -> FragmentPrimerVueloDiaBinding,
    private var getRequestBody: () -> RequestFirstFlightForm,
    var enviarForm:() -> Unit
) : Fragment(), View.OnClickListener, ObservableScrollViewCallbacks{
    lateinit var binding: FragmentInspeccionPilotoBinding
    lateinit var buttons: Array<TextView>
    lateinit var optionsBoxes: Array<CheckBox>
    lateinit var optionsBoxesMal: Array<CheckBox>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var itemsToHide: Array<View>
    lateinit var pilot: Piloto
    lateinit var preguntas1to4: MutableList<Pregunta>
    lateinit var diag: Dialogo
    private var buttonState: Boolean = true
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspeccionPilotoBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity
        model = PrimerVueloDiaViewModel()
        pilot = Piloto("", "", "", "", "", "")
        preguntas1to4 = mutableListOf()
        diag = Dialogo(requireContext())

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

        diag.apply {
            btnCerrar.visibility = View.GONE
            btnAceptar.visibility = View.VISIBLE
            btnAceptar.setOnClickListener {diag.Ocultar()}
        }

        binding.apply {
            btnEnviarForm.setOnClickListener {
                enviarForm.invoke()
            }
            optionsBoxes = arrayOf(cbPregunta1Bueno, cbPregunta2bueno, cbPregunta3bueno, cbPregunta4bueno)
            optionsBoxesMal = arrayOf(cbPregunta1Malo, cbPregunta2malo, cbPregunta3malo, cbPregunta4malo)
            buttons = arrayOf(btnConsultarEmpleado, btnFirmar, btnGuardarLocal)
            tilNombrePiloto.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
            }
            for(box in optionsBoxes) box.setOnClickListener(this@InspeccionPilotoFragment)
            for(box in optionsBoxesMal) box.setOnClickListener(this@InspeccionPilotoFragment)
            buttons.forEach { it.setOnClickListener(this@InspeccionPilotoFragment) }
        //scrollView.setScrollViewCallbacks(this@InspeccionPilotoFragment)

            tvIdPiloto.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(tvIdPiloto.text.toString().isNotEmpty())
                        if(tvIdPiloto.text.toString()[0] == '0' && tvIdPiloto.text.toString().length > 1) {
                            tvIdPiloto.setText(
                                tvIdPiloto.text.toString().toInt().toString()
                            )
                            tvIdPiloto.setSelection(tvIdPiloto.text.toString().length)
                        }
                }

            })
        }
        for (i in 1..4) preguntas1to4.add(Pregunta(0, i))
        loadSS()
        return binding.root
    }

    override fun onClick(v: View?) {

        binding.apply {
            when (v?.id) {
                
                R.id.btnConsultarEmpleado -> if(buttonState) findPilot(v) else clearForm()

                btnFirmar.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaPilotos.setImageBitmap(bitmap)
                        pilot.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                    }.show(it, "SignatureFragment")
                }

                btnGuardarLocal.id -> onSaveLocal()

                else -> setCheckBoxesLogic(optionsBoxesMal, optionsBoxes, v, requireContext())
            }
        }
    }

    private fun loadSS() {
        var entra : Boolean = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach out@{
                entra = true
                var rq = Gson().fromJson(it.request, RequestFirstFlightForm::class.java)
                if (rq.flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber){
                    if (rq.piloto.nombre != "" && getRequestBody.invoke().piloto.nombre.isNullOrEmpty())//cambio GET FORMULARIO LLENADO
                        Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                            loadFromDB(this)
                            return@out
                        }
                    else if (rq.piloto.nombre.isNotEmpty() && getRequestBody.invoke().piloto.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())
//                        addPilotToDB(getRequestBody.invoke().piloto)
                    }
                    else if (rq.piloto.nombre.isNullOrEmpty() && getRequestBody.invoke().piloto.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())

                }
                else if (getRequestBody.invoke().piloto.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                    loadFromDB(getRequestBody.invoke())
                }

            }

        }
        if (!entra && getRequestBody.invoke().piloto.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
            loadFromDB(getRequestBody.invoke())

        model = PrimerVueloDiaViewModel()
    }

    private fun loadFromDB(requestFirstFlightForm: RequestFirstFlightForm?) {
        var aux = 1
        var i = 0

        binding.apply {
            requestFirstFlightForm?.apply {
                preguntas.forEach {
                    if (it.idpregunta >= aux && aux < 5) {
                        if (it.condicion == 0) {
                            optionsBoxesMal[i].isChecked = true
                            i++
                            aux++
                        } else {
                            optionsBoxes[i].isChecked = true
                            i++
                            aux++
                        }
                    }
                }
                etDiscrepanciasValue.setText("${piloto.discrepancia}")
                tvIdPiloto.setText("${piloto.numEmpleado}")
                tvNombrePiloto.text = "${piloto.nombre}"
                ivFirmaPilotos.apply {
                    visibility = View.GONE
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(if(piloto.firmaB64.isNullOrEmpty()) " " else piloto.firmaB64))
                }
                tilNombrePiloto.prefixText = ""
                this@InspeccionPilotoFragment.pilot = piloto
            }
        }
        disableForm()
    }

    private fun clearForm() {
        binding.apply {
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = !buttonState
            resetPilotValues()
            tvIdPiloto.setText("")
            tvNombrePiloto.text = ""
            unableSignatureImageView(true, ivFirmaPilotos)
            btnFirmar.visibility = View.GONE
            tvIdPiloto.isEnabled = true
            btnGuardarLocal.apply {
                visibility = View.GONE
                isEnabled = false
            }
        }
    }

    private fun resetPilotValues() {
        pilot.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
        }
    }

    private fun findPilot(v: View) {
        v.hideKeyboard()
        val diag = Dialogo(requireContext())
        diag.btnCerrar.setOnClickListener { diag.Ocultar() }
        binding.apply {
            if (tvIdPiloto.text.isNullOrEmpty() || tvIdPiloto.text.length < 4) {
                tilNombrePiloto.error = "Ingresa un número de empleado valido"
            } else {
                diag.mostrarCargando("Buscando piloto, espere.")
                tilNombrePiloto.isErrorEnabled = false
                model.getEmpleado(binding.tvIdPiloto.text.toString())
                    .observe(viewLifecycleOwner, {
                        diag.Ocultar()
                        if (it == null)
                            Snackbar.make(this.root,"Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                        else {
                            if (it.status == RequestState.REQ_OK) {
                                tvNombrePiloto.text = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                                pilot.apply {
                                    creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                                    it.result.usuarioCore.apply {
                                        nombre = "$name $apellidoPaterno $apellidoMaterno"
                                        numEmpleado = "AM$employeeNumber"
                                    }
                                }
                                unableSignatureImageView(false, ivFirmaPilotos)
                                btnFirmar.visibility = View.VISIBLE
                                val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                                img?.setBounds(0, 0, 60, 60)
                                btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
                                buttonState =! buttonState
                                tvIdPiloto.apply {
                                    isEnabled = false
                                    setTextColor(resources.getColor(R.color.black))
                                }
                            } else {
                                diag.mostrarError(
                                    "Piloto no encontrado",
                                    "Verifica que el número de empleado ${tvIdPiloto.text} sea correcto."
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

    private fun onSaveLocal() {
        val diag = Dialogo(requireContext())
        var discre = false

        optionsBoxesMal.forEach { if (it.isChecked) discre = true}
        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener{diag.Ocultar()}

        binding.apply {
            if (!allBoxesSelected(optionsBoxes, optionsBoxesMal)) {
                diag.mostrarError(
                    "Preguntas vacias",
                    "Para poder guardar el formulario debes hacer check en todo el formulario"
                )
            } else if (discre && etDiscrepanciasValue.text.toString().length < 5) {
                diag.mostrarError(
                    "Discrepancia vacia",
                    "Debes ingresar detalles de la discrepancia que has marcado.\nIngresa por lo menos 5 caracteres."
                )
            } else if (tvIdPiloto.text.toString() == "") {
                diag.mostrarError(
                    "Piloto no valido",
                    "Para poder guardar el formulario debes ingresar un piloto"
                )
            } else if (pilot.firmaB64 == "") {
                diag.mostrarError(
                    "Firma vacia",
                    "Para poder guardar el formulario debe estar firmado."
                )
            } else {
                pilot.apply {
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                    Log.i("Fecha creacion piloto",fechaCreacion.toString())
                    discrepancia = "${binding.etDiscrepanciasValue.text}"
                    llenarFromulario(optionsBoxes, preguntas1to4)
                    if (getRequestBody.invoke().sobrecargo.nombre != "" || getRequestBody.invoke().oficial.nombre != "")
                        addPilotToDB(this) else guardarFormBD(this)
                    disableForm()
                }
            }
        }
    }

    private fun disableForm() {
        val boxes: Array<CheckBox> = optionsBoxes + optionsBoxesMal
        boxes.forEach{ it.isEnabled = false }
        buttons.forEach { it.apply { isEnabled = false; visibility = View.GONE } }
        binding.apply {
            etDiscrepanciasValue.isEnabled = false
            editText.endIconMode = TextInputLayout.END_ICON_NONE
            tvIdPiloto.isEnabled = false
            tvNombrePiloto.isEnabled = false
            ivFirmaPilotos.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.border_for_image_view_signature_gray,
                null
            )
            tilNombrePiloto.prefixTextView.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.color_gray_white_background_checklist,
                    null
                )
            )
        }
    }

    private fun addPilotToDB(piloto: Piloto) {
        var entra = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach out@{
                if (Gson().fromJson(it.request, RequestFirstFlightForm::class.java).flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber){
                    Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                        this.piloto = piloto
                        preguntas.addAll(preguntas1to4)
                        updateBD(CheckPrimeVueloEntity(request = Gson().toJson(this).toString(), id = it.id))
                        return@out
                    }
                }
            }
            if (!entra)
                guardarFormBD(piloto)
        }
    }

    private fun updateBD(checkPrimeVueloEntity: CheckPrimeVueloEntity) {
        model.updateForm(checkPrimeVueloEntity).observeOnce{
            Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
            binding.btnEnviarForm.isVisible = true
        }
    }

    private fun guardarFormBD(piloto: Piloto) {
        getRequestBody.invoke().apply {
            this.piloto = piloto
            preguntas.addAll(preguntas1to4)
            flightReferenceNumber = getRequestBody.invoke().flightReferenceNumber
            model.addFormToDB(this).observe(viewLifecycleOwner, {
                Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
                binding.btnEnviarForm.isVisible = true
            })
        }
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