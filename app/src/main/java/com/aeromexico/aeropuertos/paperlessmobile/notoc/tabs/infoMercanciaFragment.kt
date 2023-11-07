package com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInfoMercanciaBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.InformacionMercancia
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.gson.Gson

class infoMercanciaFragment(
) : Fragment() {

    private lateinit var mBinding: FragmentInfoMercanciaBinding
    private  val viewModel: NotocViewModel by activityViewModels()
    private  var listaPosicion: List<String>? = listOf()
    private lateinit var notoc: RequestNotoc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentInfoMercanciaBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListMenu()
    }

    private fun obtieneInfoEnDBLocal(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            notoc = it

        }

        viewModel.posicionArregloInfoMercancia.observe(viewLifecycleOwner){

//            llenaVistaConDatos(notoc.informacionMercancia.get(it))
        }
    }


    /*private fun llenaVistaConDatos(notoc: InformacionMercancia){
        mBinding.tvGuiaAerea.text = notoc.mercanciasPeligrosas.numeroGuiaAerea.toString()
        mBinding.tvNombreApropiadoExpedicion.text = notoc.mercanciasPeligrosas.nombreExpedicion
        mBinding.tvClaseODivision.text = notoc.mercanciasPeligrosas.clase.toString()
        mBinding.tvRiesgoSecundario.text = notoc.mercanciasPeligrosas.peligroSecundario
        mBinding.tvCantidadBultos.text = notoc.mercanciasPeligrosas.cantidadBultos.toString()
        mBinding.tvGrupoEmbalaje.text = notoc.mercanciasPeligrosas.grupoEmbalaje.toString()
        mBinding.tvCodigo.text = notoc.mercanciasPeligrosas.codigo
        mBinding.tvCRE.text = notoc.mercanciasPeligrosas.cre.toString()

        //radiactivos
        mBinding.tvCantidadNetaIndiceTransporte.text = notoc.radiactivos.cantidadNeta.toString()
        mBinding.tvCategoria.text = notoc.radiactivos.categoria

        //estibado
        mBinding.tvIdUld.text = notoc.estibado.idUld.toString()

    }*/
    private fun setupListMenu() {
        listaPosicion = listOf("1","Remota","4454")
        val adapter= ArrayAdapter(requireContext(),R.layout.list_item,listaPosicion ?: listOf())
        (mBinding.tilPosicion.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (mBinding.tilPosicion.editText as? AutoCompleteTextView)?.
        setOnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
            val value = adapter.getItem(i) ?: ""
            Toast.makeText(requireContext(),value, Toast.LENGTH_SHORT).show()


        }
    }





}