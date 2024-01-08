package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.viewModel.BuscarVueloViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.adapters.OnClickListener
import com.aeromexico.aeropuertos.paperlessmobile.common.adapters.PaperlessAdapter
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.BuscarVueloFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast

class BuscarVueloFragment : Fragment(), OnClickListener {

    private lateinit var mBinding: BuscarVueloFragmentBinding
    private lateinit var viewModel: BuscarVueloViewModel
    private lateinit var mAdapter: PaperlessAdapter
    private lateinit var gridLayout: GridLayoutManager
    private var mActivity: MainActivity? = null
    private var selectedModule: String?=""
    private lateinit var dialogo: Dialogo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = BuscarVueloViewModel(
            VueloRepository(
                WebServiceApi().getVuelosApi()
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = BuscarVueloFragmentBinding.inflate(inflater,container,false)
        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedModule=arguments?.getString("ModuloSeleccionado")
//        setupViewModel()
        setupRecyclerView()
        setupActionBar()
        mBinding.etFechaVuelo.setOnClickListener{
            showDialog()
        }
        mBinding.btnBuscar.setOnClickListener {
            if (verficarCampos()){
                viewModel.buscarVuelo()
                observersBusqueda()
                hideKeyboard()
            }
        }

        if (arguments?.getBoolean("btnManual") != false)
            mBinding.apply {
                btnManual.visibility = View.VISIBLE
                btnManual.setOnClickListener {
                    val bundle = bundleOf("ModuloSeleccionado" to arguments?.get("ModuloSeleccionado").toString())
                    findNavController().navigate(R.id.action_buscarVueloFragment3_to_informacionManualVueloFragment, bundle)
                }
            }
                setupDialogo()
    }

    private fun observersBusqueda() {
        viewModel.responseState!!.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS){
                dialogo.mostrarCargando("Buscando....")
                mBinding.btnBuscar.isEnabled = false
//                mBinding.progressBar.visibility = View.VISIBLE
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogo.Ocultar()
                mBinding.btnBuscar.isEnabled = true
//                mBinding.progressBar.visibility = View.GONE
                if(it.state == RequestState.REQ_BAD){
                    dialogo.mostrarError("Verifica vuelo",
                        "Comprueba que los datos del vuelo que ingresaste sean correctos.")
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                }
//                Snackbar.make(mBinding.root, "Result ${it.state}", Snackbar.LENGTH_SHORT)
//                    .show()
            }
        })
        viewModel.responseBuscarVuelo.observe(viewLifecycleOwner,{
            if (it.status == RequestState.REQ_OK && it.result != null) {
                if (!it.result.vuelos.isNullOrEmpty())
                    mBinding.tvInfoVuelo.visibility = View.VISIBLE
                    mAdapter.setFlights(it.result.vuelos)
            }
            else{
                mAdapter.cleanFlightList()
                showToast("Vuelo no encontrado")
            }
        })
    }
    private fun setupDialogo() {
        dialogo = Dialogo(requireContext())
    }

    private fun verficarCampos():Boolean {

        if(!mBinding.etDestino.text.isNullOrEmpty() || !mBinding.etOrigen.text.isNullOrEmpty()) {
            if (!mBinding.etDestino.text.isNullOrEmpty()) {
                viewModel.requestBuscarVuelo.Destino = mBinding.etDestino.text.toString().trim()
                if (!mBinding.etOrigen.text.isNullOrEmpty()) {
                    viewModel.requestBuscarVuelo.Origen = mBinding.etOrigen.text.toString().trim()
                    if (!mBinding.etNumVuelo.text.isNullOrEmpty()) {
                        viewModel.requestBuscarVuelo.NumVuelo =
                            mBinding.etNumVuelo.text.toString().trim().toLong()
                        if (!mBinding.etFechaVuelo.text.isNullOrEmpty()) {

                            return true
                        } else {
                            mBinding.etFechaVuelo.error = getString(R.string.error_campo_invalido)
                            mBinding.etFechaVuelo.requestFocus()
                            return false
                        }
                    } else {
                        mBinding.etNumVuelo.error = getString(R.string.error_campo_invalido)
                        mBinding.etNumVuelo.requestFocus()
                        return false
                    }
                } else {
                    mBinding.etOrigen.error = getString(R.string.error_campo_invalido)
                    mBinding.etOrigen.requestFocus()
                    return false
                }
            } else {
                mBinding.etDestino.error = getString(R.string.error_campo_invalido)
                mBinding.etDestino.requestFocus()
                return false
            }
        } else {

            if (!mBinding.etNumVuelo.text.isNullOrEmpty()) {
                viewModel.requestBuscarVuelo.NumVuelo =
                    mBinding.etNumVuelo.text.toString().trim().toLong()
                if (!mBinding.etFechaVuelo.text.isNullOrEmpty()) {

                    return true
                } else {
                    mBinding.etFechaVuelo.error = getString(R.string.error_campo_invalido)
                    mBinding.etFechaVuelo.requestFocus()
                    return false
                }
            } else {
                mBinding.etNumVuelo.error = getString(R.string.error_campo_invalido)
                mBinding.etNumVuelo.requestFocus()
                return false
            }
        }

    }


    fun Int.twoDigits() =
        if (this <= 9) "0$this" else this.toString()

    private fun showDialog(){
        val datepicker = DatePickerFragment{day, month, year -> onDaySelected(day,month,year) }
        datepicker.show(parentFragmentManager,"date")
    }
    fun onDaySelected(day: Int, month: Int, year : Int){
        var m = month+1
        mBinding.etFechaVuelo.setText("${day.twoDigits()}/${m.twoDigits()}/${year}")
        viewModel.requestBuscarVuelo.FechaVuelo="${year}-${m.twoDigits()}-${day.twoDigits()}"

    }
    private fun setupRecyclerView() {
        mAdapter = PaperlessAdapter(mutableListOf(),this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    /**
     * OnClickListener
     */
    override fun onClick(flightEntity: Vuelos) {
        //Toast.makeText(context,selectedModule,Toast.LENGTH_SHORT).show()

        when(selectedModule){
            Constants.Modulos.Mensajes_Operacionales.name -> launchMensajesOperacionales(flightEntity)
            Constants.Modulos.Inspeccion_Aviones.name -> launchInspeccionAeronave(flightEntity)
            Constants.Modulos.Inspeccion_primer_vuelo.name -> launchPrimerVueloDia(flightEntity)
            Constants.Modulos.Deshielo.name -> launchServicioDeshielo(flightEntity)
            Constants.Modulos.Orden_carga_combustible.name -> launchOrdenCargaCombustibles(flightEntity)
            Constants.Modulos.OPT_BDM.name -> launchOPT(flightEntity)
            Constants.Modulos.Search_list.name -> launchSearchList(flightEntity)
			Constants.Modulos.ControlAbordaje.name -> launchControlAbordaje(flightEntity)
            Constants.Modulos.NOTOC.name -> launchNotoc(flightEntity)
            Constants.Modulos.ManifiestoCarga.name -> launchManifiestoCarga(flightEntity)
            Constants.Modulos.Metar.name -> launchMetar(flightEntity)
            Constants.Modulos.NewInspeccion.name -> launchNewInspeccionAeronave(flightEntity)
            Constants.Modulos.Afan.name -> launchAfan(flightEntity)

        }
    }

    private fun launchAfan(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToAfanFragment(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchNewInspeccionAeronave(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToInspecionAeronaveFragment2(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchMetar(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToMetarFragment2(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchManifiestoCarga(flightEntity: Vuelos){
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavManifiestoCarga(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchNotoc(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNotocFragment(flightEntity)
          findNavController().navigate(action)
        
    }

    private fun launchControlAbordaje(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToControlAbordajeFragment(flightEntity)
        findNavController().navigate(action)
    }


    private fun setAtributes(flightEntity: Vuelos) : Bundle{
        val bundle = bundleOf(
                "noVuelo" to flightEntity.numVuelo,
                "fechaVuelo" to Fecha().stringToFecha(flightEntity.fechaVueloLocal),
                "origen" to flightEntity.origen,
                "destino" to flightEntity.destino,
                "matricula" to flightEntity.matricula,
                "flightReferenceNumber" to flightEntity.flightReferenceNumber,//flightEntity.flightReferenceNumber,4355939922, No borrar pls      sáquese
                "guid" to flightEntity.guid,
                "equipo" to flightEntity.equipo,
                "aerolinea" to flightEntity.aerolinea
        )
        return bundle
    }

    private fun launchSearchList(fligthEntity: Vuelos){
       findNavController().navigate(BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavSearchList(fligthEntity))
    }

    private fun launchOPT(flightEntity: Vuelos){
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavOptBdm(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchOrdenCargaCombustibles(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavOrdenCargaCombustible(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchServicioDeshielo(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavDeshielo(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchMensajesOperacionales(flightEntity: Vuelos) {

        /*val int = Intent(context, MensajesOperacionalesActivity::class.java).apply {
            putExtra("flight",flightEntity.flight_reference_number)
        }
        startActivity(int)*/
        /*val fragment = MensajesOperacionalesFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.constainerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/

        findNavController().navigate(R.id.action_buscarVueloFragment3_to_nav_mensajes_operacionales,setAtributes(flightEntity))
    }

    private fun launchInspeccionAeronave(flightEntity: Vuelos) {
         val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavInspecionAeronave(flightEntity)
        findNavController().navigate(action)
    }

    private fun launchPrimerVueloDia(flightEntity: Vuelos) {
        val action = BuscarVueloFragmentDirections.actionBuscarVueloFragment3ToNavInspeccionAeronavePrimerVueloDia(flightEntity)
        findNavController().navigate(action)
    }

    private fun setupActionBar(){
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.buscar_un_vuelo)

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

    private fun hideKeyboard(){
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null){
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)

        super.onDestroy()
    }

}