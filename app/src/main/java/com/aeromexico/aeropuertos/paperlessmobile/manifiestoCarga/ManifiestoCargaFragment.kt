package com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ManifiestoCargaFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocFragmentArgs
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.toast
import java.io.File

class ManifiestoCargaFragment : Fragment() {

    private lateinit var binding: ManifiestoCargaFragmentBinding
    private val viewModel: ManifiestoCargaViewModel by activityViewModels()
    private val args: ManifiestoCargaFragmentArgs by navArgs()
    private lateinit var vuelo: Vuelos
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ManifiestoCargaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(ContextCompat.checkSelfPermission
                (requireActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
        args.vuelo.let {
            vuelo = it
        }
        setInfoVuelo()
        setupActionBar()

        viewModel.getManifiestoCarga(vuelo.flightReferenceNumber)
        setUpObservers()
    }

    private fun setInfoVuelo(){
        vuelo.let { vuelo->
            binding.includeDetalleVuelo.tvFechaVuelo.text = Fecha().stringToFechaOnly(vuelo?.fechaVueloLocal)
            binding.includeDetalleVuelo.tvNumeroVuelo.text = vuelo?.numVuelo.toString()
            binding.includeDetalleVuelo.tvRuta.text = "${vuelo?.origen} - ${vuelo?.destino}"
            binding.includeDetalleVuelo.tvMatricula.text = vuelo?.matricula
        }
    }

    private fun setUpObservers(){
        val dialogoCarga = Dialogo(requireContext())
        viewModel.responseState.observe(viewLifecycleOwner){

            if (it.state == RequestState.REQ_IN_PROGRESS){

                dialogoCarga.mostrarCargando(getString(R.string.cargando))
                /*Snackbar.make(mBinding.root, "State en progreso ${it.state}", Snackbar.LENGTH_SHORT)
                        .show()*/
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogoCarga.Ocultar()
                if(it.state == RequestState.REQ_BAD) {
                    val dialogo = Dialogo(requireContext())
                    dialogo.mostrarError(getString(R.string.no_hay_informacion_disponible),
                        getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                        //findNavController().popBackStack()
                        //dialogoCarga.Ocultar()
                    }
                }
                Snackbar.make(binding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.responseMC.observe(viewLifecycleOwner){
            if(it != null) {
                if (it.status.isNotEmpty()) {
                    if (it.status == RequestState.REQ_OK) {
                        if (it.result.pdfBase64.isNotEmpty()) {
                            var filePDF: File? = CreateImageFile.getPDFFIleFromB64Encode(
                                it.result.pdfBase64,
                                it.result.nombreArchivo
                            )
                            if (filePDF != null) {
                                binding.pdf.fromFile(filePDF)
                                    .enableAnnotationRendering(true)
                                    .defaultPage(0)
                                    .swipeHorizontal(false)
                                    .spacing(12)
                                    .enableAntialiasing(true)
                                    .load()
                            } else
                                requireContext().toast("Error al Cargar PDF")
                        } else {
                            requireContext().toast("Error al Cargar PDF")
                        }
                    } else {
                        val dialogo = Dialogo(requireContext())
                        dialogo.mostrarError(
                            getString(R.string.no_hay_informacion_disponible),
                            getString(R.string.verifique_su_conexion_e_intente_de_nuevo)
                        )
                        dialogo.btnCerrar.setOnClickListener {
                            dialogo.Ocultar()
                            //findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun setupActionBar(){
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.manifiesto_carga)
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

}