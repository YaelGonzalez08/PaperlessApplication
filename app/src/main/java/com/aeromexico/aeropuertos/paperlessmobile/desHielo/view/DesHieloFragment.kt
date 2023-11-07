package com.aeromexico.aeropuertos.paperlessmobile.desHielo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDesHieloBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.adapters.DeshieloPageAdapter
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.viewmodel.DesHieloViewModel
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.*

class DesHieloFragment : Fragment() {
    val args: DesHieloFragmentArgs by navArgs()
    lateinit var binding: FragmentDesHieloBinding
    lateinit var model: DesHieloViewModel
    private var mActivity: MainActivity? = null
    lateinit var vuelo: Vuelos
    lateinit var pageadapter:DeshieloPageAdapter
    var page: Int = 0
    var datosToserver: DeshieloToServer = DeshieloToServer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDesHieloBinding.inflate(inflater,container,false)
        setupActionBar()
        model = DesHieloViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        args.vuelo.let {
            vuelo = it!!
            datosToserver.vuelo = vuelo
        }
        datosToserver.fechaCreacion =  Fecha().calendarToString(Calendar.getInstance())
        pageadapter = DeshieloPageAdapter(childFragmentManager,::onScrollChanged)

        binding.apply {

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
                      if(pageadapter.concentimientoFragment.comprobacion()){
                          EnviarDeshieloToServer()
                      }
                  }
                  3 -> {
                      findNavController().popBackStack()
                      activity?.onBackPressed()
                  }
              }
            }
            btnRegresar.setOnClickListener {
                when(page){
                    3 -> setGoToTab(2)
                    2-> setGoToTab(1)
                    1 -> setGoToTab(0)
                    0 -> {
                        activity?.onBackPressed()
                    }
                }
            }

            tabsContent.adapter = pageadapter

            tabsContent.setOnTouchListener(OnTouchListener { v, event ->
                tabsContent.parent.requestDisallowInterceptTouchEvent(false)
                false
            })

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

            vuelo.let { vuelo->
                includeDetalleVuelo.tvFechaVuelo.text = Fecha().stringToFecha(vuelo?.fechaVueloLocal.toString())
                includeDetalleVuelo.tvNumeroVuelo.text = vuelo?.numVuelo.toString()
                includeDetalleVuelo.tvRuta.text = "${vuelo?.origen} - ${vuelo?.destino}"
                includeDetalleVuelo.tvMatricula.text = vuelo?.matricula
            }
            btnInformacion.textInCircle.text = "1"
            btnInformacion.textNameButton.text = "Informacion de Servicio"
            btnCondiciones.textInCircle.text = "2"
            btnCondiciones.textNameButton.text = "Condiciones Meteorologicas"
            btnConcentimiento.textInCircle.text = "3"
            btnConcentimiento.textNameButton.text = "Concentimiento"

        }

        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            datosToserver .creadoPor = userdata.userGuid
        }

        return binding.root
    }

    private fun EnviarDeshieloToServer() {
        datosToserver.vuelo = vuelo
        pageadapter.informacionServicioFragment.binding.apply {
            datosToserver.tipoFluido = tipoFluido.spinner.selectedItem.toString()
            datosToserver.mezclaFluido = editMezclaFluido.text.toString().toDouble()
            datosToserver.horaComienzo = textTimeHoraComienzo.text.toString()
            datosToserver.horaTermino = textTimeHoraTermino.text.toString()
            datosToserver.temperaturaAmbiente =
                "${editTemperaturaAmbiente.text.toString()}${spinnerTemperatura.spinner.selectedItem.toString()}"
            datosToserver.temperaturaCongelamiento =
                "${editTemperaturaCongelamiento.text.toString()}${spinnerTemperaturaCongelamiento.spinner.selectedItem.toString()}"
            datosToserver.holdoverTime = textHoldoverTime.text.toString()
            datosToserver.deshieloManual = checkBoxDeshieloManual.isChecked
        }
        pageadapter.condicionesMeteorologicasFragment.binding.apply {
            datosToserver.hielo = checkboxHielo.isChecked
            datosToserver.lluviaSobreEnfriada = checkboxLluviaEnfriada.isChecked
            datosToserver.nevadaFuerte = checkboxNevadaFuerte.isChecked
            datosToserver.nevadaLigera = checkboxNevadaLigera.isChecked
            datosToserver.escarcha = checkboxCongelamiento.isChecked
        }
        datosToserver.capitan = pageadapter.concentimientoFragment.capitan
        datosToserver.aplicador = pageadapter.concentimientoFragment.aplicador
        datosToserver.oficialOperaciones = pageadapter.concentimientoFragment.oficinalOperaciones

        datosToserver.firmaOficialOperaciones =
            pageadapter.concentimientoFragment.firmaoficinalOperaciones

        SaveBDDeshielo(datosToserver)
        binding.apply {
            exitosoMessage.root.visibility = View.VISIBLE
            exitosoMessage.folio.text = getString(R.string.folio) + "trest "
            exitosoMessage.imagenCentral.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.deshielo)
            setGoToTab(3)
        }
        Log.i("datosToserver", Gson().toJson(datosToserver).toString())


    }

    private fun SaveBDDeshielo(toserver: DeshieloToServer) {
        model.saveBdDeshielo(toserver).observe(viewLifecycleOwner, Observer {
           // dialogo.Ocultar()
            Snackbar.make(binding.root, "code: $it", Snackbar.LENGTH_SHORT).show();
            findNavController().popBackStack()
            activity?.onBackPressed()
        })
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Antihielo / Servicio deshielo"
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)

        super.onDestroy()
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
    private fun setGoToTab(i: Int) {
        onScrollChanged(ScrollState.DOWN)
        page = i

        if(page == 1 && !pageadapter.informacionServicioFragment.comprobacion()){
            page = 0
        }

        when (page) {

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

                }
            }
        }
    }

    private fun onScrollChanged(scrollState: ScrollState?){

        if (scrollState == ScrollState.UP) {
            mActivity?.supportActionBar?.apply {
                if (mActivity?.supportActionBar?.isShowing!!) {
                    binding.apply {
                        includeDetalleVuelo.root.visibility = View.GONE
                        tabs.visibility = View.GONE
                    }
                    hide()
                }
            }
        } else if (scrollState == ScrollState.DOWN ) {
            mActivity?.supportActionBar?.apply {
                binding.includeDetalleVuelo.root.visibility = View.VISIBLE
                binding.tabs.visibility = View.VISIBLE
                show()
            }
        }
    }

}