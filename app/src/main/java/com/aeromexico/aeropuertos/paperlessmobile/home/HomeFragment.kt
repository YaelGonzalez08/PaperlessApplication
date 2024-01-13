package com.aeromexico.aeropuertos.paperlessmobile.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.viewModel.BuscarVueloViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.AppPreferences
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentHomeBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.adapter.AdapterMenuPrincipal
import com.aeromexico.aeropuertos.paperlessmobile.home.adapter.AdapterMenuTopPrincipal
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mBuscarVueloViewModel: BuscarVueloViewModel
    private var mActivity: MainActivity? = null
    var listModules = ArrayList<MenuModule>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        listModules.add(MenuModule(
            name = getString(R.string.mensajes_operacionales_menu),
            image = R.drawable.ic_airplane_ticket,
            destination = ::launchMensajesOperacionalesFragment))

        listModules.add(MenuModule(
            name = getString(R.string.inspeccion_de_aeronave),
            image = R.drawable.ic_inspeccion_aeronave,
            destination = ::launchInpeccionAeronave))
        listModules.add(MenuModule(
            name ="Manifiesto \nde Carga",
            image =R.drawable.manifiesto_carga,
            destination = ::launchManifiestoCarga))
        listModules.add(MenuModule(
            name ="Metar",
            image =R.drawable.deshielo,
            destination = ::  launchMetar))
        listModules.add(MenuModule(
            name ="AFAN",
            image =R.drawable.plane_check,
            destination = ::  afanmodule))

        listModules.add(MenuModule(
            name =getString(R.string.orden_de_carga_de_combustible),
            image =R.drawable.ic_bomba_de_gasolina,
            destination = ::launchOrdenCarga))
        listModules.add(MenuModule(
            name =getString(R.string.check_plane_firts_flying_day),
            image =R.drawable.ic_primer_vuelo,
            destination = ::launchFirstFlightDay))
        listModules.add(MenuModule(
            name ="Control de \nabordaje y \nproc. de seg.",
            image =R.drawable.abordaje,
            destination = ::launchControlAbordaje))

        listModules.add(MenuModule(
            name =getString(R.string.inspeccion_de_equipo),
            image =R.drawable.coche,
            destination = ::launchInspeccionEquipoFragment))

        listModules.add(MenuModule(
            name ="NOTOC",
            image =R.drawable.notoc,
            destination = ::launchNotoc))

        listModules.add(MenuModule(
            name =getString(R.string.search_list),
            image =R.drawable.ic_search_list,
            destination = ::lauchSearchList))

        listModules.add(MenuModule(
            name ="GAM \ne-Report",
            image =R.drawable.gam,
            destination = ::launchGAMReport))

        listModules.add(MenuModule(
            name ="ACK \nComunicados",
            image =R.drawable.ic_text_snippet,
            destination = ::launchACK))
        /*
        listModules.add(MenuModule(
            name ="Encuesta",
            image =R.drawable.ic_baseline_assignment_24,
            destination = ::launchEncuesta))
            */

    }


    override fun onResume() {
        setupActionBar()
        super.onResume()
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)

        if(BuildConfig.PAPERLESS_AMBIENT.contains("dev")){
            mActivity?.supportActionBar?.title = getString(R.string.app_name) + "-Dev-"
        }

        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater,container,false)

        mBinding.reciclerMenu.adapter = AdapterMenuPrincipal(listModules)
        var actualesList = AppPreferences(requireContext()).getList() as ArrayList<MenuModuleRecientes>
        if(!actualesList.isNullOrEmpty()){
            mBinding.tvTituloTop.visibility = View.VISIBLE
            mBinding.reciclerMenuTop.visibility = View.VISIBLE
            mBinding.reciclerMenuTop.adapter = AdapterMenuTopPrincipal(actualesList,::goRecent)
        }
        return mBinding.root
    }

    private fun goRecent(nameModule: String) {
        var moduleSelected = listModules.first { nameModule.contains(it.name) }
        moduleSelected.destination.invoke(moduleSelected)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {

        /*mBinding.btnOptBdm.setOnClickListener{
            val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.OPT_BDM.name)
            findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
        }*/

    
    }

    private fun launchACK(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)
        findNavController().navigate(R.id.action_homeFragment2_to_comunicadosFragment)
    }

    private fun launchGAMReport(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        findNavController().navigate(R.id.action_homeFragment2_to_GAMeReportFragment)

    }

    private fun launchControlAbordaje(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.ControlAbordaje.name)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchInpeccionAeronave(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.NewInspeccion.name)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)

    }

    private fun launchMetar(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Metar.name)
        bundle.putBoolean("btnManual", false)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchManifiestoCarga(module:MenuModule){
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.ManifiestoCarga.name)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchNotoc(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.NOTOC.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)

    }

    private fun lauchSearchList(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Search_list.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchEncuesta(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        findNavController().navigate(R.id.action_homeFragment2_to_metarFragment)
    }

    private fun launchDesHielo(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Deshielo.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchInspeccionEquipoFragment(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        findNavController().navigate(R.id.action_homeFragment2_to_nav_checklist_diario)
    }

    private fun launchMensajesOperacionalesFragment(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

//        Toast.makeText(this,Constants.Modulos.Mensajes_Operacionales.name,Toast.LENGTH_SHORT).show()
//        mBuscarVueloViewModel.setSelectedModule(Constants.Modulos.Mensajes_Operacionales.name)
        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Mensajes_Operacionales.name)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3,bundle)

        /*val int = Intent(this, MensajesOperacionalesActivity::class.java)
        startActivity(int)*/

        /*val fragment = BuscarVueloFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.constainerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/
    }

    private fun afanmodule(module: MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Afan.name)
        bundle.putBoolean("btnManual", false)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3,bundle)
    }

    private fun launchInspeccionAeronaveFragment(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Inspeccion_Aviones.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3,bundle)
    }

    private fun launchFirstFlightDay (module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Inspeccion_primer_vuelo.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }

    private fun launchOrdenCarga(module:MenuModule) {
        AppPreferences(requireContext()).saveModule(module)

        val bundle = bundleOf("ModuloSeleccionado" to Constants.Modulos.Orden_carga_combustible.name)
        bundle.putBoolean("btnManual", true)
        findNavController().navigate(R.id.action_homeFragment2_to_buscarVueloFragment3, bundle)
    }
}