package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspecionAeronaveMainBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel.ConsentimientoFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.InspeccionPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager.viewModel.PagerViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.PrimerVueloDiaFragmentArgs
import com.google.gson.Gson


class InspecionAeronaveMainFragment : Fragment() {

    val args: InspecionAeronaveMainFragmentArgs by navArgs()
    private var mActivity: MainActivity? = null
    lateinit var binding: FragmentInspecionAeronaveMainBinding
    private lateinit var adapterPagerTabs: InspeccionPagerAdapter


    //ViewModels
    private val inspeccionAeronaveViewModel: InspeccionAeronaveViewModel by activityViewModels()
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()
    private val consentimientoFormViewModel: ConsentimientoFormViewModel by activityViewModels()
    private val pagerViewModel: PagerViewModel by activityViewModels()

    //User Storage
    lateinit var userStorage: DataStorageLogin;
    var sessionActive = MutableLiveData<Boolean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBackButtonSystem(requireActivity())

        // Inflate the layout for this fragment
        binding = FragmentInspecionAeronaveMainBinding.inflate(inflater, container, false);

        this.setupStepper();
        this.setInfoVuelo();
        userStorage = DataStorageLogin(requireContext());

        this.pagerViewModel.currentStep.observe(viewLifecycleOwner, {
           this.setGoToTab(it);
        });

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.setupViewModel();
        setupActionBar()




       obtenerElementosMandatorios()

