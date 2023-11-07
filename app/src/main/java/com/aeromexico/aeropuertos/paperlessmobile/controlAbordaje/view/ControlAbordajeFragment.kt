package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.viewmodel.ControlAbordajeViewModel
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.adapters.ControlAbordajePageAdapter
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ControlAbordajeFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.github.ksoichiro.android.observablescrollview.ScrollState

class ControlAbordajeFragment : Fragment() {
    lateinit var binding: ControlAbordajeFragmentBinding
    lateinit var vuelo: Vuelos
    private lateinit var viewModel: ControlAbordajeViewModel
    val args: ControlAbordajeFragmentArgs by navArgs()
    lateinit var pageadapter: ControlAbordajePageAdapter
    var page: Int = 0
    private var mActivity: MainActivity? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ControlAbordajeFragmentBinding.inflate(layoutInflater)
        args.vuelo.let {
            vuelo = it!!
        }
        setupActionBar()


        pageadapter = ControlAbordajePageAdapter(
            childFragmentManager,
            vuelo.flightReferenceNumber.toString(),
            ::btnCotinue
        )

        binding.apply {
            tabsContent.adapter = pageadapter


            vuelo.let { vuelo ->
                includeDetalleVuelo.tvFechaVuelo.text =
                    Fecha().stringToFecha(vuelo?.fechaVueloLocal.toString())
                includeDetalleVuelo.tvNumeroVuelo.text = vuelo?.numVuelo.toString()
                includeDetalleVuelo.tvRuta.text = "${vuelo?.origen} - ${vuelo?.destino}"
                includeDetalleVuelo.tvMatricula.text = vuelo?.matricula
            }

            btnOficialARR.textInCircle.text = "3"
            btnOficialARR.textNameButton.text = "O.O/ARR"

            btnClientes.textInCircle.text = "2"
            btnClientes.textNameButton.text = "Final"

            btnPreliminar.textInCircle.text = "1"
            btnPreliminar.textNameButton.text = "Preliminar"

            btnOficialARR.root.setOnClickListener {
                setGoToTab(2)
            }
            btnClientes.root.setOnClickListener {
                setGoToTab(1)
            }
            btnPreliminar.root.setOnClickListener {
                setGoToTab(0)
            }

        }

        return binding.root
    }

    private fun btnCotinue() {
        setGoToTab(1)
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Control Abordaje"
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
        page = i
        when (page) {

            0 -> {
                binding.apply {

                    tabsContent.currentItem = 0
                    tabsContent.visibility = View.VISIBLE

                    btnPreliminar.textInCircle.visibility = View.VISIBLE
                    btnPreliminar.iconPalomita.visibility = View.GONE

                    btnOficialARR.textInCircle.visibility = View.VISIBLE
                    btnOficialARR.iconPalomita.visibility = View.GONE

                    btnClientes.textInCircle.visibility = View.VISIBLE
                    btnClientes.iconPalomita.visibility = View.GONE

                }
            }
            1 -> {

                binding.apply {

                    tabsContent.currentItem = 1
                    tabsContent.visibility = View.VISIBLE

                    btnPreliminar.textInCircle.visibility = View.GONE
                    btnPreliminar.iconPalomita.visibility = View.VISIBLE

                    btnOficialARR.textInCircle.visibility = View.VISIBLE
                    btnOficialARR.iconPalomita.visibility = View.GONE

                    btnClientes.textInCircle.visibility = View.VISIBLE
                    btnClientes.iconPalomita.visibility = View.GONE

                }
            }
            2 -> {

                binding.apply {
                    tabsContent.currentItem = 2
                    tabsContent.visibility = View.VISIBLE

                    btnPreliminar.textInCircle.visibility = View.GONE
                    btnPreliminar.iconPalomita.visibility = View.VISIBLE

                    btnOficialARR.textInCircle.visibility = View.GONE
                    btnOficialARR.iconPalomita.visibility = View.VISIBLE

                    btnClientes.textInCircle.visibility = View.VISIBLE
                    btnClientes.iconPalomita.visibility = View.GONE


                }
            }

        }
    }

}