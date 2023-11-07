package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentOrdenCargaCombustibleBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.adapters.OrdenCargaViewPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.viewModel.OrdenCargaViewModel
import java.text.SimpleDateFormat
import com.google.gson.Gson
import ng.softcom.android.utils.ui.showToast
import java.util.*

class OrdenCargaCombustible : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentOrdenCargaCombustibleBinding
    lateinit var adapter: OrdenCargaViewPagerAdapter
    val args: OrdenCargaCombustibleArgs by navArgs()
    lateinit var mActivity: MainActivity
    private var actualPage = 0
    lateinit var rqOrdenCarga: RequestOrdenCarga
    lateinit var actualOrdenCargaEntity :CargacombustibleEntity
    lateinit var dialogo: Dialogo
    var flag = false
    private var percnotaSelected: Boolean = false
    var c = Calendar.getInstance()
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    lateinit var model: OrdenCargaViewModel
    var page: Int = 0
    var cargasExcedidas =0

    var isEditable = false


    private fun setGoToTab(i: Int) {
        page = i

        when (page) {

            0 -> {
                binding.apply {

                    vpOrdenCarga.currentItem = 0
                    vpOrdenCarga.visibility = View.VISIBLE

                    btnInformacionCombustible.textInCircle.visibility = View.VISIBLE
                    btnInformacionCombustible.iconPalomita.visibility = View.GONE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                    exitosoMessage.root.visibility = View.GONE
                }
            }
            1 -> {

                binding.apply {

                    vpOrdenCarga.currentItem = 1
                    vpOrdenCarga.visibility = View.VISIBLE

                    btnInformacionCombustible.textInCircle.visibility = View.GONE
                    btnInformacionCombustible.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                    exitosoMessage.root.visibility = View.GONE
                }
            }
            3->{
                binding.apply {

                    vpOrdenCarga.currentItem = 1
                    vpOrdenCarga.visibility = View.VISIBLE

                    btnInformacionCombustible.textInCircle.visibility = View.GONE
                    btnInformacionCombustible.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.GONE
                    btnConcentimiento.iconPalomita.visibility = View.VISIBLE

                    exitosoMessage.root.visibility = View.GONE
                }
            }
            2 -> {
                binding.apply {

                    if (cargasExcedidas>=5) {
                        excedidoMessage.apply {
                            root.visibility = View.VISIBLE
                            textMensaje.text =
                                "Este vuelo ya cuenta con las cargas máximas permitidas, valida la información"
                            folio.text = "¡Aviso!"

                        }
                        binding.apply {
                            btnInformacionCombustible.root.visibility = View.GONE
                            btnConcentimiento.root.visibility = View.GONE
                        }
                    } else {
                        exitosoMessage.apply {
                            root.visibility = View.VISIBLE
                            imagenCentral.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_bomba_de_gasolina,
                                null
                            )
                            textMensaje.text =
                                "Orden de carga de combustible se ha enviado correctamente."
                        }
                    }
                    btnRegresar.visibility = View.GONE
                    vpOrdenCarga.apply {
                        hideKeyboard()
                        visibility = View.INVISIBLE
                    }
                    btnConcentimiento.apply {
                        textInCircle.visibility = View.GONE
                        iconPalomita.visibility = View.VISIBLE
                        root.isEnabled = false
                    }
                    btnInformacionCombustible.apply {
                        textInCircle.visibility = View.GONE
                        iconPalomita.visibility = View.VISIBLE
                        root.isEnabled = false
                    }
                    btnContinuar.apply {
                        text = "Aceptar"
                        background = resources.getDrawable(R.drawable.green_background_rounded)
                    }
                    percnota.visibility = View.GONE

                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdenCargaCombustibleBinding.inflate(inflater, container, false)
        rqOrdenCarga = RequestOrdenCarga()
        model = OrdenCargaViewModel()
        dialogo = Dialogo(requireContext())
        dialogo.mostrarCargando(getString(R.string.cargando))

        rqOrdenCarga.apply {
            creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!
            guidVuelo = args.vuelo!!.guid
            percnota = flag
            horaRegistro = df.format(c.time)
        }

        model.checkOrdenCarga(rqOrdenCarga.guidVuelo).observeOnce( androidx.lifecycle.Observer {
            if(it!= null && it.code == 500){
                showToast("${it.message}")

            }else if (it!= null && it.result != null){
               var rq = it.result.ordenCarga
                rq.isForService = true

                if(rq.veces<5)
                    rq.isPendingToSend = true

                rqOrdenCarga =  rq
                cargarFormulario(isEditable,rq )

            }else{
                verificarExistenciaLocal()
            }
        })


        binding.vpOrdenCarga.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.vpOrdenCarga.parent.requestDisallowInterceptTouchEvent(false)
            false
        })

        binding.vpOrdenCarga.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.root.hideKeyboard()
                setGoToTab(position)
            }
        })

        binding.apply {

            btnInformacionCombustible.apply {
                textInCircle.text = "1"
                textNameButton.text = getString(R.string.informacion_de_combustible)
                //root.setOnClickListener(this@OrdenCargaCombustible)
            }

            btnConcentimiento.apply {
                textInCircle.text = "2"
                textNameButton.text = getString(R.string.consentimiento_combustible)
                //root.setOnClickListener(this@OrdenCargaCombustible)
            }
            btnInformacionCombustible.root.setOnClickListener {
                setGoToTab(0)
            }
            btnConcentimiento.root.setOnClickListener {
                setGoToTab(1)
            }

            btnRegresar.setOnClickListener(this@OrdenCargaCombustible)
            btnContinuar.setOnClickListener(this@OrdenCargaCombustible)
            percnota.setOnClickListener(this@OrdenCargaCombustible)
        }

        setupActionBar()
        setBackButtonSystem(requireActivity())
        setHeaders()

        return binding.root
    }

    fun enviarBtn() {
        rqOrdenCarga = adapter.informacionCombustible.rqOrdenCarga

        rqOrdenCarga = adapter.combustibleConsentimiento.setRqValues(rqOrdenCarga)
        if (validateOficialOperaciones() && validateMecanico() && validateSpinners())
            enviarForm(rqOrdenCarga)
        else
            showDialog()
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {
                btnConcentimiento.root.id -> goToTab(1, false)

                btnInformacionCombustible.root.id -> goToTab(0, false)

                btnRegresar.id -> {
                    if (actualPage == 1) {
                        goToTab(actualPage - 1, false)
                        actualPage -= 1
                    } else returnToHome()
                }

                btnContinuar.id -> {

                    if (validateFormInfoCombustible()) {
                        if (actualPage == 0) {
                            actualPage += 1
                            goToTab(actualPage, false)
                        } else if (actualPage == 1) {
                            rqOrdenCarga = adapter.combustibleConsentimiento.setRqValues(rqOrdenCarga)
                            if (validateOficialOperaciones() && validateMecanico() && validateSpinners())
                                enviarForm(rqOrdenCarga)
                        } else returnToHome()
                    } else {
                        btnContinuar.hideKeyboard()
                        showDialog()
                    }
                }

                R.id.percnota -> {
                    if (percnotaSelected) {
                        percnota.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_moon, null)
                        percnotaSelected = false
                    } else {
                        percnota.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_moon_blue, null)
                        percnotaSelected = true
                    }
                }
            }
        }
    }
    fun updateFormBD( form: RequestOrdenCarga){
        if(form.isForService){
            enviarForm(form)
        }else{
            //cada cambio se guarda en local
            actualOrdenCargaEntity.request = Gson().toJson(form)
            Log.w("UpdateBD",""+actualOrdenCargaEntity.toString())
            UpdateBD(actualOrdenCargaEntity)
        }
    }
    private fun UpdateBD(actualOrdenCargaEntity: CargacombustibleEntity) {
        model.updateCargaCOmbustible(actualOrdenCargaEntity)
        //showToast("BD Actualizado")
        Log.w("UpdateBD Actualizado",""+actualOrdenCargaEntity.toString())


    }

    private fun cargarFormulario(editable: Boolean, form: RequestOrdenCarga?) {

        binding.vpOrdenCarga.invalidate()
        if (form != null) {
            adapter = OrdenCargaViewPagerAdapter(
                requireActivity().supportFragmentManager,
                args.vuelo,
                form!!,
                ::updateFormBD,
                ::enviarBtn
            )
            binding.vpOrdenCarga.adapter = adapter
        }
        dialogo.Ocultar()


    }
    private fun verificarExistenciaLocal() {

        model.readAllCarfaCombustible().observeOnce( androidx.lifecycle.Observer {
            var findForm: RequestOrdenCarga? = null

            it.forEach { entity ->
                var orderBD = Gson().fromJson(entity.request, RequestOrdenCarga::class.java)
                if (orderBD != null) {
                    if (orderBD.guidVuelo.equals(rqOrdenCarga.guidVuelo)) {
                        Log.w("Form encontrado",rqOrdenCarga.toString())
                       // showToast("Form encontrado")
                        findForm = orderBD
                        actualOrdenCargaEntity = entity
                    }
                }
            }
            if(findForm!=null){
                //cargar vista para edicion
                isEditable = true
                cargarFormulario(isEditable, findForm)
            }else{
             //   showToast("Guardan primera vez")
                Log.w("Savefirst",rqOrdenCarga.toString())
                guardarenBDForm(rqOrdenCarga)
                cargarFormulario(isEditable, rqOrdenCarga)

            }
        })
    }

    private fun validateFormInfoCombustible(): Boolean {
        return adapter.informacionCombustible.validarCampos()
    }

    private fun enviarForm(requestOrdenCarga: RequestOrdenCarga) {
        rqOrdenCarga.apply {
            percnota = percnotaSelected
            c = Calendar.getInstance()
            fechaCreacion = df.format(c.time)
        }
        val diag = Dialogo(requireContext())
        diag.mostrarCargando("Enviando formulario")
        model.insertarOrdenCarga(requestOrdenCarga).observeOnce {
            diag.Ocultar()
            if (it != null) {
                if (it.error) {
                    diag.mostrarError("Error al enviar formulario", "${it.errorMessage}")
                } else if (it.result.ordenCarga.cargasExcedidas) {
                    actualPage += 1
                    cargasExcedidas = 5
                    binding.exitosoMessage.folio.text = "Se han superado las 5 cargas"
                    setGoToTab(2)
                 //   goToTab(actualPage, it.result.ordenCarga.cargasExcedidas)
                } else {
                    setGoToTab(2)
                    actualPage += 1
                    binding.exitosoMessage.folio.text =
                        "Folio: ${it.result.ordenCarga.idOrdenCarga.toString()}"
                   if(!requestOrdenCarga.isForService)
                    model.deleteCargaCombustiblecheckById(actualOrdenCargaEntity.id)

                    //  goToTab(actualPage, false)
                }
            } else {
                diag.mostrarError(
                    "Sin conexión",
                    "Verifica tu enlace a internet, el formulario se guardara de manera local."
                )
                if(!requestOrdenCarga.isForService){
                    requestOrdenCarga.isPendingToSend = true
                    updateFormBD(requestOrdenCarga)
                    diag.btnCerrar.setOnClickListener {
                        startActivity(
                            Intent(
                                activity,
                                MainActivity::class.java
                            )
                        )
                    }
                }
                diag.btnCerrar.setOnClickListener {
                    diag.Ocultar()
                }


            }
        }
        model = OrdenCargaViewModel()
    }

    private fun validateOficialOperaciones(): Boolean {
        var flag = true
        val diag = Dialogo(requireContext())

        diag.btnCerrar.setOnClickListener { diag.Ocultar() }
        diag.btnAceptar.visibility = View.GONE
        rqOrdenCarga.apply {
            if (noEmpleadoOficialOperaciones == "") {
                diag.mostrarError(
                    "Concentimiento incompleto",
                    "Debes ingresar un numero de oficial de operaciones."
                )
                flag = false
            } else if (nombreOficialOperaciones == "") {
                diag.mostrarError(
                    "Concentimiento incompleto",
                    "Debes ingresar el nombre del oficial de operaciones."
                )
                flag = false
            } else if (!rqOrdenCarga.isForService && firmaB64Operaciones == "") {
                diag.mostrarError(
                    "Concentimiento incompleto",
                    "Debes ingresar la firma del oficial de operaciones."
                )
                flag = false
            }
        }
        return flag
    }

    private fun validateMecanico(): Boolean {
        var flag = true
        val diag = Dialogo(requireContext())

        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener { diag.Ocultar() }
        rqOrdenCarga.apply {
            if (noEmpleadoMecanico != "") {
                if (nombreMecanico == "") {
                    diag.mostrarError(
                        "Concentimiento incompleto",
                        "Debes ingresar el nombre de mecánico."
                    )
                    flag = false
                } else if (!rqOrdenCarga.isForService && firmaB64Mecanico == "") {
                    diag.mostrarError(
                        "Concentimiento incompleto",
                        "Debes ingresar la firma del mecánico."
                    )
                    flag = false
                }
            } else flag = true
        }
        return flag
    }

    fun validateSpinners(): Boolean {
        var flag = true
        val diag = Dialogo(requireContext())

        diag.btnAceptar.visibility = View.GONE
        diag.btnCerrar.setOnClickListener { diag.Ocultar() }

        if (rqOrdenCarga.extraFuel == "Solicitud del capitan por") {
            diag.mostrarError(
                "Concentimiento incompleto",
                "Debes seleccionar una opcion para solicitud del capitan por"
            )
            flag = false
        }
        return flag
    }

    private fun guardarenBDForm(requestOrdenCarga: RequestOrdenCarga) {

        model.guardarOrdenCarga(
            requestOrdenCarga
        ).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
           // showToast("code save BD $it")
            Log.w("code save BD $it",rqOrdenCarga.toString())
            actualOrdenCargaEntity = CargacombustibleEntity(it.toInt(),Gson().toJson(requestOrdenCarga))
        })


    }

    private fun returnToHome() {
        startActivity(Intent(activity, MainActivity::class.java))
    }

    private fun goToTab(page: Int, cargasExcedidas: Boolean) {
        binding.apply {
            val params: ViewGroup.LayoutParams = binding.vpOrdenCarga.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT

            when (page) {
                0 -> {
                    btnContinuar.hideKeyboard()
                    btnContinuar.apply {
                        text = getString(R.string.siguiente)
                        background =
                            resources.getDrawable(R.drawable.background_azul_cuadrado_esquinasredondas)
                    }
                    btnInformacionCombustible.apply {
                        textInCircle.visibility = View.VISIBLE
                        iconPalomita.visibility = View.GONE
                    }
                    btnConcentimiento.apply {
                        textInCircle.visibility = View.VISIBLE
                        iconPalomita.visibility = View.GONE
                    }
                    vpOrdenCarga.apply {
                        currentItem = 0
                        visibility = View.VISIBLE
                    }
                    vpOrdenCarga.apply {
                        currentItem = 0
                        params.height = 1200
                    }
                }

                1 -> {
                    btnContinuar.apply {
                        hideKeyboard()
                        text = getString(R.string.text_finalizar_btn)
                        background =
                            resources.getDrawable(R.drawable.background_rojo_cuadrado_esquinasredondas)
                    }
                    btnInformacionCombustible.apply {
                        textInCircle.visibility = View.GONE
                        iconPalomita.visibility = View.VISIBLE
                    }
                    vpOrdenCarga.apply {
                        currentItem = 1
                        params.height = if (args.vuelo?.companiaEquipo == "5D") 950 else 1500
                    }
                }

                2 -> {
                    if (cargasExcedidas) {
                        excedidoMessage.apply {
                            root.visibility = View.VISIBLE
                            textMensaje.text =
                                "Este vuelo ya cuenta con las cargas máximas permitidas, valida la información"
                            folio.text = "¡Aviso!"

                        }
                        binding.apply {
                            btnInformacionCombustible.root.visibility = View.GONE
                            btnConcentimiento.root.visibility = View.GONE
                        }
                    } else {
                        exitosoMessage.apply {
                            root.visibility = View.VISIBLE
                            imagenCentral.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_bomba_de_gasolina,
                                null
                            )
                            textMensaje.text =
                                "Orden de carga de combustible se ha enviado correctamente."
                        }
                    }
                    btnRegresar.visibility = View.GONE
                    vpOrdenCarga.apply {
                        hideKeyboard()
                        params.height = 750
                        visibility = View.INVISIBLE
                    }
                    btnConcentimiento.apply {
                        textInCircle.visibility = View.GONE
                        iconPalomita.visibility = View.VISIBLE
                        root.isEnabled = false
                    }
                    btnInformacionCombustible.apply {
                        textInCircle.visibility = View.GONE
                        iconPalomita.visibility = View.VISIBLE
                        root.isEnabled = false
                    }
                    btnContinuar.apply {
                        text = "Aceptar"
                        background = resources.getDrawable(R.drawable.green_background_rounded)
                    }
                    percnota.visibility = View.GONE

                }
            }
        }
    }

    private fun showDialog() {
        val diag = Dialogo(requireContext())
        diag.apply {

            btnAceptar.visibility = View.GONE
            mostrarAviso("Aviso", "Verifica llenar los campos obligatorios antes de continuar.")
            btnCerrar.setOnClickListener { diag.Ocultar() }
        }

    }

    private fun setHeaders() {
        args.let {
            binding.apply {
                tvDateFlightValue.text = it.vuelo?.fechaVueloLocal
                tvFlightNumberValue.text = it.vuelo?.numVuelo.toString()
                tvEnrollmentValue.text = it.vuelo?.matricula
                tvRouteValue.text = "${it.vuelo?.origen}-${it.vuelo?.destino}"
            }
        }
    }

    private fun setupActionBar() {
        mActivity = (activity as? MainActivity)!!
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.orden_carga_combustible)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(activity, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
    }
}