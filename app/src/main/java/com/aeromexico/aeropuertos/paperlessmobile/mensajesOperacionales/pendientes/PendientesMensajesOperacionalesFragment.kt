package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.PendientesMensajesOperacionalesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.MensajeOperacionalesRepository
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.adapters.OnClickListenerMOPendientes
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.adapters.PendientesMOAdapter
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.viewModel.PendientesMensajesOperacionalesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson

class PendientesMensajesOperacionalesFragment : Fragment(), OnClickListenerMOPendientes {

    private lateinit var viewModel: PendientesMensajesOperacionalesViewModel
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mBinding: PendientesMensajesOperacionalesFragmentBinding
    private lateinit var mAdapter: PendientesMOAdapter
    private lateinit var gridLayout: GridLayoutManager
    private lateinit var pendiente: ModificarDetalleLirEntity
    private lateinit var dialogoCarga: Dialogo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PendientesMensajesOperacionalesViewModel(
            MensajeOperacionalesRepository(
                WebServiceApi().getMOApi()
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = PendientesMensajesOperacionalesFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
    }



    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.getFirmas().observe(viewLifecycleOwner,{
            mBinding.textInCircle.text = it.size.toString()
            mAdapter.setPendientes(it)
            muestraLayoutCorrecto(it.size)
        })
    }
    private fun muestraLayoutCorrecto(size: Int){
        if(size == 0 ) {
            mBinding.layoutConPendientes.visibility = View.GONE
            mBinding.includeSinPendientes.textSinpendientes.visibility = View.VISIBLE
            mBinding.includeSinPendientes.textSinpendientes.text = getString(R.string.no_hay_pendientes_de_mensajes_operacionales)
        }
        else {
            mBinding.layoutConPendientes.visibility = View.VISIBLE
            mBinding.includeSinPendientes.textSinpendientes.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        mAdapter = PendientesMOAdapter(mutableListOf(),this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    override fun onClick(detalleLirEntity: ModificarDetalleLirEntity) {
        resources.getStringArray(R.array.array_options_item)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.enviar_pendiente_pregunta))
            .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                pendiente = detalleLirEntity
                viewModel.sendDetalle(detalleLirEntity)
                observersSendDetalleLir()
            })
            .setNegativeButton(R.string.cancel,null)
            .show()
    }

    override fun onDelete(detalleLirEntity: ModificarDetalleLirEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.borrar_pendiente_pregunta))
            .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                pendiente = detalleLirEntity
                viewModel.deleteDetalleFirmasRoom(detalleLirEntity)
            })
            .setNegativeButton(R.string.cancel,null)
            .show()
    }


    private fun observersSendDetalleLir(){
        viewModel.responseStateEnvioFirma!!.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS) {
                dialogoCarga = Dialogo(requireContext())
                dialogoCarga.mostrarCargando(getString(R.string.cargando))
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogoCarga.Ocultar()
                if (it.state == RequestState.REQ_BAD){
                    mMainViewModel.setCambioFirmas()
                    mostrarErrorEnvio()
                }
            }
        })
        viewModel.responseMODetalle.observe(viewLifecycleOwner,{
            mMainViewModel.setCambioFirmas()
            if (it.status == RequestState.REQ_OK){
                viewModel.deleteDetalleFirmasRoom(pendiente)
            }else if (it.status == RequestState.REQ_BAD){
                mostrarErrorEnvio()
            }
        })

        viewModel.getResult().observe(viewLifecycleOwner,{result ->
            when(result){
                is Boolean ->{
                    if (!result){
                        FirebaseCrashlytics.getInstance()
                            .log("El detalle LIR Mensajes Operacionales no se pudo borrar de la base de datos local: ${Gson().toJson(pendiente)} id en la base local: ${pendiente.id}")
                    }
                    else if(result){
                        mMainViewModel.setCambioFirmas()
                        val confirma = Dialogo(requireContext())
                        confirma.mostrarMensajeConfirmacion(getString(R.string.mensaje_enviado),
                            getString(R.string.el_mensaje_se_envio_con_exito))
                        confirma.btnAceptar.setOnClickListener {
                            confirma.Ocultar()
                        }
                        confirma.btnCerrar.visibility = View.GONE
                        confirma.btnCerrar.setOnClickListener {
                            confirma.Ocultar()
                        }
                        /*dialogo.mostrarMensajeConfirmacion(getString(R.string.mensaje_enviado),
                            getString(R.string.el_mensaje_se_envio_con_exito))

                        setBotonesDialogoCerrar()*/
                    }
                }
            }
        })
    }
    private fun mostrarErrorEnvio(){
        val error = Dialogo(requireContext())
        error.mostrarError(getString(R.string.error_al_enviar_a_la_web),
            getString(R.string.el_detalle_del_mensaje_se_ha_guardado_para_su_envio_posterior))
        error.btnAceptar.visibility = View.GONE
        error.btnAceptar.setOnClickListener {
            error.Ocultar()
        }
        error.btnCerrar.setOnClickListener {
            error.Ocultar()
        }
    }

}