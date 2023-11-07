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
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionSobrecargosBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Cocinas
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Pregunta
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Sobrecargo
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.*

class InspeccionSobrecargosFragment(
    private val getBindingprimerVuelo: () -> FragmentPrimerVueloDiaBinding,
    private val getRequestBody: () -> RequestFirstFlightForm,
    var enviarForm:() -> Unit
) : Fragment(), View.OnClickListener, ObservableScrollViewCallbacks {
    lateinit var binding: FragmentInspeccionSobrecargosBinding
    lateinit var optionsBoxesBueno: Array<CheckBox>
    lateinit var optionsBoxesMalo: Array<CheckBox>
    lateinit var butons: Array<TextView>
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var sobrecargo: Sobrecargo
    lateinit var itemsToHide: Array<View>
    lateinit var preguntas9to25: MutableList<Pregunta>
    lateinit var diag: Dialogo
    private var buttonState = true
    private var mActivity: MainActivity? = null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentInspeccionSobrecargosBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity
        model = PrimerVueloDiaViewModel()
        preguntas9to25 = mutableListOf()
        sobrecargo = Sobrecargo("", "", "", "", "", "")
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
            optionsBoxesMalo = arrayOf(cbPregunta9malo, cbPregunta10malo, cbPregunta11malo, cbPregunta12malo, cbPregunta13malo, cbPregunta14Malo, cbPregunta15malo, cbPregunta16malo, cbPregunta17malo, cbPregunta18malo, cbPregunta19malo, cbPregunta20malo, cbPregunta21malo, cbPregunta22malo, cbPregunta23malo, cbPregunta24malo, cbPregunta25malo)
            optionsBoxesBueno = arrayOf(cbPregunta9bueno, cbPregunta10bueno, cbPregunta11bueno, cbPregunta12bueno, cbPregunta13bueno, cbPregunta14Bueno, cbPregunta15bueno, cbPregunta16bueno, cbPregunta17bueno, cbPregunta18bueno, cbPregunta19bueno, cbPregunta20bueno, cbPregunta21bueno, cbPregunta22bueno, cbPregunta23Bueno, cbPregunta24bueno, cbPregunta25bueno)
            butons = arrayOf(btnConsultarSobrecargo, btnFirmar, btnGuardarLocal)
            swEmpExterno.setOnClickListener(this@InspeccionSobrecargosFragment)

            for(box in optionsBoxesMalo) box.setOnClickListener(this@InspeccionSobrecargosFragment)
            for(box in optionsBoxesBueno) box.setOnClickListener(this@InspeccionSobrecargosFragment)
            butons.forEach { it.setOnClickListener(this@InspeccionSobrecargosFragment)}

            tilNumSobrecargo.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
            }

         //   scrollView.setScrollViewCallbacks(this@InspeccionSobrecargosFragment)
            for(i in 9 .. 25) preguntas9to25.add(Pregunta(0, i))

            actvSobrecargo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(actvSobrecargo.text.toString().isNotEmpty())
                        if(actvSobrecargo.text.toString()[0] == '0' && actvSobrecargo.text.toString().length > 1) {
                            actvSobrecargo.setText(
                                actvSobrecargo.text.toString().toInt().toString()
                            )
                            actvSobrecargo.setSelection(actvSobrecargo.text.toString().length)
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

                swEmpExterno.id -> {
                    if (swEmpExterno.isChecked)
                        showNoAeromexicoOficial()
                    else
                        showAeromexicoOficial()
                }

                btnConsultarSobrecargo.id -> if (buttonState) findSobrecargo(v) else clearForm()

                btnFirmar.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaSobrecargo.setImageBitmap(bitmap)
                        sobrecargo.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                    }.show(it, "SignatureFragment")
                }

                btnGuardarLocal.id -> onSaveLocal(swEmpExterno.isChecked)

                else -> setCheckBoxesLogic(optionsBoxesMalo, optionsBoxesBueno, v, requireContext())
            }
        }
    }

    private fun loadSS() {
        var entra = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                entra = true
                var rq = Gson().fromJson(it.request, RequestFirstFlightForm::class.java)
                if (rq.flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber){
                   if(rq.sobrecargo.nombre != "" && getRequestBody.invoke().sobrecargo.nombre.isNullOrEmpty())//cambio GET FORMULARIO LLENADO
                    Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                        loadFromDB(this)
                        return@forEach
                    }
                   else if (rq.sobrecargo.nombre.isNotEmpty() && getRequestBody.invoke().sobrecargo.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                       loadFromDB(getRequestBody.invoke())
//                       addSobrecargoToDB(getRequestBody.invoke().sobrecargo)
                   }
                   else if (rq.sobrecargo.nombre.isNullOrEmpty() && getRequestBody.invoke().sobrecargo.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
                       loadFromDB(getRequestBody.invoke())

                }
                else if (getRequestBody.invoke().sobrecargo.nombre.isNotEmpty()){//cambio GET FORMULARIO LLENADO
                    loadFromDB(getRequestBody.invoke())
                }

            }
        }

        if (!entra && getRequestBody.invoke().sobrecargo.nombre.isNotEmpty())//cambio GET FORMULARIO LLENADO
            loadFromDB(getRequestBody.invoke())

        model = PrimerVueloDiaViewModel()
    }

    private fun loadFromDB(requestFirstFlightForm: RequestFirstFlightForm?) {
        var aux = 9
        var i = 0

        binding.apply {
            requestFirstFlightForm?.apply {
                preguntas.forEach{
                    if (it.idpregunta >= aux && aux < 26) {
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
                etDiscrepanciasValue.setText("${sobrecargo.discrepancia}")
                actvSobrecargo.setText("${sobrecargo.numEmpleado}")
                tvNombreSobrecargo.text = "${sobrecargo.nombre}"
                ivFirmaSobrecargo.apply {
                    visibility = View.GONE
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(sobrecargo.firmaB64))
                }
                tilNumSobrecargo.prefixText = ""
                this@InspeccionSobrecargosFragment.sobrecargo = sobrecargo
            }
        }
        disableForm()
    }

    private fun clearForm() {
        binding.apply {
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarSobrecargo.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = !buttonState
            resetSobrecargoValues()
            actvSobrecargo.setText("")
            tvNombreSobrecargo.text = ""
            unableSignatureImageView(true, ivFirmaSobrecargo)
            btnFirmar.visibility = View.GONE
            actvSobrecargo.isEnabled = true
            btnGuardarLocal.apply {
                visibility = View.GONE
                isEnabled = false
            }

            actvSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun resetSobrecargoValues() {
        sobrecargo.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
        }
    }
    /*private fun gatherDataEmpExterno(): Sobrecargo {
        binding.apply {
            if (actvSobrecargo.text.isNullOrEmpty() || actvSobrecargo.text.length < 4) {
                tilNombreSobrecargo.error = "Ingresa un número de empleado valido"
            } else {

                tilNombreSobrecargo.isErrorEnabled = false
                tilNombreSobrecargo.visibility = View.VISIBLE
                tilNombreSobrecargo.isEnabled = false

                sobrecargo.apply {
                    creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                    nombre = tvNombreSobrecargo.editableText.toString()
                    numEmpleado = "AM" + tilNumSobrecargo.editText
                }


            }
        }
        return sobrecargo
    }*/

    private fun findSobrecargo(v: View) {
        v.hideKeyboard()
        val diag = Dialogo(requireContext())
        binding.apply {
            if(actvSobrecargo.text.isNullOrEmpty() || actvSobrecargo.text.length < 4){
                tilNumSobrecargo.error = "Ingresa un número de empleado valido"
            } else {
                diag.mostrarCargando("Buscando sobrecargo, espere.")
                tilNumSobrecargo.isErrorEnabled = false
                model.getEmpleado(actvSobrecargo.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombreSobrecargo.text = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            sobrecargo.apply {
                                creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                            }
                            unableSignatureImageView(false, ivFirmaSobrecargo)
                            btnFirmar.visibility = View.VISIBLE
                            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                            img?.setBounds(0, 0, 60, 60)
                            btnConsultarSobrecargo.setCompoundDrawablesRelative(img, null, null, null)
                            buttonState =! buttonState
                            actvSobrecargo.apply {
                                isEnabled = false
                                setTextColor(resources.getColor(R.color.black))
                            }
                        } else {
                            diag.mostrarError("Sobrecargo no encontrado", "Verifica que el número de empleado ${actvSobrecargo.text} sea correcto.")
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

        optionsBoxesMalo.forEach {if (it.isChecked) discre = true }
        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener{ diag.Ocultar() }
        binding.apply {
            if (!allBoxesSelected(optionsBoxesBueno, optionsBoxesMalo)) {

                diag.mostrarError(
                    "Preguntas vacias",
                    "Para poder guardar el formulario debes hacer check en todo el formulario."
                )

            } else if (discre && etDiscrepanciasValue.text.toString().length < 5) {
                diag.mostrarError(
                    "Discrepancia vacia",
                    "Debes ingresar detalles de la discrepancia que has marcado.\nIngresa por lo menos 5 caracteres."
                )
            } else if (!aeEmployee && actvSobrecargo.text.toString() == "") {
                diag.mostrarError(
                    "Sobrecargo no valido",
                    "Para poder guardar el formulario debes ingresar un número de empleado."
                )
            } else if (aeEmployee && (actvSobrecargo.text.toString() == "" || actvOficialNombreOficial.text.toString() == "")) {
                diag.mostrarError(
                    "Sobrecargo no valido",
                    "Para poder guardar el formulario debes ingresar un número y nombre de empleado."
                )

            } else if (sobrecargo.firmaB64 == "") {
                diag.mostrarError(
                    "Firma vacia",
                    "Para poder guardar el formulario debe estar firmado."
                )

            } else {
                sobrecargo.apply {
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance())
                    discrepancia = "${binding.etDiscrepanciasValue.text}"
                    llenarFromulario(optionsBoxesBueno, preguntas9to25)
                    if (swEmpExterno.isChecked) {
                        nombre = actvOficialNombreOficial.text.toString()
                        numEmpleado = actvSobrecargo.text.toString()
                        creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
                    }
                    if (getRequestBody.invoke().piloto.nombre != "" || getRequestBody.invoke().oficial.nombre != "" || getRequestBody.invoke().cocinas.nombre != "")
                        addSobrecargoToDB(sobrecargo) else guardarFormBD(this)
                    disableForm()
                }
            }
        }
    }

    private fun disableForm() {
        var boxes: Array<CheckBox> = optionsBoxesBueno + optionsBoxesMalo
        boxes.forEach { it.isEnabled = false }
        butons.forEach { it.isEnabled = false }
        binding.apply {
            etDiscrepanciasValue.isEnabled = false
            editText.endIconMode = TextInputLayout.END_ICON_NONE
            actvSobrecargo.isEnabled = false
            tvNombreSobrecargo.isEnabled = false
            swEmpExterno.isEnabled = false
            actvOficialNombreOficial.isEnabled = false
            ivFirmaSobrecargo.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.border_for_image_view_signature_gray,
                null
            )
            butons.forEach {
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

    private fun addSobrecargoToDB(sobrecargo: Sobrecargo) {
        var entra = false
        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                if(Gson().fromJson(it.request, RequestFirstFlightForm::class.java).flightReferenceNumber == getRequestBody.invoke().flightReferenceNumber)
                    Gson().fromJson(it.request, RequestFirstFlightForm::class.java).apply {
                        entra = true
                        this.sobrecargo = sobrecargo
                        this.preguntas.addAll(preguntas9to25)
                        this.preguntas.forEach{preg -> Log.e("preguntasSob", "${preg.idpregunta} ${preg.condicion}")}
                        updateBD(CheckPrimeVueloEntity(request = Gson().toJson(this).toString(), id = it.id))
                        return@forEach
                    }
            }
            if (!entra)
                guardarFormBD(sobrecargo)
        }
    }

    private fun updateBD(check: CheckPrimeVueloEntity) {
        model.updateForm(check).observeOnce{
            Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
            binding.btnEnviarForm.isVisible = true
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

    private fun guardarFormBD(sobrecargo: Sobrecargo) {
        Log.e("sobrecargoFR", "Dentro de guardarBD")
        getRequestBody.invoke().apply {
            this.sobrecargo = sobrecargo
            Log.e("Preguntas sobrecargo",preguntas9to25.toString())
            Log.e("Preguntas general antes",preguntas.toString())
            preguntas.addAll(preguntas9to25)
            Log.e("Preguntas general desp",preguntas.toString())
            flightReferenceNumber = getRequestBody.invoke().flightReferenceNumber
            model.addFormToDB(this).observe(viewLifecycleOwner, {
                Snackbar.make(binding.root, "Se guardo exitosamente formulario.", Snackbar.LENGTH_SHORT).show()
                binding.btnEnviarForm.isVisible = true
            })
        }
    }
    private fun showNoAeromexicoOficial() {
        binding.apply {
            tilNombreSobrecargoManual.visibility = View.VISIBLE
            tilNumSobrecargo.isErrorEnabled = false
            actvOficialNombreOficial.setText("")
            actvSobrecargo.setText("")
            btnConsultarSobrecargo.visibility = View.GONE
            tvNombreSobrecargo.visibility = View.GONE
            btnFirmar.visibility = View.VISIBLE
            unableSignatureImageView(false, ivFirmaSobrecargo)
            btnConsultarSobrecargo.visibility = View.GONE
            btnConsultarSobrecargo.isEnabled = false
            actvSobrecargo.apply {
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
            actvSobrecargo.setText("")
            btnConsultarSobrecargo.visibility = View.VISIBLE
            btnConsultarSobrecargo.isEnabled = true
            tvNombreSobrecargo.apply {
                visibility = View.VISIBLE
                text = ""
            }
            btnFirmar.visibility = View.GONE
            unableSignatureImageView(true, ivFirmaSobrecargo)
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            binding.btnConsultarSobrecargo.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = true
            actvSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumSobrecargo.prefixTextView.apply {
                text = "AM"
            }
        }

        resetSobrecargoValues()
    }

    /*fun getSobrecargoToserver(): Sobrecargo {
        return sobrecargo
    }

    fun validateSobrecargoForm(): Boolean {
        var flag = true
        binding.apply {
            when {
                !allBoxesSelected(optionsBoxesBueno, optionsBoxesMalo) -> {diag.mostrarError("Formulario Sobrecargo incompleto", "Debes llenar todas las preguntas."); flag = false}
                actvSobrecargo.text.toString() == "" -> {diag.mostrarError("Formulario Sobrecargo incompleto", "Nombre del sobrecargo vacio"); flag = false}
                tvNombreSobrecargo.text.toString() == "" -> {diag.mostrarError("Formulario Sobrecargo incompleto", "Numero del sobrecargo vacio"); flag = false}
                sobrecargo.firmaB64 == "" -> {diag.mostrarError("Formulario Sobrecargo incompleto", "No esta firmado el formulario"); flag = false}
            }
        }
        return flag
    }*/

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