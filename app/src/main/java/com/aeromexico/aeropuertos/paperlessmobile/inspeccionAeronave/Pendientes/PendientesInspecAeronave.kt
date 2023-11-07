package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Pendientes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentConsentimientoFormBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPendientesInspecAeronaveBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.PendientesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel.ConsentimientoFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Pendientes.ViewModel.PendientesInspecAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.InspeccionAeronaveAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.InspeccionAeronaveClickListener
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.ResponsableEstibaAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson

class PendientesInspecAeronave : Fragment(), InspeccionAeronaveClickListener {


    //Binding
    private lateinit var mBinding: FragmentPendientesInspecAeronaveBinding

    //ViewModels
    private val pendientesViewModel: PendientesInspecAeronaveViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //Main activity
    private var mActivity: MainActivity? = null

    //RecyclerView
    private lateinit var mAdapter: InspeccionAeronaveAdapter
    private lateinit var gridLayout: GridLayoutManager

    //Diálogo (loading)
    private lateinit var dialogoConfirmarDelete: Dialogo
    private lateinit var dialogoCarga: Dialogo

    //Inspeccion seleccionada (enviar o borrar)
    private var inspeccionSeleccionada: InspeccionAeronaveEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendientesViewModel.inspeccionesPendientes.value = arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = FragmentPendientesInspecAeronaveBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupRecyclerView()
        pendientesViewModel.getAllInspecAeronave()
        setupDialogo()




        super.onViewCreated(view, savedInstanceState)
    }

    fun observeViewModel() {

        //Obtener lista de inspecciones pendientes
        pendientesViewModel.getInspeccionesPendientes().observe(viewLifecycleOwner, {
            this.mAdapter.setList(it)

            var count = mAdapter.itemCount;

            if(count > 0){
                mBinding.tvSolicitudesPendientes.text = "$count reporte(s) pendiente(s) por enviar:"

                mBinding.tvSolicitudesPendientes.visibility = View.VISIBLE
                mBinding.recyclerViewPendientes. visibility = View.VISIBLE
                mBinding.includeSinPendientes.containerSinPendientes.visibility = View.GONE
            }else{
                mBinding.tvSolicitudesPendientes.visibility = View.GONE
                mBinding.recyclerViewPendientes. visibility = View.GONE
                mBinding.includeSinPendientes.containerSinPendientes.visibility = View.VISIBLE
            }

        })

        //Enviar una inspección pendiente al backend
        pendientesViewModel.getReadyToSend().observe(viewLifecycleOwner,{
            if(it){
                Log.i("PEND_INSPEC_AERONAVE", "Listo para enviar al backend la inspección pendiente.")
                pendientesViewModel.sendInspeccionAeronavePendiente()
                observersCreateInspecAeronave()
            }
        })


        //Cuando una inspección ha sido eliminada o enviada
        pendientesViewModel.getIdInsertado().observe(viewLifecycleOwner, {
            if(it != null && it != 0L ){

                var msgExito = getString(R.string.msg_exito_inspeccion_aeronave_pendiente) + it

                resources.getStringArray(R.array.array_options_item)
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(msgExito)
                    .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                        pendientesViewModel.deleteInspeccionAeronave(inspeccionSeleccionada!!)
                        pendientesViewModel.resetRequest()
                        pendientesViewModel.resetReadyToSend()
                        pendientesViewModel.resetIdInserted()

                        dialogInterface.dismiss()


                    })
                    .show()
            }
        })

        pendientesViewModel.getInspeccionEliminada().observe(viewLifecycleOwner,{
            if(it){
                mainViewModel.inspecAeronaveCount()
            }
        })
    }


    private fun setupDialogo() {
        dialogoConfirmarDelete = Dialogo(requireContext())

        dialogoConfirmarDelete.btnAceptar.setOnClickListener{
            if(inspeccionSeleccionada != null){
                pendientesViewModel.deleteInspeccionAeronave(inspeccionSeleccionada!!)
                pendientesViewModel.resetRequest()
                pendientesViewModel.resetReadyToSend()
                inspeccionSeleccionada = null
                dialogoConfirmarDelete.Ocultar()


            }
        }

        dialogoConfirmarDelete.btnCerrar.setOnClickListener{
            inspeccionSeleccionada = null
            dialogoConfirmarDelete.Ocultar()
        }

    }

    //RecyclerView (cards pendientes)
    private fun setupRecyclerView() {
        mAdapter = InspeccionAeronaveAdapter(mutableListOf(), this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerViewPendientes.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    override fun onSend(inspeccionAeronaveEntity: InspeccionAeronaveEntity) {
        Toast.makeText(requireContext(),"Se ha selececcionado ENVIAR para reporte pendiente.", Toast.LENGTH_SHORT).show()
        Log.i("PEND_INSPEC_AERONAVE", "Se ha selececcionado ENVIAR para reporte pendiente")
        inspeccionSeleccionada = inspeccionAeronaveEntity
        pendientesViewModel.getAveriasEnInspeccion(inspeccionAeronaveEntity)
    }

    override fun onDelete(inspeccionAeronaveEntity: InspeccionAeronaveEntity) {
        inspeccionSeleccionada = inspeccionAeronaveEntity
        dialogoConfirmarDelete.mostrarPregunta("¿Deseas eliminar este reporte?", "Esta acción es permanente.")
    }


    private fun observersCreateInspecAeronave(){
        var dialogo = Dialogo(requireContext())
        pendientesViewModel.responseState!!.observe(viewLifecycleOwner, {
            if (it.state == RequestState.REQ_IN_PROGRESS) {
                Toast.makeText(requireContext(),"IN PROGRESS", Toast.LENGTH_SHORT).show()
                Log.i("PEND_INSPEC_AERONAVE", "Enviando request en proceso...")
                dialogo.mostrarCargando(getString(R.string.cargando))
            } else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK) {
                Log.i("PEND_INSPEC_AERONAVE", "Se obtuvo una respuesta: " + it.state)
                dialogo.Ocultar()
                if (it.state == RequestState.REQ_BAD) {
                    Toast.makeText(requireContext(),"RESPUESTA BAD", Toast.LENGTH_SHORT).show()
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Ha ocurrido un error al enviar reporte, por favor inenta más tarde.")
                        .setPositiveButton(R.string.aceptar) { dialogInterface, i ->
                            pendientesViewModel.resetRequest()
                            pendientesViewModel.resetReadyToSend()
                            dialogInterface.dismiss()

                        }
                        .show()
                }
            }
        })

        pendientesViewModel.getBaseResponse().observe(viewLifecycleOwner, {
            val respJSON = Gson().toJson(it)
            Log.i("PEND_INSPEC_AERONAVE", "Respuesta obtenida del servidor: " + respJSON)

            if (it.status == RequestState.REQ_OK) {
                var idInspeccion: Long = it.result.InspeccionAeronave
                pendientesViewModel.setIdInsertado(idInspeccion)
                Log.i("PEND_INSPEC_AERONAVE", "Id de inspección creado: " + idInspeccion)
            } else {
                Log.i("PEND_INSPEC_AERONAVE", "Ha ocurrido un error al crear el reporte.")


                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(respJSON)
                    .setPositiveButton(R.string.aceptar) { dialogInterface, i ->
                        pendientesViewModel.resetRequest()
                        pendientesViewModel.resetReadyToSend()
                        dialogInterface.dismiss()
                        // inspeccionSeleccionada = null
                    }
                    .show()
            }
        })
    }


}