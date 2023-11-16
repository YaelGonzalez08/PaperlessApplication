package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspecionAeronaveMainBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.adapters.AdapterPageInspeccionAeronaves
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import ng.softcom.android.utils.ui.showToast
import java.util.*
import kotlin.collections.ArrayList


class InspecionAeronaveFragment : Fragment() {
    private lateinit var userLogginData: Usuario
    val args: InspecionAeronaveFragmentArgs by navArgs()
    private var mActivity: MainActivity? = null
    lateinit var binding: FragmentInspecionAeronaveMainBinding
    lateinit var model: InspeccionAeronaveViewModel
    lateinit var pageadapter: AdapterPageInspeccionAeronaves
    var listaAverias: ArrayList<Averia> = arrayListOf()
    var formLocalExist: InspeccionAeronave? = null

    override fun onResume() {
        super.onResume()
        setupActionBar()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInspecionAeronaveMainBinding.inflate(inflater, container, false);
        model = InspeccionAeronaveViewModel()
        setInfoVuelo()



        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            userLogginData = userdata
        }

        return binding.root
    }

    //Mostrar datos del vuelo
    private fun setInfoVuelo() {
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
                isLlegada(it.vuelo.guid)

                binding.includeDetalleVuelo.tvNumeroVuelo.text = noVuelo
                binding.includeDetalleVuelo.tvMatricula.text = matricula
                binding.includeDetalleVuelo.tvFechaVuelo.text = fechaVuelo.substring(0, 10)
                binding.includeDetalleVuelo.tvRuta.text = "$origen - $destino"

            }
        }


    }

    private fun isLlegada(guid: String) {
        var d = Dialogo(requireContext())
        d.mostrarPregunta("Tipo de Vuelo", "¿Qué tipo de Vuelo es?")
        d.btnAceptar.text = "Llegada"
        d.btnAceptar.setOnClickListener {
            d.Ocultar()
          //  consultarFormularioByGuid(guid, true)
            getAverias(guid,true)
        }
        d.btnCerrar.text = "Salida"
        d.btnCerrar.setOnClickListener {
            d.Ocultar()
           // consultarFormularioByGuid(guid, false)
            getAverias(guid,false)
        }

    }
    private fun getAverias(guid:String,esLlegada:Boolean){
        model.getTiposAveria(esSalida = !esLlegada,esLlegada = esLlegada)?.observe(viewLifecycleOwner, Observer {
            listaAverias = it.result.TiposAveria
            consultarFormularioByGuid(guid, esLlegada)
        })
    }

    private fun consultarFormularioByGuid(guid: String, isLlegada: Boolean) {
        //consulta local
        model.consultarExisLocal(guid, isLlegada).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //  showToast("Se encontro localmente")
                formLocalExist = it
            }
        })
        var d = Dialogo(requireContext())
        d.mostrarCargando(null)
        model.GetInspeccion(guid, isLlegada).observe(viewLifecycleOwner, Observer {
            d.Ocultar()
            if (it != null) {
                if (it.result?.inspeccionAeronave?.Completado == true && !it.result.inspeccionAeronave.nombre_OficialOperaciones.isNullOrEmpty()) {

                    listaAverias.forEach { averiaDef ->
                        averiaDef.codigoAveria = averiaDef.Codigo
                        averiaDef.tieneAveria = null
                    }

                    var ins = InspeccionAeronave(
                        fechaCreacion = Fecha().parser.format(Date()).toString(),
                        creadoPor = userLogginData.userGuid,
                        guidVuelo = guid,
                        esLlegada = isLlegada
                    )
                    ins.averias = listaAverias

                    cargarForm(ins)
                } else {
                    //      showToast("info$it")
                    var listaCombinada: ArrayList<Averia> = arrayListOf()
                    listaAverias.forEach { averiaDef ->
                        var update =
                            it.result?.inspeccionAeronave?.averias?.firstOrNull {
                                it.codigoAveria == averiaDef.Codigo
                            }
                        averiaDef.codigoAveria = averiaDef.Codigo
                        averiaDef.tieneAveria = null
                        if (update != null) {
                            averiaDef.idAveria = update.idAveria
                            averiaDef.codigoAveria = update.codigoAveria
                            averiaDef.tieneAveria = update.tieneAveria
                            averiaDef.descripcionAveria = update.descripcionAveria
                            averiaDef.imagenes = update.imagenes
                            averiaDef.respuestaAux = update.respuestaAux
                        }

                        listaCombinada.add(averiaDef)
                    }

                    if (it.result?.inspeccionAeronave != null) {
                        //carga form con datos precargados
                        var ins = it.result?.inspeccionAeronave
                        ins.guidVuelo = guid
                        ins.averias = listaCombinada
                        //aqui falta que cuando carge lo elimine cde local junto con las imagenes
                        model.deleteByguid(guid, isLlegada)
                        cargarForm(ins)
                    } else if (formLocalExist != null) {
                        cargarForm(formLocalExist!!)
                    } else {
                        //form nuevo
                        var ins = InspeccionAeronave(
                            fechaCreacion = Fecha().parser.format(Date()).toString(),
                            creadoPor = userLogginData.userGuid,
                            guidVuelo = guid,
                            esLlegada = isLlegada
                        )
                        ins.averias = listaCombinada
                        cargarForm(ins)
                    }
                }


            }else{
                d.mostrarError("Error!","Error interno del servidor.")
                    d.btnCerrar.setOnClickListener {
                        d.Ocultar()
                        mActivity?.onBackPressed()
                    }
            }
        })


    }

    private var inspeccionLocal: InspeccionAeronave? = null

    private fun cargarForm(inspeccionAeronave: InspeccionAeronave) {
        var otrosEnd = inspeccionAeronave.averias?.firstOrNull { it.Codigo?.contains("otro") == true  }
        inspeccionAeronave.averias?.forEach { averia ->
            averia.imagenes?.forEach { imgObject ->
                    if (imgObject.imgB64.isNullOrEmpty()){
                        averia.imagenes?.remove(imgObject)
                    }
            }

        }

        if (otrosEnd != null) {
            inspeccionAeronave.averias?.remove(otrosEnd)
            inspeccionAeronave.averias?.add(otrosEnd)
        }
        inspeccionLocal = inspeccionAeronave
        pageadapter = AdapterPageInspeccionAeronaves(
            childFragmentManager,
            args.vuelo ?: null,
            inspeccionAeronave,
            ::toConcentiemto,
            ::getInspeccionLocal
        )
        binding.apply {
            tabsContent.adapter = pageadapter
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
                    if (!issend)
                        binding.tabsContent.currentItem = 0
                }
            })

        }
    }

    fun getInspeccionLocal() = inspeccionLocal

    var issend = false
    private fun toConcentiemto(inspeccion: InspeccionAeronave) {
        inspeccionLocal = inspeccion
        issend = true
        binding.tabsContent.currentItem = 1
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.inspeccion_de_aeronave_form)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}