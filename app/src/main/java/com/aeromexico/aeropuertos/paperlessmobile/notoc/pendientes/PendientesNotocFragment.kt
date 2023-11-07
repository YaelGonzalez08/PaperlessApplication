package com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPendientesNotocBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.PendientesMensajesOperacionalesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.adapters.PendientesMOAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes.adapter.OnClickListener
import com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes.adapter.PendientesNotocAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.util.ArrayList

class PendientesNotocFragment : Fragment(), OnClickListener {
    private  val viewModel: NotocViewModel by activityViewModels()
    private lateinit var mBinding: FragmentPendientesNotocBinding
    private lateinit var pendiente: NotocEntity
    private lateinit var mAdapter: PendientesNotocAdapter
    private lateinit var gridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPendientesNotocBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewmodel()
    }

    private fun setupViewmodel(){

        viewModel.getInfoLocalDB().observe(viewLifecycleOwner){

            mAdapter.setPendientes(it)
            muestraLayoutCorrecto(it.size)
        }
    }
    private fun muestraLayoutCorrecto(size: Int){
        if(size == 0 ) {
            mBinding.recyclerPendientes.visibility = View.GONE

            mBinding.msjSinPendientes.textSinpendientes.visibility = View.VISIBLE
            mBinding.msjSinPendientes.textSinpendientes.text = getString(R.string.no_hay_pendientes_notoc)
        }
        else {
            mBinding.recyclerPendientes.visibility = View.VISIBLE
            mBinding.msjSinPendientes.textSinpendientes.visibility = View.GONE
        }
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
                viewModel.deleteNotocById(pendiente)
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

                }
                dia.btnAceptar.setOnClickListener {
                    dia.Ocultar()

                }
            }
        }
    }
    private fun setupRecyclerView() {
        mAdapter = PendientesNotocAdapter(mutableListOf(),this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerPendientes.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    override fun onClick(notoc: NotocEntity) {
        var rq = Gson().fromJson(notoc.request, RequestNotoc::class.java)
        resources.getStringArray(R.array.array_options_item)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.enviar_pendiente_pregunta))
            .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                pendiente = notoc
                viewModel.sendInfoNotoc(rq)
                setObserversSend()
            })
            .setNegativeButton(R.string.cancel,null)
            .show()
    }

    override fun onDelete(notoc: NotocEntity) {
        var rq = Gson().fromJson(notoc.request, RequestNotoc::class.java)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.borrar_pendiente_pregunta))
            .setPositiveButton(R.string.aceptar,{dialogInterface, i ->
                pendiente = notoc
                viewModel.deleteNotocById(notoc)
            })
            .setNegativeButton(R.string.cancel,null)
            .show()
    }
}