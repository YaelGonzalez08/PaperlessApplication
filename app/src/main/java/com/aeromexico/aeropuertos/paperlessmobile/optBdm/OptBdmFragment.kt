package com.aeromexico.aeropuertos.paperlessmobile.optBdm

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.OptBdmFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.PrimerVueloDiaFragmentArgs
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities.Piloto
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities.Planificador
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities.RequestOptBdmForm
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.log

class OptBdmFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: OptBdmViewModel
    lateinit var binding: OptBdmFragmentBinding
    lateinit var piloto: Piloto
    lateinit var planificador: Planificador
    private var buttonState: Boolean = true
    val args: OptBdmFragmentArgs by navArgs()
    lateinit var buttons: Array<TextView>
    var flightReferenceNumber:Long=0
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OptBdmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as? MainActivity
        setupActionBar()
        viewModel = OptBdmViewModel()
        setHeaders()
        flightReferenceNumber = args.vuelo.flightReferenceNumber
        viewModel.getInfoOptBdm(args.vuelo.flightReferenceNumber)
        observers()
        piloto = Piloto("","","","")
        planificador=Planificador("","", "","")

        binding.apply {
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

            swEmpExterno.setOnClickListener(this@OptBdmFragment)
            buttons = arrayOf(btnConsultarPiloto,btnConsultarEmpleado, btnFirmarPiloto,btnFirmarSobrecargo, btnGuardarLocal)
            buttons.forEach { it.setOnClickListener(this@OptBdmFragment) }
            tilNumSobrecargo.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                gravity = Gravity.CENTER
            }
            tilNumPiloto.prefixTextView.apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                gravity = Gravity.CENTER
            }
        }
    }
    private fun observers(){
        viewModel.responseEstado!!.observe(viewLifecycleOwner,{
//            var dialogo = Dialogo(requireContext())
            if (it.state == RequestState.REQ_IN_PROGRESS){
//                dialogo.mostrarCargando(getString(R.string.cargando))
                /*Snackbar.make(mBinding.root, "State en progreso ${it.state}", Snackbar.LENGTH_SHORT)
                        .show()*/
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){

                Log.i("EstaDo OPT",it.state)
                if(it.state == RequestState.REQ_BAD) {
                    var diag = Dialogo(requireContext())
                    diag.mostrarError(getString(R.string.no_hay_mensajes_disponibles),
                        getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                    diag.btnCerrar.setOnClickListener {
                        diag.Ocultar()
                    }

                    binding.moduloFirmaOpt.visibility = View.GONE

                }
//                dialogo.Ocultar()
                Snackbar.make(binding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.responseGetInfo.observe(viewLifecycleOwner,{
            var dialogo = Dialogo(requireContext())
            dialogo.Ocultar()
            Log.i("respuesta OPT ",it.toString())
            if (it.status == RequestState.REQ_OK) {
                if(it.result.creado) {
                    binding.tvZFWInfo.text = it.result.zFW.toString()
                    binding.tvCGInfo.text = it.result.cG.toString()
                    if(it.result.tipo.toUpperCase().equals("EDM")){
                        binding.tvCG.visibility = View.INVISIBLE
                        binding.tvCGInfo.visibility = View.INVISIBLE
                    }
                    if(it.result.firmado)
                    {
                        binding.moduloFirmaOpt.visibility = View.GONE
                        binding.tvFirmado.visibility = View.VISIBLE
                    }
                }
                else{
                    binding.moduloFirmaOpt.visibility = View.GONE
                    binding.tvNoInfoOpt.visibility = View.VISIBLE
                    dialogo.mostrarAviso("Aviso","No hay información para este vuelo")
                    dialogo.btnCerrar.setOnClickListener{
                        dialogo.Ocultar()
                    }
                    dialogo.btnAceptar.visibility = View.GONE
                }

            }
            else{
                dialogo.mostrarError(getString(R.string.no_hay_mensajes_disponibles),
                    getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                }
            }
        })


    }
    private fun oberverEnvio(){
        viewModel.responseSend.observe(viewLifecycleOwner,{
            var dialogo = Dialogo(requireContext())
            if(it.status== RequestState.REQ_OK){
                dialogo.mostrarMensajeConfirmacion(
                    getString(R.string.mensaje_enviado),
                    getString(R.string.el_mensaje_se_envio_con_exito))
                dialogo.btnAceptar.setOnClickListener {
                    findNavController().popBackStack()
                    dialogo.Ocultar()
                }
                dialogo.btnCerrar.visibility = View.GONE

            }
            else{
                dialogo.mostrarError(getString(R.string.no_hay_mensajes_disponibles),
                    getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                }
            }
        })
    }

    private fun setHeaders() {
        args.let {
            binding.apply {
                includeDetalleVuelo.apply {
                    tvFechaVuelo.text = Fecha().stringToFecha(it.vuelo.fechaVueloLocal)
                    tvNumeroVuelo.text = it.vuelo.numVuelo.toString()
                    tvRuta.text="${it.vuelo.origen}-${it.vuelo.destino}"
                    tvMatricula.text=it.vuelo.matricula
                }
            }
        }
    }
    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "OPT-EDM"
        setHasOptionsMenu(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                startActivity(Intent(activity, MainActivity::class.java))
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findPiloto(v: View) {
        v.hideKeyboard()
        val diag = Dialogo(requireContext())
        binding.apply {
            if(tvIdPiloto.text.isNullOrEmpty() || tvIdPiloto.text.length < 4){
                tilNumPiloto.error = "Ingresa un número de empleado valido"
            } else {
                diag.mostrarCargando("Buscando Piloto, espere.")
                tilNumPiloto.isErrorEnabled = false
                viewModel.getEmpleado(tvIdPiloto.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombrePiloto.text = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            piloto.apply {

                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                            }
                            unableSignatureImageView(false, ivFirmaPilotos)
                            btnFirmarPiloto.visibility = View.VISIBLE
                            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)
                            img?.setBounds(0, 0, 60, 60)
                            btnConsultarPiloto.setCompoundDrawablesRelative(img, null, null, null)
//                            buttonState =! buttonState
                            tvIdPiloto.apply {
                                isEnabled = false
                                setTextColor(resources.getColor(R.color.black))
                            }
                        } else {
                            diag.mostrarError("Sobrecargo no encontrado", "Verifica que el número de empleado ${tvIdPiloto.text} sea correcto.")
                            diag.btnCerrar.setOnClickListener{
                                diag.Ocultar()
                            }
                        }
                    }
                })
            }
        }
        viewModel = OptBdmViewModel()
    }

    private fun findPlanificador(v: View) {
        v.hideKeyboard()
        val diag = Dialogo(requireContext())
        binding.apply {
            if(tvIdSobrecargo.text.isNullOrEmpty() || tvIdSobrecargo.text.length < 4){
                tilNumSobrecargo.error = "Ingresa un número de empleado valido"
            } else {
                diag.mostrarCargando("Buscando Planificador, espere.")
                tilNumSobrecargo.isErrorEnabled = false
                viewModel.getEmpleado(tvIdSobrecargo.text.toString()).observe(viewLifecycleOwner, {
                    diag.Ocultar()
                    if (it == null)
                        Snackbar.make(this.root, "Error revisa tu conexión", Snackbar.LENGTH_SHORT).show()
                    else {
                        if (it.status == RequestState.REQ_OK) {
                            tvNombreSobrecargo.text = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                            planificador.apply {

                                it.result.usuarioCore.apply {
                                    nombre = "$name $apellidoPaterno $apellidoMaterno"
                                    numEmpleado = "AM$employeeNumber"
                                }
                            }
                            unableSignatureImageView(false, ivFirmaSobrecargo)
                            btnFirmarSobrecargo.visibility = View.VISIBLE
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
        viewModel = OptBdmViewModel()
    }

    private fun showNoAeromexicoOficial() {
        binding.apply {
            tilNombreSobrecargoManual.visibility = View.VISIBLE
            tilNumSobrecargo.isErrorEnabled = false
            actvOficialNombreOficial.setText("")
            tvIdSobrecargo.setText("")
            btnConsultarEmpleado.visibility = View.GONE
            tvNombreSobrecargo.visibility = View.GONE
            btnFirmarSobrecargo.visibility = View.VISIBLE
            unableSignatureImageView(false, ivFirmaSobrecargo)
            btnConsultarEmpleado.visibility = View.GONE
            btnConsultarEmpleado.isEnabled = false
            tvIdSobrecargo.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumSobrecargo.prefixTextView.apply {
                text = ""
            }
            etRemarksPlanificadorValue.setText("")
        }
        resetPlanificadorValues()
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
            btnFirmarSobrecargo.visibility = View.GONE
            unableSignatureImageView(true, ivFirmaSobrecargo)
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
            etRemarksPlanificadorValue.setText("")
        }

        resetPlanificadorValues()
    }
    private fun resetPlanificadorValues() {
        planificador.apply {
            firmaB64 = ""
            nombre = ""
            numEmpleado = ""
            remark =""
        }
    }
    private fun clearForm() {
        binding.apply {
            val img = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
            img?.setBounds(0, 0, 60, 60)
            btnConsultarEmpleado.setCompoundDrawablesRelative(img, null, null, null)
            buttonState = !buttonState
            resetPlanificadorValues()
            tvIdSobrecargo.setText("")
            tvNombreSobrecargo.setText("")
            unableSignatureImageView(true, ivFirmaSobrecargo)
            btnFirmarSobrecargo.visibility = View.GONE
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
    private fun mostarBotonEnviar(){
        if(!planificador.firmaB64.isNullOrEmpty() && !piloto.firmaB64.isNullOrEmpty()){
            binding.btnGuardarLocal.apply {
                visibility = View.VISIBLE
                isEnabled = true
            }
        }
        else{
            binding.btnGuardarLocal.apply {
                visibility = View.GONE
                isEnabled = false
            }
        }
    }
    private fun validaFirmas(aeEmployee: Boolean){
        val diag = Dialogo(requireContext())
        binding.apply {
            if (tvIdPiloto.text.isNullOrEmpty()){
                diag.mostrarError(
                    "Piloto no valido",
                    "Para poder guardar el formulario debes ingresar un número de empleado."
                )
            }
            else if (!aeEmployee && tvIdSobrecargo.text.isNullOrEmpty()){
                diag.mostrarError(
                    "Planificador no valido",
                    "Para poder guardar el formulario debes ingresar un número de empleado."
                )
            }
            else if(aeEmployee && (tvIdSobrecargo.text.toString() == "" || actvOficialNombreOficial.text.toString() == "")){
                diag.mostrarError(
                    "Planificador no valido",
                    "Para poder guardar el formulario debes ingresar un número y nombre de empleado."
                )
            }
            else if(planificador.firmaB64.isNullOrEmpty() || piloto.firmaB64.isNullOrEmpty()){
                diag.mostrarError(
                    "Firma vacia",
                    "Para poder guardar el formulario debe estar firmado."
                )
            }
            else{
                if (aeEmployee){
                    planificador.apply {
                        numEmpleado = tvIdSobrecargo.text.toString()
                        nombre = actvOficialNombreOficial.text.toString()
                    }

                }
                planificador.remark = binding.etRemarksPlanificadorValue.text.toString()
                piloto.remark = binding.etRemarksPilotoValue.text.toString()
                var request: RequestOptBdmForm = RequestOptBdmForm(
                    flightReferenceNumber = flightReferenceNumber,
                    fechaCreacion = Fecha().calendarToString(Calendar.getInstance()),
                    creadoPor = MainActivity.getInstance()?.getUsuarioLogeado()?.userGuid!!,
                    planificador = planificador,
                    piloto = piloto

                )
                viewModel.sendDetalle(request)
                oberverEnvio()
            }
        }
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

                btnConsultarEmpleado.id -> if (buttonState) findPlanificador(v) else clearForm()
                btnConsultarPiloto.id -> if (buttonState) findPiloto(v) else clearForm()

                btnFirmarSobrecargo.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaSobrecargo.setImageBitmap(bitmap)
                        planificador.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        /*btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }*/
                        mostarBotonEnviar()
                    }.show(it, "SignatureFragment")
                }
                btnFirmarPiloto.id -> parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirmaPilotos.setImageBitmap(bitmap)
                        piloto.firmaB64 = CreateImageFile.getB64FromBitmap(bitmap)
                        /*btnGuardarLocal.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }*/
                        mostarBotonEnviar()
                    }.show(it, "SignatureFragment")
                }

                btnGuardarLocal.id -> validaFirmas(swEmpExterno.isChecked)

//                else -> setCheckBoxesLogic(optionsBoxesMal, optionsBoxes, v, requireContext())
            }
        }
    }

}