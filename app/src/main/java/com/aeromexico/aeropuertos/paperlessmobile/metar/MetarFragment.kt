package com.aeromexico.aeropuertos.paperlessmobile.metar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentMetarBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.metar.Model.MetarViewModel
import com.aeromexico.aeropuertos.paperlessmobile.metar.adapter.MetarAdapter
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast


class MetarFragment : Fragment() {
    lateinit var binding: FragmentMetarBinding
    val args: MetarFragmentArgs by navArgs()
    lateinit var vuelo: Vuelos
    lateinit var model: MetarViewModel
    private var mActivity: MainActivity? = null
    lateinit var adapter: MetarAdapter
    lateinit var d: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMetarBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setupActionBar()
        model = MetarViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        adapter = MetarAdapter(arrayListOf())
        d = Dialogo(requireContext())

        args.vuelo.let {
            vuelo = it!!
        }
        binding.apply {
            vuelo.let { vuelo ->
                includeDetalleVuelo.tvFechaVuelo.text =
                    Fecha().stringToFechaOnly(vuelo?.fechaVueloLocal ?: "")
                includeDetalleVuelo.tvNumeroVuelo.text = vuelo?.numVuelo.toString()
                includeDetalleVuelo.tvRuta.text = "${vuelo?.origen} - ${vuelo?.destino}"
                includeDetalleVuelo.tvMatricula.text = vuelo?.matricula
            }
            recycler.adapter = adapter
            btnBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        model.getMetar(vuelo.guid)
        observers()

        return binding.root
    }

    private fun observers() {
        model.responseState.observe(viewLifecycleOwner, Observer {
            if (it.state == RequestState.REQ_IN_PROGRESS) {
                d.mostrarCargando(getString(R.string.cargando))
            } else {
                d.Ocultar()
            }
        })
        model.responseMetar.observe(viewLifecycleOwner, Observer {
            if (!it.result?.metar.isNullOrEmpty()) {
                it.result?.metar.let { lista ->
                    adapter.updateList(lista)
                }
            }else{
                showToast("No hay Información")
            }
        })

    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Metar"
        setHasOptionsMenu(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}