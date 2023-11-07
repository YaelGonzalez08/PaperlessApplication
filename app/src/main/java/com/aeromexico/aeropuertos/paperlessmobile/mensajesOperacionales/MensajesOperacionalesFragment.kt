package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.DialogPrintUtil
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.ScanningActivity
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.utilities.Printooth
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.SilkySignature.viewModel.SignatureViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.MensajesOperacionalesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.viewModel.MensajesOperacionalesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Msjs
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import java.text.SimpleDateFormat
import java.util.*

class MensajesOperacionalesFragment : Fragment() {

    private lateinit var mBinding: MensajesOperacionalesFragmentBinding
    private val mMainViewModel: MainViewModel by activityViewModels()
    private lateinit var viewModel: MensajesOperacionalesViewModel
    private  val mSignatureViewModel: SignatureViewModel by activityViewModels()
    lateinit var userStorage: DataStorageLogin
    private var sessionActive = MutableLiveData<Boolean>()
    private  var listaMensajes: List<String>? = listOf()
    private  var mensajes: List<Msjs>? = listOf()

    private var mActivity: MainActivity? = null
    private var lirEmpleadoActual = ""
    private var firmaSeleccionada: String = ""
    private var idInsertadoRoom: Long? = null
    private lateinit var dialogo: Dialogo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MensajesOperacionalesViewModel(
                MensajeOperacionalesRepository(
                        WebServiceApi().getMOApi()
                )
        )
        userStorage = DataStorageLogin(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = MensajesOperacionalesFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfoVuelo()
        viewModel.getMensajes(requireArguments().getLong("flightReferenceNumber"))
        observers()
        setupViewModel()
        setupActionBar()
        mBinding.mbFirmaRampa.setOnClickListener {
            viewModel.setBase64OfRampa("")
            launchFirmaFragment("oficialRampa")
        }

        //Despacho
        /*mBinding.mbFirmaDespacho.setOnClickListener {
            viewModel.setBase64Despacho("")
            launchFirmaFragment("oficialDespacho")
        }
        mBinding.ibDespacho.setOnClickListener {
            if(validaNoEmpleado()){
                viewModel.getEmpleado(mBinding.etNEDespacho.text.toString().trim())
                observersCore()
                it.hideKeyboard()
            }
        }*/
        mBinding.ibRampa.setOnClickListener {
            if(validaNoEmpleado()){
                viewModel.getEmpleado(mBinding.etNERampa.text.toString().trim())
                observersCore()
                it.hideKeyboard()
            }
        }
        setupSegmentoLir()

        mBinding.mbEnviar.setOnClickListener {
            mBinding.mbEnviar.isEnabled = false
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Está seguro de enviar el detalle del mensaje?")
                .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                    if (validaEntity()){
//                Toast.makeText(requireContext(),"Pasa validacion", Toast.LENGTH_SHORT).show()
                        viewModel.saveDetalleFirmasRoom()

                    }
                })
                .setNegativeButton(R.string.cancel,{dailogInterface, i ->
                    mBinding.mbEnviar.isEnabled = true
                })
                .show()

        }

        setupDialogo()

    }

    private fun setupDialogo() {
        dialogo = Dialogo(requireContext())
    }

    override fun onResume() {
        super.onResume()

        val firmaOfOp = this.viewModel.getBase64OfRampa().value
        if( firmaOfOp != null && firmaOfOp != ""){
            this.mostrarFirmaRampa(firmaOfOp)
        }

        /*val firmaTecMant = this.viewModel.getBase64Despacho().value
        if( firmaTecMant != null && firmaTecMant != ""){
            this.mostrarFirmaDespacho(firmaTecMant)
        }*/
    }
    private fun observers(){
        viewModel.responseState!!.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS){
                dialogo.mostrarCargando(getString(R.string.cargando))
                /*Snackbar.make(mBinding.root, "State en progreso ${it.state}", Snackbar.LENGTH_SHORT)
                        .show()*/
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogo.Ocultar()
                if(it.state == RequestState.REQ_BAD) {
                    dialogo.mostrarError(getString(R.string.no_hay_mensajes_disponibles),
                        getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                }
                Snackbar.make(mBinding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
                        .show()
            }
        })

        viewModel.responseMO.observe(viewLifecycleOwner,{
            if (it.status == RequestState.REQ_OK) {

                listaMensajes=it.result.tiposMsjs
                mensajes = it.result.msjs
                Log.i("Lista de mensajes",listaMensajes.toString())
                Log.i("mensajes",mensajes.toString())
                setupListMenu()
            }
            else{
                dialogo.mostrarError(getString(R.string.no_hay_mensajes_disponibles),
                    getString(R.string.intente_de_nuevo_mas_tarde))
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                }
            }
        })


    }
    @SuppressLint("SetTextI18n")
    private fun observersCore(){
        if (!viewModel.responseStateCore.hasActiveObservers()) {
            viewModel.responseStateCore.observe(viewLifecycleOwner, {
                if (it.state == RequestState.REQ_IN_PROGRESS) {
                    dialogo.mostrarCargando(getString(R.string.cargando))
                    /*Snackbar.make(mBinding.root, "State Core en progreso ${it.state}", Snackbar.LENGTH_SHORT)
                            .show()*/
                } else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK) {
                    dialogo.Ocultar()
                    if (it.state == RequestState.REQ_BAD) {
                        if (lirEmpleadoActual.equals("Rampa", true)) {
                            mBinding.etNERampa.error = getString(R.string.empleado_incorrecto)
                            mBinding.etNERampa.requestFocus()
                        } /*else {
                            mBinding.etNEDespacho.error = getString(R.string.empleado_incorrecto)
                            mBinding.etNEDespacho.requestFocus()
                        }*/
                        Snackbar.make(mBinding.root, "StateCore ${it.state}", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                }
            })
        }
        if (!viewModel.responseCore.hasActiveObservers()) {
            viewModel.responseCore.observe(viewLifecycleOwner, {
                if (!it.status.isNullOrEmpty() && it.status == RequestState.REQ_OK) {
                    if (lirEmpleadoActual.equals("Rampa", true)) {
                        mBinding.etNombreRampa.setText("${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}")
                        viewModel.detalleLirEntity.nombreRampa = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                        mBinding.mbFirmaRampa.isEnabled = true
                    } /*else {
                        mBinding.etNombreDespacho.setText("${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}")
                        viewModel.detalleLirEntity.nombreDespacho = "${it.result.usuarioCore.name} ${it.result.usuarioCore.apellidoPaterno} ${it.result.usuarioCore.apellidoMaterno}"
                        mBinding.mbFirmaDespacho.isEnabled = true
                    }*/
                    mBinding.mbEnviar.isEnabled = true
                    mBinding.root.hideKeyboard()
                }
            })
        }
    }


    private fun observersSendDetalleLir(){
        viewModel.responseStateEnvioFirma!!.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS)
                dialogo.mostrarCargando(getString(R.string.cargando))
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogo.Ocultar()
                if (it.state == RequestState.REQ_BAD){
                    mMainViewModel.setCambioFirmas()
                    mostrarErrorEnvio()
                }
            }
        })
        viewModel.responseMODetalle.observe(viewLifecycleOwner,{
            mMainViewModel.setCambioFirmas()
            if (it.status == RequestState.REQ_OK){
                viewModel.deleteDetalleFirmasRoom(idInsertadoRoom!!)
            }else if (it.status == RequestState.REQ_BAD){
                mostrarErrorEnvio()

            }
        })
    }

    private fun mostrarErrorEnvio(){
        dialogo.mostrarError(
            getString(R.string.error_al_enviar_a_la_web),
            getString(R.string.el_detalle_del_mensaje_se_ha_guardado_para_su_envio_posterior)
        )
        dialogo.btnAceptar.setOnClickListener {
            limpiarModeloViewModel()
            findNavController().popBackStack()
            dialogo.Ocultar()
        }
        dialogo.btnCerrar.visibility = View.GONE
        dialogo.btnCerrar.setOnClickListener {
            limpiarModeloViewModel()
            findNavController().popBackStack()
            dialogo.Ocultar()
        }
    }

    private fun limpiarModeloViewModel(){
        viewModel.detalleLirEntity.id= 0
        viewModel.detalleLirEntity.nombreRampa = ""
        viewModel.detalleLirEntity.CreadoPor = ""
        viewModel.detalleLirEntity.firmaRampa = ""
        viewModel.detalleLirEntity.remarks = ""
        viewModel.detalleLirEntity.idMensajeOps = 0
        viewModel.detalleLirEntity.Aerolinea = ""
        viewModel.detalleLirEntity.Destino = ""
        viewModel.detalleLirEntity.Origen = ""
        viewModel.detalleLirEntity.FechaVueloLocal = ""
        viewModel.detalleLirEntity.fechaCreacion = ""
        viewModel.detalleLirEntity.matricula = ""
        viewModel.detalleLirEntity.equipo = ""
        viewModel.detalleLirEntity.NumVuelo = 0
        viewModel.detalleLirEntity.numEmpleadoRampa = ""
//        viewModel.detalleLirEntity.nombreDespacho = ""
//        viewModel.detalleLirEntity.firmaDespacho = ""
//        viewModel.detalleLirEntity.numEmpleadoDespacho = ""
    }

    private fun setupViewModel() {
        mSignatureViewModel.base64.observe(viewLifecycleOwner,{
            procesarFirma(it)
        })

        viewModel.textoMensajeSeleccionado.observe(viewLifecycleOwner, {
            mBinding.tvMensaje.text = if(it != null && it !="" ) it else ""
        })

        userStorage.getUserLogin.asLiveData().observe(viewLifecycleOwner, {
            if (it == null) {
                sessionActive.postValue(false)
            } else {
                sessionActive.postValue(true)
                viewModel.detalleLirEntity.CreadoPor = it.userGuid
            }
        })

        viewModel.getResult().observe(viewLifecycleOwner,{result ->
            when(result){
                is Long ->{
                    idInsertadoRoom = result
                    viewModel.sendDetalle()
                    observersSendDetalleLir()
                }
                is Boolean ->{

                    if (!result){
                        FirebaseCrashlytics.getInstance()
                                .log("El detalle LIR Mensajes Operacionales no se pudo borrar de la base de datos local: ${Gson().toJson(viewModel.detalleLirEntity)} id en la base local: ${idInsertadoRoom.toString()}")
                    }
                    else{
                        mMainViewModel.setCambioFirmas()
                        dialogo.btnAceptar.setOnClickListener {
                            limpiarModeloViewModel()
                            findNavController().popBackStack()
                            dialogo.Ocultar()
                        }
                        dialogo.btnCerrar.visibility = View.GONE
                        dialogo.btnCerrar.setOnClickListener {
                            limpiarModeloViewModel()
                            findNavController().popBackStack()
                            dialogo.Ocultar()
                        }
                        dialogo.mostrarMensajeConfirmacion(
                                getString(R.string.mensaje_enviado),
                                getString(R.string.el_mensaje_se_envio_con_exito))
                    }
                }
            }
        })
        viewModel.getIsLir().observe(viewLifecycleOwner,{
            if(it){
                mBinding.clLIR.visibility = View.VISIBLE

            }
            else{
                mBinding.clLIR.visibility = View.GONE
                mBinding.clLirFirmado.visibility = View.GONE
            }
        })

        viewModel.getFirmas().observe(viewLifecycleOwner,{
            mActivity?.mCantidadNotificaciones?.plus(it)
        })

    }
    private fun procesarFirma(firmaB64: String){
        when(firmaSeleccionada){
            "oficialRampa"->{
                viewModel.setBase64OfRampa(firmaB64)
                this.mostrarFirmaRampa(firmaB64)
            }
            /*"oficialDespacho"->{
                this.viewModel.setBase64Despacho(firmaB64)
                mostrarFirmaDespacho(firmaB64)
            }*/
        }
    }
    private fun mostrarFirmaRampa(firmaB64: String){
        val imageBytes = Base64.decode(firmaB64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes,
                0,
                imageBytes.size)

        viewModel.detalleLirEntity.firmaRampa = firmaB64
        mBinding.mbFirmaRampa.text = getString(R.string.reintentar_firma)
        mBinding.mbFirmaRampa.setIconResource(R.drawable.ic_redo)
        mBinding.mbFirmaRampa.isEnabled = true
        mBinding.ivFirmaRampaMO.visibility = View.VISIBLE
        mBinding.ivFirmaRampaMO.setImageBitmap(decodedImage)



    }
    private fun ocultarFirmaRampa(){
        viewModel.detalleLirEntity.firmaRampa = ""
        mBinding.mbFirmaRampa.text = getString(R.string.firmar)
        mBinding.mbFirmaRampa.setIconResource(R.drawable.ic_draw)
        mBinding.mbFirmaRampa.isEnabled = false
        mBinding.ivFirmaRampaMO.visibility = View.GONE
    }

    /*private fun mostrarFirmaDespacho(firmaB64: String){
        val imageBytes = Base64.decode(firmaB64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes,
                0,
                imageBytes.size)

        viewModel.detalleLirEntity.firmaDespacho = firmaB64
        mBinding.mbFirmaDespacho.text = getString(R.string.reintentar_firma)
        mBinding.mbFirmaDespacho.setIconResource(R.drawable.ic_redo)
        mBinding.mbFirmaDespacho.isEnabled = true
        mBinding.ivFirmaDespachoMO.visibility = View.VISIBLE
        this.mBinding.ivFirmaDespachoMO.setImageBitmap(decodedImage)
    }*/

    /*private fun ocultarFirmaDespacho(){
        viewModel.detalleLirEntity.firmaDespacho = ""
        mBinding.mbFirmaDespacho.text = getString(R.string.firmar)
        mBinding.mbFirmaDespacho.setIconResource(R.drawable.ic_draw)
        mBinding.mbFirmaDespacho.isEnabled = false
        mBinding.ivFirmaDespachoMO.visibility = View.GONE
    }*/

    private fun launchFirmaFragment(firmaSeleccionada: String) {
        this.firmaSeleccionada = firmaSeleccionada
        findNavController().navigate(R.id.action_mensajesOperacionalesFragment_to_signatureFragment)
    }

    private fun setupListMenu() {

        val adapter= ArrayAdapter(requireContext(),R.layout.list_item,listaMensajes ?: listOf())
        (mBinding.tilMensajesOperacionales.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (mBinding.tilMensajesOperacionales.editText as? AutoCompleteTextView)?.
        setOnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
            val value = adapter.getItem(i) ?: ""
            Toast.makeText(requireContext(),value,Toast.LENGTH_SHORT).show()
            mensajes?.forEach { mensaje ->
                if (mensaje.codigo==value){
                    viewModel.setTextoSeleccionado(mensaje.cuerpoMensaje)
                    viewModel.setIsLir(mensaje.isLir)
                    if(!mensaje.fecha.isNullOrEmpty()){
                        mBinding.tvDate.visibility = View.VISIBLE
                        mBinding.tvDate.text = "TimeStamp\n ${Fecha().stringToFechaYearStared(mensaje.fecha)}"
                    }else{
                        mBinding.tvDate.visibility = View.GONE
                    }
                    if (mensaje.isLir) {
                        //Comentar este if para quitar validacion de mensaje firmado
                        if (mensaje.firmado){
                            mBinding.clLIR.visibility = View.GONE
                            mBinding.clLirFirmado.visibility = View.VISIBLE
                        }
                        mensaje.idMensajeOperacional.also { viewModel.detalleLirEntity.idMensajeOps = it }
                    }
                    if(!mensaje.imgb64.isNullOrEmpty() && mensaje.codigo.contains("ACARS")){
                        mBinding.btnPrint.visibility = View.VISIBLE
                        mBinding.btnPrint.setOnClickListener {
                            var bitmap = CreateImageFile.setBitmapFromB64String(mensaje.imgb64)
                            parentFragmentManager.let {
                                DialogPrintUtil(bitmap).show(it, "DialogPrintUtil")
                            }
                        }
                    }else{
                        mBinding.btnPrint.visibility = View.GONE

                    }
                }

            }

        }
    }

    private fun setupActionBar(){
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.mensajes_operacionales)
        setHasOptionsMenu(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                mActivity?.onBackPressed()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //Mostrar datos del vuelo
    private fun setInfoVuelo(){
        (getString(R.string.fecha_de_vuelo) +":").also { mBinding.includeDetalleVuelo.tvtextFechaVuelo.text = it }
        (getString(R.string.numero_de_vuelo) + ":").also { mBinding.includeDetalleVuelo.tvtextNumeroVuelo.text = it }
//        (getString(R.string.matricula) + ":").also { mBinding.includeDetalleVuelo.tvtextMatricula.text = it }
        (getString(R.string.ruta) + ":").also { mBinding.includeDetalleVuelo.tvtextRuta.text = it }
        mBinding.includeDetalleVuelo.tvNumeroVuelo.text = arguments?.getInt("noVuelo").toString()
        viewModel.detalleLirEntity.NumVuelo = arguments?.getInt("noVuelo")!!
        mBinding.includeDetalleVuelo.tvMatricula.text = arguments?.getString(("matricula"))
        viewModel.detalleLirEntity.matricula = arguments?.getString(("matricula")).toString()
        mBinding.includeDetalleVuelo.tvFechaVuelo.text = arguments?.getString("fechaVuelo")
        viewModel.detalleLirEntity.FechaVueloLocal = arguments?.getString("fechaVuelo").toString()
        viewModel.detalleLirEntity.Aerolinea = arguments?.getString("aerolinea").toString()

        val origen: String = arguments?.getString("origen").toString()
        viewModel.detalleLirEntity.Origen = origen
        val destino: String = arguments?.getString("destino").toString()
        viewModel.detalleLirEntity.Destino = destino
        "$origen - $destino".also { mBinding.includeDetalleVuelo.tvRuta.text = it }
        viewModel.detalleLirEntity.equipo = arguments?.getString("equipo").toString()
        viewModel.detalleLirEntity.fechaCreacion = getCurrentDateTime().toString("yyyy-MM-dd HH:mm:ss")
    }

    private fun setupSegmentoLir(){
        //Metodo para configurar switches de empleado externo
        setupSwitchesEmpleadoExterno()
        /*mBinding.etNEDespacho.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding.etNEDespacho.text.isNullOrEmpty()){
                    mBinding.etNombreDespacho.setText("")
                    viewModel.detalleLirEntity.nombreDespacho = ""
                    ocultarFirmaDespacho()
                }
                viewModel.detalleLirEntity.numEmpleadoDespacho =
                    if(!mBinding.swEmpExternoDespacho.isChecked)
                        "AM" + mBinding.etNEDespacho.text.toString().trim()
                    else
                        mBinding.etNEDespacho.text.toString().trim()
            }

            override fun afterTextChanged(p0: Editable?) {
                lirEmpleadoActual = "Despacho"
                mBinding.ibDespacho.isEnabled = true
            }
        })*/
        mBinding.etNERampa.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                viewModel.detalleLirEntity.numEmpleadoRampa =
                    if(!mBinding.swEmpExternoRampa.isChecked)
                        "AM" + mBinding.etNERampa.text.toString().trim()
                    else
                        mBinding.etNERampa.text.toString().trim()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if(mBinding.etNERampa.text.isNullOrEmpty()){
                    mBinding.etNombreRampa.setText("")
                    viewModel.detalleLirEntity.nombreRampa = ""
                    ocultarFirmaRampa()
//                }
            }

            override fun afterTextChanged(p0: Editable?) {
                lirEmpleadoActual = "Rampa"
                mBinding.ibRampa.isEnabled = true

            }
        })
        mBinding.etRemarks.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.detalleLirEntity.remarks = mBinding.etRemarks.text.toString().trim()
                mBinding.mbEnviar.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        /*mBinding.etNombreDespacho.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.detalleLirEntity.nombreDespacho = mBinding.etNombreDespacho.text.toString().trim().toUpperCase()
                mBinding.mbFirmaDespacho.isEnabled = true
            }
        })*/
        mBinding.etNombreRampa.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.detalleLirEntity.nombreRampa = mBinding.etNombreRampa.text.toString().trim().toUpperCase()
                mBinding.mbFirmaRampa.isEnabled = true
            }
        })
    }

    private fun setupSwitchesEmpleadoExterno() {
        /*mBinding.swEmpExternoDespacho.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked){
                mBinding.ibDespacho.visibility = View.GONE
                mBinding.tilNombreDespacho.isEnabled = true
                mBinding.tilNEDespacho.prefixText = ""
                mBinding.etNombreDespacho.setText("")
                viewModel.detalleLirEntity.nombreDespacho = ""
            }
            else{
                mBinding.ibDespacho.visibility = View.VISIBLE
                mBinding.tilNombreDespacho.isEnabled = false
                mBinding.tilNEDespacho.prefixText = "AM"
                mBinding.etNombreDespacho.setText("")
                viewModel.detalleLirEntity.nombreDespacho = ""
                mBinding.mbFirmaDespacho.isEnabled = false

            }
            ocultarFirmaDespacho()
        }*/

        mBinding.swEmpExternoRampa.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                mBinding.ibRampa.visibility = View.GONE
                mBinding.tilNombreRampa.isEnabled = true
                mBinding.tilNERampa.prefixText = ""
                mBinding.etNombreRampa.setText("")
                viewModel.detalleLirEntity.nombreRampa = ""
            }
            else{
                mBinding.ibRampa.visibility = View.VISIBLE
                mBinding.tilNombreRampa.isEnabled = false
                mBinding.tilNERampa.prefixText = "AM"
                mBinding.etNombreRampa.setText("")
                viewModel.detalleLirEntity.nombreRampa = ""
                mBinding.mbFirmaRampa.isEnabled = false
            }
            ocultarFirmaRampa()
        }
    }

    private fun validaNoEmpleado(): Boolean {
//        if (lirEmpleadoActual == "Rampa"){
            if(mBinding.etNERampa.text.isNullOrEmpty()){
                mBinding.etNERampa.error = getString(R.string.empleado_incorrecto)
                return false
            }
            return true
//        }
        /*else{
            if (mBinding.etNEDespacho.text.isNullOrEmpty()){
                mBinding.etNEDespacho.error = getString(R.string.empleado_incorrecto)
                return false
            }
            return true
        }*/
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private fun validaEntity(): Boolean {
//        Toast.makeText(requireContext(),"Json Entity ${Gson().toJson(viewModel.detalleLirEntity).toString()}", Toast.LENGTH_SHORT).show()
        Log.i("Json entity",Gson().toJson(viewModel.detalleLirEntity))
        if (viewModel.detalleLirEntity.firmaRampa.isEmpty()) {
            Toast.makeText(requireContext(),getString(R.string.firma_rampa_requerida), Toast.LENGTH_SHORT).show()
            mBinding.etNERampa.error = getString(R.string.firma_requerida)
            return false
        }
        if (viewModel.detalleLirEntity.nombreRampa.isEmpty()){
            Toast.makeText(requireContext(),getString(R.string.empleado_rampa_requerido), Toast.LENGTH_SHORT).show()
            mBinding.etNERampa.error = getString(R.string.empleado_requerido)
            return false
        }
        if(viewModel.detalleLirEntity.numEmpleadoRampa.isEmpty()){
            Toast.makeText(requireContext(),getString(R.string.numero_de_empleado_requerido), Toast.LENGTH_SHORT).show()
            mBinding.etNERampa.error = getString(R.string.numero_de_empleado_requerido)
            return false
        }
        /*if (viewModel.detalleLirEntity.firmaDespacho.isEmpty()) {
            mBinding.etNEDespacho.error = getString(R.string.firma_requerida)
            Toast.makeText(requireContext(),getString(R.string.firma_despacho_requerida), Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewModel.detalleLirEntity.nombreDespacho.isEmpty()){
            mBinding.etNEDespacho.error = getString(R.string.empleado_requerido)
            Toast.makeText(requireContext(),getString(R.string.empleado_despacho_requerido), Toast.LENGTH_SHORT).show()
            return false
        }
        if(viewModel.detalleLirEntity.numEmpleadoDespacho.isEmpty()){
            Toast.makeText(requireContext(), getString(R.string.numero_de_empleado_requerido), Toast.LENGTH_SHORT).show()
            mBinding.etNEDespacho.error = getString(R.string.numero_de_empleado_requerido)
            return false
        }*/
        if (viewModel.detalleLirEntity.idMensajeOps == 0L){
            Log.i("modelo a enviar", viewModel.detalleLirEntity.idMensajeOps.compareTo(0L).toString())
            Toast.makeText(requireContext(),getString(R.string.selecciona_un_mensaje_valido),Toast.LENGTH_SHORT).show()
            return false
        }
        /*if (viewModel.detalleLirEntity.remarks.isEmpty()){
            mBinding.etRemarks.error = getString(R.string.introducir_remarks)
            return false
        }*/

        return true
    }


}