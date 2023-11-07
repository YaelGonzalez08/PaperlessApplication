package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCocinasBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Cocinas
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Pregunta
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.*

class CocinasFragment(
    private var getBindingprimerVuelo: () -> FragmentPrimerVueloDiaBinding,
    private var getRequestBody: () -> RequestFirstFlightForm,
    var enviarForm:() -> Unit
) : Fragment(), View.OnClickListener, ObservableScrollViewCallbacks {
    lateinit var binding: FragmentCocinasBinding
    lateinit var buttons: Array<TextView>
    lateinit var optionsBoxes: Array<CheckBox>
    lateinit var optionsBoxesMal: Array<CheckBox>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var itemsToHide: Array<View>
    lateinit var cocinas: Cocinas
    lateinit var preguntas5to8: MutableList<Pregunta>
    lateinit var diag: Dialogo
    private var buttonState: Boolean = true
    private var mActivity: MainActivity? = null
    lateinit var formsInDB: ArrayList<RequestFirstFlightForm>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCocinasBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity
        model = PrimerVueloDiaViewModel()
        cocinas = Cocinas("", "", "", "", "", "")
        preguntas5to8 = mutableListOf()
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
            optionsBoxesMal =
                arrayOf(cbPregunta5malo, cbPregunta6malo, cbPregunta7malo, cbPregunta8malo)
            optionsBoxes =
                arrayOf(cbPregunta5bueno, cbPregunta6bueno, cbPregunta7bueno, cbPregunta8bueno)
            buttons = arrayOf(btnConsultarEmpleado, btnFirmar, btnGuardarLocal)
            swEmpExterno.setOnClickListener(this@CocinasFragment)

            for (box in optionsBoxesMal) box.setOnClickListener(this@CocinasFragment)
            for (box in optionsBoxes) box.setOnClickListener(this@CocinasFragment)
            buttons.forEach { it.setOnClickListener(this@CocinasFragment) }

            tilNumSobrecargo.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                gravity = Gravity.CENTER
            }
            //scrollView.setScrollViewCallbacks(this@CocinasFragment)
            for (i in 5..8) preguntas5to8.add(Pregunta(0, i))

            tvIdSobrecargo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (tvIdSobrecargo.text.toString().isNotEmpty())
                        if (tvIdSobrecargo.text.toString()[0] == '0' && tvIdSobrecargo.text.toString().length > 1) {
                            tvIdSobrecargo.setText(
                                tvIdSobrecargo.text.toString().toInt().toString()
                            )
                            tvIdSobrecargo.setSelection(tvIdSobrecargo.text.toString().length)
                        }
                }

            })
        }
        loadSS()
        return binding.root
    }

    private fun loadSS() {
        var entra : Boolean = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                entra = true
                var rq = Gson().fromJson(it.request, RequestFirstFlightForm::class.java)
                if (rq.flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber){
                    if(rq.cocinas.nombre != "" && getRequestBody.invoke().cocinas.nombre.isNullOrEmpty())//cambio GET FORMULARIO LLENADO
                        Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                            loadFromDB(this)
                            return@forEach
                        }
                    else if (rq.cocinas.nombre.isNotEmpty() && getRequestBody.invoke().cocinas.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())
//                        addSobrecargoToDB(getRequestBody.invoke().cocinas)
                    }
                    else if (rq.cocinas.nombre.isNullOrEmpty() && getRequestBody.invoke().cocinas.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
                        loadFromDB(getRequestBody.invoke())
                }
                else if (getRequestBody.invoke().cocinas.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                    loadFromDB(getRequestBody.invoke())
                }

            }
        }
        if (!entra && getRequestBody.invoke().cocinas.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
            loadFromDB(getRequestBody.invoke())
        model = PrimerVueloDiaViewModel()
    }

    private fun loadFromDB(requestFirstFlightForm: RequestFirstFlightForm?) {
        var aux = 5
        var i = 0

        binding.apply {
            requestFirstFlightForm?.apply {
                preguntas.forEach{
                    if (it.idpregunta >= aux && aux < 9) {
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
                Log.i("Cocinas",cocinas.toString())
                etDiscrepanciasValue.setText("${cocinas.discrepancia}")
                tvIdSobrecargo.setText("${cocinas.numEmpleado}")
                tvNombreSobrecargo.setText("${cocinas.nombre}")
                ivFirmaCocina.apply {
                    visibility = View.GONE
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(cocinas.firmaB64))
                }
                tilNumSobrecargo.prefixText = ""
                this@CocinasFragment.cocinas = cocinas
            }
        }
        disableForm()
    }

    private fun disableForm() {
        var boxes: Array<CheckBox> = optionsBoxes + optionsBoxesMal
        boxes.forEach { it.isEnabled = false }
        buttons.forEach { it.isEnabled = false }
        binding.apply {
            etDiscrepanciasValue.isEnabled = false
            editText.endIconMode = TextInputLayout.END_ICON_NONE
            tvIdSobrecargo.isEnabled = false
            tvNombreSobrecargo.isEnabled = false
            swEmpExterno.isEnabled = false
            actvOficialNombreOficial.isEnabled = false
            ivFirmaCocina.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.border_for_image_view_signature_gray,
                null
            )
            buttons.forEach {
                it.isEnabled = false
                it.visibility = View.GONE
            }
            tilNumSobrecargo.prefixTextView.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.color_gray_white_background_checklist,
                    null
                )
            )
        }
    }
    private fun clearForm() {
        binding.apply {
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = !buttonState
            resetSobrecargoValues()
            tvIdSobrecargo.setText("")
            tvNombreSobrecargo.setText("")
            unableSignatureImageView(true, ivFirmaCocina)
            btnFirmar.visibility = View.GONE
            tvIdSobrecargo.isEnabled = true
            btnGuardarLocal.apply {
                visibility = View.GONE
                isEnabled = false
            }

            tvIdSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun resetSobrecargoValues() {
        cocinas.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
        }
    }


    private fun findSobrecargo(v: View) {
        v.hideKeyboard()
        val diag = Dialogo(requireContext())
        binding.apply {
            if(tvIdSobrecargo.text.isNullOrEmpty() || tvIdSobrecargo.text.length < 4){
                tilNumSobrecargo.error = "Ingresa un número de empleado valido"
            } else {
                diag.mostrarCargando("Buscando sobrecargo, espere.")
                tilNumSobrecargo.isErrorEnabled = false
                model.getEmpleado(tvIdSobrecargo.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombreSobrecargo.text = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            cocinas.apply {
                                creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                            }
                            unableSignatureImageView(false, ivFirmaCocina)
                            btnFirmar.visibility = View.VISIBLE
                            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                            img?.setBounds(0, 0, 60, 60)
                            btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
                            buttonState =! buttonState
                            tvIdSobrecargo.apply {
                                isEnabled = false
                                setTextColor(resources.getColor(R.color.black))
                            }
                        } else {
                            diag.mostrarError("Sobrecargo no encontrado", "Verifica que el número de empleado ${tvIdSobrecargo.text} sea correcto.")
                            diag.btnCerrar.setOnClickListener{
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

        optionsBoxesMal.forEach {if (it.isChecked) discre = true }
        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener{ diag.Ocultar() }
        binding.apply {
            if (!allBoxesSelected(optionsBoxes, optionsBoxesMal)) {

                diag.mostrarError(
                    "Preguntas vacias",
                    "Para poder guardar el formulario debes hacer check en todo el formulario."
                )

            } else if (discre && etDiscrepanciasValue.text.toString().length < 5) {
                diag.mostrarError(
                    "Discrepancia vacia",
                    "Debes ingresar detalles de la discrepancia que has marcado.\nIngresa por lo menos 5 caracteres."
                )
            } else if (!aeEmployee && tvIdSobrecargo.text.toString() == "") {
                diag.mostrarError(
                    "Sobrecargo no valido",
                    "Para poder guardar el formulario debes ingresar un número de empleado."
                )

            } else if (aeEmployee && (tvIdSobrecargo.text.toString() == "" || actvOficialNombreOficial.text.toString() == "")) {
                diag.mostrarError(
                    "Sobrecargo no valido",
                    "Para poder guardar el formulario debes ingresar un número y nombre de empleado."
                )

            } else if (cocinas.firmaB64 == "") {
                diag.mostrarError(
                    "Firma vacia",
                    "Para poder guardar el formulario debe estar firmado."
                )

            } else {
                cocinas.apply {
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                    discrepancia = "${binding.etDiscrepanciasValue.text}"
                    llenarFromulario(optionsBoxes, preguntas5to8)
                    if (swEmpExterno.isChecked) {
                        nombre = actvOficialNombreOficial.text.toString()
                        numEmpleado = tvIdSobrecargo.text.toString()
                        creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                    }
                    if (getRequestBody.invoke().piloto.nombre != "" || getRequestBody.invoke().oficial.nombre != "" || getRequestBody.invoke().sobrecargo.nombre != "")
                        addSobrecargoToDB(cocinas) else guardarFormBD(this)
                    disableForm()
                }
            }
        }
    }

    private fun addSobrecargoToDB(cocinas: Cocinas) {
        var entra=false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                if(Gson().fromJson(it.request, RequestFirstFlightForm::class.java).flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber)
                    Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                        entra = true
                        this.cocinas = cocinas
                        this.preguntas.addAll(preguntas5to8)
                        this.preguntas.forEach{preg -> Log.e("preguntasSob", "${preg.idpregunta} ${preg.condicion}")}
                        updateBD(CheckPrimeVueloEntity(request = Gson().toJson(this).toString(), id = it.id))
                        return@forEach
                    }
            }
            if (!entra)
                guardarFormBD(cocinas)
        }
    }

    private fun updateBD(check: CheckPrimeVueloEntity) {
        model.updateForm(check).observeOnce{
            Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
            binding.btnEnviarForm.isVisible = true
        }
    }

    private fun guardarFormBD(cocinas: Cocinas) {
        Log.e("sobrecargoFR", "Dentro de guardarBD")
        getRequestBody.invoke().apply {
            this.cocinas = cocinas
            Log.i("Preguntas Antes", preguntas.toString())
            Log.i("Preguntas cocina", preguntas5to8.toString())
            preguntas.addAll(preguntas5to8)
            Log.i("Preguntas Despues", preguntas.toString())
            flightReferenceNumber = getRequestBody.invoke().flightReferenceNumber
            model.addFormToDB(this).observe(viewLifecycleOwner, {
                Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
                binding.btnEnviarForm.isVisible = true
            })
        }
    }

    /*private fun setupSwitchesEmpleadoExterno() {
        binding.swEmpExterno.setOnCheckedChangeListener { button, isChecked ->
            clearForm()
            if(isChecked){
//                binding.ibRampa.visibility = View.GONE
                binding.tilNombreSobrecargo.visibility = View.VISIBLE
                binding.tilNombreSobrecargo.isEnabled = true
                binding.tilNumSobrecargo.prefixText = ""
                binding.tvNombreSobrecargo.setText("")
                unableSignatureImageView(false, binding.ivFirmaSobrecargo)
                binding.btnFirmar.visibility = View.VISIBLE
                val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                img?.setBounds(0, 0, 60, 60)
//                btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
                binding.btnGuardarLocal.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                buttonState = !buttonState
            }
            else{
                binding.tilNombreSobrecargo.visibility = View.INVISIBLE
                binding.tilNombreSobrecargo.isEnabled = false
                binding.tilNumSobrecargo.prefixText = "AM"
                binding.tvNombreSobrecargo.setText("")

//                binding.mbFirmaRampa.isEnabled = false
//                viewModel.detalleLirEntity.nombreRampa = ""
            }
        }
    }*/

    private fun showNoAeromexicoOficial() {
        binding.apply {
            tilNombreSobrecargoManual.visibility = View.VISIBLE
            tilNumSobrecargo.isErrorEnabled = false
            actvOficialNombreOficial.setText("")
            tvIdSobrecargo.setText("")
            btnConsultarEmpleado.visibility = View.GONE
            tvNombreSobrecargo.visibility = View.GONE
            btnFirmar.visibility = View.VISIBLE
            unableSignatureImageView(false, ivFirmaCocina)
            btnConsultarEmpleado.visibility = View.GONE
            btnConsultarEmpleado.isEnabled = false
            tvIdSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumSobrecargo.prefixTextView.apply {
                text = ""
            }
        }
        resetSobrecargoValues()
    }

    private fun showAeromexicoOficial() {
        binding.apply {
            tilNombreSobrecargoManual.visibility = View.GONE
            tvIdSobrecargo.setText("")
            btnConsultarEmpleado.visibility = View.VISIBLE
            btnConsultarEmpleado.isEnabled = true
            tvNombreSobrecargo.apply {
                visibility = View.VISIBLE
                text = ""
            }
            btnFirmar.visibility = View.GONE
            unableSignatureImageView(true, ivFirmaCocina)
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            binding.btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = true
            tvIdSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumSobrecargo.prefixTextView.apply {
                text = "AM"
            }
        }

        resetSobrecargoValues()
    }



    override fun onClick(v: View?) {
        binding.apply {
            when(v?.id){

                swEmpExterno.id -> {
                    if (swEmpExterno.isChecked)
                        showNoAeromexicoOficial()
                    else
                        showAeromexicoOficial()
                }

                btnConsultarEmpleado.id -> if (buttonState) findSobrecargo(v) else clearForm()

                btnFirmar.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaCocina.setImageBitmap(bitmap)
                        cocinas.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }

                    }.show(it, "SignatureFragment")
                }

                btnGuardarLocal.id -> onSaveLocal(swEmpExterno.isChecked)

                else -> setCheckBoxesLogic(optionsBoxesMal, optionsBoxes, v, requireContext())
            }
        }
    }

    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
    }

    override fun onDownMotionEvent() {

    }

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