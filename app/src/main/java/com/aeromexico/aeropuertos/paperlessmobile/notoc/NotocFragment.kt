package com.aeromexico.aeropuertos.paperlessmobile.notoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentNotocBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.adapters.DeshieloPageAdapter
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.view.DesHieloFragmentArgs
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.PrimerVueloDiaFragmentDirections
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.MensajeOperacionalesRepository
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.viewModel.MensajesOperacionalesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.NotocPageAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class NotocFragment : Fragment() {
    lateinit var binding: FragmentNotocBinding
    val args: NotocFragmentArgs by navArgs()
    lateinit var pageadapter: NotocPageAdapter
    var page: Int = 0
    private var mActivity: MainActivity? = null
    lateinit var vuelo: Vuelos
    private  val viewModel: NotocViewModel by activityViewModels()
    lateinit var notoc: RequestNotoc
    lateinit var userStorage: DataStorageLogin
    private var sessionActive = MutableLiveData<Boolean>()
    //var idToDelete = 0L
    var esEditable=true
    lateinit var local:RequestNotoc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userStorage = DataStorageLogin(requireContext())
        //viewModel = NotocViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        //viewModel = NotocViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotocBinding.inflate(layoutInflater)
        pageadapter = NotocPageAdapter(childFragmentManager)
        setupActionBar()
        args.vuelo.let {
            vuelo = it!!
        }
        limpiaVM()
        local = RequestNotoc(flightReferenceNumber = vuelo.flightReferenceNumber)

        viewModel.getInfoNotoc(vuelo.flightReferenceNumber)

        setObserversInfoDB()
        observeViewModel()



        binding.apply {
            tabsContent.adapter = pageadapter
            btnInformacion.textInCircle.text = "1"
            btnInformacion.textNameButton.text = "Mercancía\n Peligrosa"
            btnCondiciones.textInCircle.text = "2"
            btnCondiciones.textNameButton.text = "Otra carga\n especial"
            btnConcentimiento.textInCircle.text = "3"
            btnConcentimiento.textNameButton.text = "Consentimiento"

            vuelo.let { vuelo->
                includeDetalleVuelo.tvFechaVuelo.text = Fecha().stringToFechaOnly(vuelo?.fechaVueloLocal)
                includeDetalleVuelo.tvNumeroVuelo.text = vuelo?.numVuelo.toString()
                includeDetalleVuelo.tvRuta.text = "${vuelo?.origen} - ${vuelo?.destino}"
                includeDetalleVuelo.tvMatricula.text = vuelo?.matricula
            }

            btnInformacion.root.setOnClickListener{
                setGoToTab(0)
            }
            btnCondiciones.root.setOnClickListener{
                setGoToTab(1)
            }
            btnConcentimiento.root.setOnClickListener{
                setGoToTab(2)
            }

            btnContinuar.setOnClickListener {
                it.hideKeyboard()
                when(page){
                    0 -> {
                        setGoToTab(1)
                    }
                    1-> setGoToTab(2)
                    2 ->{
                        setGoToTab(3)
                       /* if(pageadapter.concentimientoFragment.comprobacion()){
                            EnviarDeshieloToServer()
                        }*/
                    }
                    3 -> {
                        findNavController().popBackStack()
                        activity?.onBackPressed()
                    }
                }
            }
            btnFinalizar.setOnClickListener {
                it.hideKeyboard()
                validateForm()
            }
            btnRegresar.setOnClickListener {
                when(page){
                    //3 -> setGoToTab(2)
                    2-> setGoToTab(1)
                    1 -> setGoToTab(0)
                    0 -> {
                        activity?.onBackPressed()
                    }
                }
            }

            tabsContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
        }
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        limpiaVM()
    }
    private fun observeViewModel(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            notoc = it
        }
    }
    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Notoc"
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)

        super.onDestroy()
    }

    private fun setGoToTab(i: Int) {
        page = i

        when (i) {

            0 -> {
                binding.apply {

                    tabsContent.currentItem = 0
                    tabsContent.visibility = View.VISIBLE

                    btnInformacion.textInCircle.visibility = View.VISIBLE
                    btnInformacion.iconPalomita.visibility = View.GONE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                    btnCondiciones.textInCircle.visibility = View.VISIBLE
                    btnCondiciones.iconPalomita.visibility = View.GONE
                    exitosoMessage.root.visibility = View.GONE

                    btnFinalizar.visibility = View.GONE
                    btnContinuar.visibility = View.VISIBLE
                }
            }
            1 -> {

                binding.apply {

                    tabsContent.currentItem = 1
                    tabsContent.visibility = View.VISIBLE

                    btnInformacion.textInCircle.visibility = View.GONE
                    btnInformacion.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                    btnCondiciones.textInCircle.visibility = View.VISIBLE
                    btnCondiciones.iconPalomita.visibility = View.GONE

                    exitosoMessage.root.visibility = View.GONE

                    btnFinalizar.visibility = View.GONE
                    btnContinuar.visibility = View.VISIBLE
                }
            }
            2 -> {
                binding.apply {

                    tabsContent.currentItem = 2
                    tabsContent.visibility = View.VISIBLE

                    btnInformacion.textInCircle.visibility = View.GONE
                    btnInformacion.iconPalomita.visibility = View.VISIBLE

                    btnCondiciones.textInCircle.visibility = View.GONE
                    btnCondiciones.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                    exitosoMessage.root.visibility = View.GONE

                    btnFinalizar.visibility = View.VISIBLE
                    btnContinuar.visibility = View.GONE

                }
            }
            3 -> {

                binding.apply {
                    tabsContent.visibility = View.GONE

                    btnInformacion.textInCircle.visibility = View.GONE
                    btnInformacion.iconPalomita.visibility = View.VISIBLE

                    btnCondiciones.textInCircle.visibility = View.GONE
                    btnCondiciones.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.GONE
                    btnConcentimiento.iconPalomita.visibility = View.VISIBLE

                    btnFinalizar.visibility = View.VISIBLE
                    btnContinuar.visibility = View.GONE

                }
            }
        }
    }

    private fun setInfoVuelo(){
        val formsInDBLocal: ArrayList<RequestNotoc> = arrayListOf()
        var idToDelete = 0L
        viewModel.getInfoLocalDB().observe(viewLifecycleOwner){local ->
            local.forEach{
                Log.i("info local",local.toString())
                formsInDBLocal.add(Gson().fromJson(it.request, RequestNotoc::class.java))
                if(Gson().fromJson(it.request, RequestNotoc::class.java).flightReferenceNumber == vuelo.flightReferenceNumber) {
                    idToDelete = it.id
                    Log.i("idToDelete obtenido", idToDelete.toString())

                }
            }
            formsInDBLocal.forEach {
                if (it.flightReferenceNumber == vuelo.flightReferenceNumber){
                    this.local = it
                    return@forEach
                }
            }
//            Log.i("Editable",esEditable.toString())
            /*if(esEditable){
                Log.i("idToDelete editable", idToDelete.toString())
                if (idToDelete != 0L){
                    viewModel.notoc.informacionMercancia.forEachIndexed { index, infoMercancia ->
                        infoMercancia.posicion = this.local.informacionMercancia[index].posicion
                    }
                    viewModel.notoc.otraCarga.forEachIndexed { index, otraCarga ->
                        otraCarga.posicion= this.local.otraCarga[index].posicion
                    }
                    *//*viewModel.notoc.informacionMercancia.estibado.posicion = local.informacionMercancia.estibado.posicion
                    viewModel.notoc.otraCarga.estibado.posicion = local.otraCarga.estibado.posicion*//*
                    Log.i("Local consentimiento",this.local.consentimiento.toString())
                    viewModel.notoc.consentimiento=this.local.consentimiento
                    esEditable=false
                }

            }
            else{
                if (idToDelete != 0L){
                    viewModel.deleteNotocById(idToDelete)
                    idToDelete=0

                }
            }*/
            viewModel.notoc.fechaCreacion
            viewModel.setNotocEntity(viewModel.notoc)
        }



        /*if (notoc.informacionMercancia.estibado.posicion != 0 && notoc.otraCarga.estibado.posicion!=0){
            if (local.informacionMercancia.estibado.posicion!=0 || local.otraCarga.estibado.posicion != 0){
                viewModel.deleteNotocById(idToDelete)
                idToDelete=0
            }
        }
        else if (local.informacionMercancia.estibado.posicion!=0 || local.otraCarga.estibado.posicion != 0){
            viewModel.notoc.informacionMercancia.estibado.posicion = local.informacionMercancia.estibado.posicion
            viewModel.notoc.otraCarga.estibado.posicion = local.otraCarga.estibado.posicion
        }*/





    }
    private fun limpiaVM(){

        viewModel.notoc = RequestNotoc(flightReferenceNumber = 0)
        viewModel.setNotocEntity(viewModel.notoc)
    }

    private fun setObserversInfoDB(){
        var dialogo = Dialogo(requireContext())
        viewModel.responseStateGetInfo.observe(viewLifecycleOwner){
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
                Snackbar.make(binding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.responseInfoNotoc.observe(viewLifecycleOwner){
            if (it.message.trim() != "No se encontro la informacion del vuelo." && !it.error){

                    if (it.result.notoc != null) {
                        viewModel.notoc = it.result.notoc
                        binding.btnFinalizar.isEnabled = it.result.notoc.editable
//                        viewModel.setEditable(esEditable(it.result.notoc))
                    }


                /*Snackbar.make(binding.root, "Info ${viewModel.notoc.informacionMercancia}", Snackbar.LENGTH_LONG)
                    .show()*/

            }else{
                dialogo.mostrarAviso(getString(R.string.no_hay_mensajes_disponibles),
                    it.message)
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                    findNavController().popBackStack()
                }

            }
//            esEditable=it.result.esEditable
            viewModel
            setInfoVuelo()

        }

        userStorage.getUserLogin.asLiveData().observe(viewLifecycleOwner, {
            if (it == null) {
                sessionActive.postValue(false)
            } else {
                sessionActive.postValue(true)
                viewModel.notoc.creadoPor = it.userGuid
            }
        })


    }
    private fun esEditable(notoc: RequestNotoc) : Boolean{
        var editable = true
        notoc.informacionMercancia.forEach{
            if (it.posicion != 0){
                editable=false
            }
        }
        notoc.otraCarga.forEach {
            if (it.posicion != 0){
                editable=false
            }
        }

        if (!notoc.consentimiento.oficial.numEmpleado.isNullOrEmpty()){

            editable=false
        }else if (!notoc.consentimiento.oficial.nombre.isNullOrEmpty()){
            editable=false
        } else if (!notoc.consentimiento.oficial.firmaB64.isNullOrEmpty()){
            editable=false
        }

        if (!notoc.consentimiento.capitan.numEmpleado.isNullOrEmpty()){
            editable=false
        }else if (!notoc.consentimiento.capitan.nombre.isNullOrEmpty()){
            editable=false
        } else if (!notoc.consentimiento.capitan.firmaB64.isNullOrEmpty()){
            editable=false
        }
//        Toast.makeText(context,"Editable: ${editable}",Toast.LENGTH_SHORT).show()
        return editable
    }
    private fun setObserversSend(){
        var dialogo = Dialogo(requireContext())
        viewModel.responseStateSendNotoc.observe(viewLifecycleOwner){
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
//                Snackbar.make(binding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
//                    .show()
            }
        }

        viewModel.responseSendNotoc.observe(viewLifecycleOwner){
            //Toast.makeText(context,"Enviado respuesta ${it.toString()}",Toast.LENGTH_SHORT).show()
            if (!it.error){
                var dia = Dialogo(requireContext())
                dia.mostrarMensajeConfirmacion("Información enviada", "La información se ha registrado correctamente")
                dia.btnCerrar.setOnClickListener {
                    dia.Ocultar()
                    findNavController().popBackStack()
                }
                dia.btnAceptar.setOnClickListener {
                    dia.Ocultar()
                    findNavController().popBackStack()
                }
            }
            else{
                var dia = Dialogo(requireContext())
                dia.mostrarError("Error al enviar la información", "${it.errorMessage}")
                dia.btnCerrar.setOnClickListener {
                    dia.Ocultar()
                    findNavController().popBackStack()
                }
                dia.btnAceptar.setOnClickListener {
                    dia.Ocultar()
                    findNavController().popBackStack()
                }
                viewModel.addNotocToDBLocal(viewModel.notoc)
            }
        }
    }
    private fun enviaAWs(){
        //Toast.makeText(context,"Enviar",Toast.LENGTH_SHORT).show()
        Log.i("Enviar notoc", viewModel.notoc.toString())
        Log.i("Enviar notoc infoMerc", viewModel.notoc.informacionMercancia.toString())
        Log.i("Enviar notoc OtraMerc", viewModel.notoc.otraCarga.toString())
        Log.i("Enviar notoc Consent", "${viewModel.notoc.consentimiento.oficial.numEmpleado.toString()} ${viewModel.notoc.consentimiento.oficial.nombre}")
        //findNavController().popBackStack()
        viewModel.notoc.fechaCreacion = getCurrentDateTime().toString("yyyy-MM-dd HH:mm:ss")
        viewModel.sendInfoNotoc(viewModel.notoc)
        setObserversSend()
    }
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }



    private fun validateForm() {
        var tieneCero =false
        var idToDelete =0L
        val formsInDBLocal: ArrayList<RequestNotoc> = arrayListOf()
        viewModel.getInfoLocalDB().observeOnce{local ->
            local.forEach{
                Log.i("info local",local.toString())
                formsInDBLocal.add(Gson().fromJson(it.request, RequestNotoc::class.java))
                if(Gson().fromJson(it.request, RequestNotoc::class.java).flightReferenceNumber == vuelo.flightReferenceNumber) {
                    idToDelete = it.id
                    Log.i("idToDelete valid", idToDelete.toString())

                }
            }

            viewModel.notoc.informacionMercancia.forEach{
                if (it.posicion == 0){
                    tieneCero=true
                }
            }
            viewModel.notoc.otraCarga.forEach {
                if (it.posicion == 0){
                    tieneCero=true
                }
            }

            if( validaEmpleados() && !tieneCero
            /*|| viewModel.notoc.informacionMercancia.estibado.posicion != 0
            || viewModel.notoc.otraCarga.estibado.posicion != 0*/
            ){
//                if(idToDelete==0L){
//                    viewModel.addNotocToDBLocal(viewModel.notoc).observe(viewLifecycleOwner){
//                        idToDelete=it
//                        Log.i("id Recien guardado", idToDelete.toString())
//                        enviaAWs()
//                    }
//                }
//                else{

                    enviaAWs()
//                }

            }
            else{
                val diag2 = Dialogo(requireContext())
                diag2.apply {
                    diag2.btnCerrar.setOnClickListener{diag2.Ocultar()}
                    mostrarError(
                        "Formulario incompleto",
                        "Verifica que cada formulario este completo antes de enviar."
                    )

                }


            }
        }
    }

    private fun validaEmpleados():Boolean{
        var dialogo= Dialogo(requireContext())
        if (viewModel.notoc.consentimiento.oficial.numEmpleado == "" || viewModel.notoc.consentimiento.oficial.numEmpleado == " "){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Numero de empleado del oficial vacio"
                )
            }
            return false
        }else if (viewModel.notoc.consentimiento.oficial.nombre.isNullOrEmpty()){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Nombre del oficial vacio"
                )
            }
            return false
        } else if (viewModel.notoc.consentimiento.oficial.firmaB64.isNullOrEmpty()){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Firma del oficial vacio"
                )
            }
            return false
        }

        if (viewModel.notoc.consentimiento.capitan.numEmpleado.isNullOrEmpty()){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Numero de empleado del capitan vacio"
                )
            }
            return false
        }else if (viewModel.notoc.consentimiento.capitan.nombre.isNullOrEmpty()){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Nombre del capitan vacio"
                )
            }
            return false
        } else if (viewModel.notoc.consentimiento.capitan.firmaB64.isNullOrEmpty()){
            dialogo.apply {
                dialogo.btnCerrar.setOnClickListener{dialogo.Ocultar()}
                mostrarError(
                    "Formulario incompleto",
                    "Firma del capitan vacio"
                )
            }
            return false
        }

        return true
    }

}