        super.onViewCreated(view, savedInstanceState);
    }


    //Obtener elementos mandatorios
    private fun obtenerElementosMandatorios(){
      //  inspeccionAeronaveViewModel.getTiposAveria()
         observersGetTiposAveria()
    }

    private fun  observersGetTiposAveria(){
        val dialogo = Dialogo(requireContext())
        dialogo.mostrarCargando(getString(R.string.cargando))

        inspeccionAeronaveViewModel.responseStateTiposAveria!!.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS){
                Log.i("INSPEC_AERONAVE", "Obteniendo lista de elementos de inspección...")

            }

            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){

                Log.i("INSPEC_AERONAVE", "Se obtuvo una respuesta: " + it.state)
                if (it.state == RequestState.REQ_BAD){

                    Log.i("INSPEC_AERONAVE", "Ha ocurrido un error al obtener lista de elementos de inspección.")
                dialogo.Ocultar()
                }
            }
        })
        inspeccionAeronaveViewModel.getTiposAveriaBaseResponse.observe(viewLifecycleOwner,{
            val respJSON = Gson().toJson(it)
            Log.i("INSPEC_AERONAVE", "Respuesta obtenida del servidor: " + respJSON)

            if (it.status == RequestState.REQ_OK){


                var mandatorios = it.result.TiposAveria.filter{a -> a.EsObligatorio!!}

                inspeccionFormViewModel.setRadomoMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.radomo.name} != null)
                inspeccionFormViewModel.setCompCargaDelanteroMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_carga_delantero.name} != null)
                inspeccionFormViewModel.setCompCargaTraseroMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_carga_trasero.name} != null)

                if(inspeccionAeronaveViewModel.getEsWideBody().value!!){
                    inspeccionFormViewModel.setCompCargaBulkMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_carga_bulk.name} != null)
                    inspeccionFormViewModel.setCompIntBulkMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_int_bulk.name} != null)
                }else{
                    inspeccionFormViewModel.setCompCargaBulkMandatorio(false)
                    inspeccionFormViewModel.setCompIntBulkMandatorio(false)
                }

                inspeccionFormViewModel.setCompIntDelanteroMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_int_delantero.name} != null)
                inspeccionFormViewModel.setCompIntTraseroMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.comp_int_trasero.name} != null)

                inspeccionFormViewModel.setSemialaIzqMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.semiala_izq.name} != null)
                inspeccionFormViewModel.setSemialaDerMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.semiala_der.name} != null)
                inspeccionFormViewModel.setAguaPotableMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.agua_potable.name} != null)
                inspeccionFormViewModel.setAguaNegraMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.agua_negra.name} != null)
                inspeccionFormViewModel.setPuertaPrincipalMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.puerta_principal.name} != null)
                inspeccionFormViewModel.setServicioTraseraMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.puerta_trasera.name} != null)
                inspeccionFormViewModel.setSepDosInMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.sep_dos_pulg.name} != null)
                inspeccionFormViewModel.setColocacionRedesMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.colocacion_redes.name} != null)
                inspeccionFormViewModel.setOtrosMandatorio(mandatorios.firstOrNull{a -> a.Codigo == Constants.TiposAveria.otro.name} != null)

              dialogo.Ocultar()

            }
        })
    }


    //Establecer stepper y navegación
    private fun setupStepper(){
        adapterPagerTabs = InspeccionPagerAdapter(childFragmentManager)
        binding.tabsContent.adapter = adapterPagerTabs

        //botones superiores de seleccionar el pager
        binding.buttonInspeccion.textInCircle.text = "1"
        binding.buttonConsentimiento.textInCircle.text = "2"
        binding.buttonInspeccion.textNameButton.text = adapterPagerTabs.getPageTitle(0)
        binding.buttonConsentimiento.textNameButton.text = adapterPagerTabs.getPageTitle(1)

        binding.buttonInspeccion.root.setOnClickListener {
            if(!inspeccionAeronaveViewModel.hasFinished.value!!){
                this.pagerViewModel.setStep(0);
            }
        }

        binding.buttonConsentimiento.root.setOnClickListener {
            if(this.pagerViewModel.currentStep.value!! >= 1){
                if(!inspeccionAeronaveViewModel.hasFinished.value!!) {
                    this.pagerViewModel.setStep(1);
                }
            }
        }

    }

    private fun setGoToTab(i: Int) {
        when (i) {
            0 -> {
                binding.buttonInspeccion.textInCircle.visibility = View.VISIBLE
                binding.buttonInspeccion.iconPalomita.visibility = View.GONE
                binding.tabsContent.currentItem = 0

            }
            1 -> {
                binding.buttonInspeccion.textInCircle.visibility = View.GONE
                binding.buttonInspeccion.iconPalomita.visibility = View.VISIBLE
                binding.tabsContent.currentItem = 1

            }

            2 -> {
                binding.buttonConsentimiento.textInCircle.visibility = View.GONE
                binding.buttonConsentimiento.iconPalomita.visibility = View.VISIBLE
                binding.includeDetalleVuelo.detalleVuelo.visibility = View.GONE
                binding.tabsContent.currentItem = 2

            }
        }
    }


    private fun setupViewModel(){
        userStorage.getUserLogin.asLiveData().observe(viewLifecycleOwner, {
            if (it == null) {
                sessionActive.postValue(false)
            } else {
                sessionActive.postValue(true)
                this.inspeccionAeronaveViewModel.request.value!!.CreadoPor = it.userGuid;
            }
        })
    }

    private fun setupActionBar(){
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.inspeccion_de_aeronave_form)

        setHasOptionsMenu(true)
    }


    //Mostrar datos del vuelo
    private fun setInfoVuelo(){
        args.let {
            binding.apply {
                var noVuelo = it.vuelo.numVuelo.toString()
                var matricula = it.vuelo.matricula
                var equipo = it.vuelo.equipo
                var fechaVuelo = it.vuelo.fechaVueloLocal
                var origen = it.vuelo.origen
                var destino = it.vuelo.destino
                var guidVuelo = it.vuelo.guid
                var tipoCabina = it.vuelo.tipoCabina


                binding.includeDetalleVuelo.tvNumeroVuelo.text = noVuelo
                binding.includeDetalleVuelo.tvMatricula.text = matricula
                binding.includeDetalleVuelo.tvFechaVuelo.text = fechaVuelo.substring(0,10)
                binding.includeDetalleVuelo.tvRuta.text =  "$origen - $destino"

                inspeccionAeronaveViewModel.setNoVuelo(noVuelo)
                inspeccionAeronaveViewModel.setMatricula(matricula)
                inspeccionAeronaveViewModel.setEquipo(equipo)
                inspeccionAeronaveViewModel.setFechaVuelo(fechaVuelo)
                inspeccionAeronaveViewModel.setOrigen(origen)
                inspeccionAeronaveViewModel.setDestino(destino)
                inspeccionAeronaveViewModel.setGuidVuelo(guidVuelo)
                if(!tipoCabina.isNullOrEmpty() && tipoCabina == "NB"){
                    inspeccionAeronaveViewModel.setEsWideBody(false)
                }else{
                    inspeccionAeronaveViewModel.setEsWideBody(true)
                }
            }
        }



    }


    private fun reiniciarViewModels(){
        inspeccionAeronaveViewModel.reiniciarVM()
        inspeccionFormViewModel.reiniciarVM()
        consentimientoFormViewModel.reiniciarVM()
        pagerViewModel.reiniciarVM()
    }

    //Prevenir click del botón de atrás
    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
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

}