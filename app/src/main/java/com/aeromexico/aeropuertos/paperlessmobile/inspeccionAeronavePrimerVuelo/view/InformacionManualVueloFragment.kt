package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInformacionManualVueloBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.InformacionManualVueloViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.IATA
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import ng.softcom.android.utils.ui.showToast

class InformacionManualVueloFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentInformacionManualVueloBinding
    lateinit var companies: List<String>
    lateinit var origins: List<IATA>
    lateinit var destin: MutableList<String>
    var enrollment: ArrayList<String> = arrayListOf()
    lateinit var model: InformacionManualVueloViewModel
    private var mActivity: MainActivity? = null
    lateinit var dialogo: Dialogo

    var codigoODS: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupActionBar()
        binding = FragmentInformacionManualVueloBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity
        dialogo = Dialogo(requireContext())
        model = InformacionManualVueloViewModel()
        model.obtenerListaIatas()
        observers()

        companies = arrayListOf()
        destin = arrayListOf()

        binding.apply {
            tvDateFlightValue.setOnClickListener(this@InformacionManualVueloFragment)
            btnContinue.setOnClickListener(this@InformacionManualVueloFragment)
            btnBack.setOnClickListener(this@InformacionManualVueloFragment)
            tietETADate.setOnClickListener(this@InformacionManualVueloFragment)
            tietETAHour.setOnClickListener(this@InformacionManualVueloFragment)
            tietETDHour.setOnClickListener(this@InformacionManualVueloFragment)
            tietETDDate.setOnClickListener(this@InformacionManualVueloFragment)
            actvEquip.setOnClickListener(this@InformacionManualVueloFragment)
        }
        return binding.root
    }

    fun observers() {
        dialogo.mostrarCargando("Espera un momento")
        model.responseIatas.observe(viewLifecycleOwner, { iatasResponse ->
            if (iatasResponse.status == RequestState.REQ_OK) {
                dialogo.Ocultar()
                origins = model.responseIatas.value?.result?.iATAs!!
                companies = model.responseIatas.value?.result?.carriers!!

                model.responseIatas.value?.result?.matriculas!!.forEach{enrollment.add(it.matricula)}
                cargarAdapters()
                binding.apply {
                    actvEnrollment.setOnItemClickListener { parent, view, position, id ->
                        model.responseIatas.value?.result?.matriculas!!.forEach {
                            if ("${actvEnrollment.text}" == it.matricula) {
                                actvEquip.setText(it.equipo)
                                var tipoCabina =
                                    if (it.tipoCabina == "NB") "Narrow body" else "Wide body"
                                actvTipoCabina.setText(tipoCabina)
                                codigoODS = it.codigoODS
                            }
                        }
                    }
                }
            }
        })
    }

    private fun cargarAdapters() {
        binding.apply {
            spinOrigin.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, origins))
            spinDestiny.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, origins))
            spinCompany.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, companies))
            actvEnrollment.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, enrollment))
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {
                tvDateFlightValue.id -> showSelectDate(tvDateFlightValue)

                tietETADate.id -> showSelectDate(tietETADate)

                tietETDDate.id -> showSelectDate(tietETDDate)

                tietETAHour.id -> showSelectHour(tietETAHour,parentFragmentManager)

                tietETDHour.id -> showSelectHour(tietETDHour,parentFragmentManager)

                actvEquip.id ->{
                    dialogo.mostrarError("Matricula vacia", "Ingresa una matricula valida para ver el equipo")
                    dialogo.btnCerrar.setOnClickListener{ dialogo.Ocultar()}
                }

                btnContinue.id -> {

                    if (isFormComplete()) {
                        v.hideKeyboard()
                        model.insertVueloManual(
                            "${etFlightValue.text}",
                            "${spinOrigin.text}",
                            "${spinDestiny.text}",
                            "${spinCompany.text}",
                            "${actvEnrollment.text}",
                            "${tietETADate.text.toString()} ${tietETAHour.text.toString()}",
                            "${tietETDDate.text.toString()} ${tietETDHour.text.toString()}",
                            "${tvDateFlightValue.text}",
                            codigoODS
                        ).observe(viewLifecycleOwner, {
                            dialogo.mostrarCargando("Espear un momento")
                            if (it != null && it.status == RequestState.REQ_OK) {
                                dialogo.Ocultar()
                                dialogo.mostrarMensajeConfirmacion("Vuelo Agregado", "Se agrego el vuelo con numero ${it.result.vuelos[0].numVuelo}")
                                val vuelo = it.result.vuelos[0]
                                codigoODS = ""
                                dialogo.btnCerrar.visibility = View.GONE
                                dialogo.btnAceptar.setOnClickListener {
                                    dialogo.Ocultar()
                                    launchForm(vuelo)
                                }
                            } else {
                                dialogo.Ocultar()
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.error_conexion) ,
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        })
                    }
                }

                btnBack.id -> {
                    val bundle =
                        bundleOf("ModuloSeleccionado" to Constants.Modulos.Inspeccion_primer_vuelo.name)
                    bundle.putBoolean("btnManual", true)
                    findNavController().navigate(
                        R.id.action_informacionManualVueloFragment2_to_buscarVueloFragment32,
                        bundle
                    )
                }
            }
        }
    }


    private fun showSelectDate(view: TextInputEditText) {
        parentFragmentManager.let { it1 ->
            DatePickerDialogHelper(false, true) { day, month, year ->
                view.setText(changeFormatDate(day, month, year))
            }.show(it1, "DateDialog")
        }
    }

    private fun isFormComplete(): Boolean {
        var flag = false
        dialogo.btnAceptar.visibility = View.GONE
        dialogo.btnCerrar.setOnClickListener{
            dialogo.Ocultar()
        }
        binding.apply {
            val viewsText = arrayOf(tvDateFlightValue, etFlightValue, spinCompany, spinOrigin, spinDestiny, actvEquip, actvEnrollment, tietETDHour, tietETDDate, tietETADate, tietETAHour)
            viewsText.forEach nestor@{
                when {
                    it.text.toString() == "" -> {
                        dialogo.mostrarError("Formulario incompleto", "Para poder agregar un vuelo manualamente, debes llenar todos los campos.")
                        return@nestor
                    }

                    "${spinOrigin.text}" == "${spinDestiny.text}" -> {
                        dialogo.mostrarError("Origen y destino traslapados", "No puedes tener el mismo valor en origen y destino.")
                    }

                    !enrollment.contains(actvEnrollment.text.toString()) -> {
                        dialogo.mostrarError("Matrícula inválida", "Por favor ingresa una matrícula válida.")
                    }

                    else -> flag = true
                }
            }
        }
        return flag
    }

    private fun launchForm(flightEntity: Vuelos) {
        when (arguments?.getString("ModuloSeleccionado")) {
            Constants.Modulos.Inspeccion_primer_vuelo.toString() -> {
                val action = InformacionManualVueloFragmentDirections.actionInformacionManualVueloFragment2ToNavInspeccionAeronavePrimerVueloDia2(
                    flightEntity
                )
                findNavController().navigate(action)
            }

            Constants.Modulos.Deshielo.toString() -> {
                val action = InformacionManualVueloFragmentDirections.actionInformacionManualVueloFragment2ToNavDeshielo(
                    flightEntity
                )
                findNavController().navigate(action)
            }

            Constants.Modulos.Inspeccion_Aviones.toString() ->{
                val action = InformacionManualVueloFragmentDirections.actionInformacionManualVueloFragment2ToNavInspecionAeronave(
                    flightEntity
                )
                findNavController().navigate(action)
            }

            Constants.Modulos.Orden_carga_combustible.toString() -> {
                val action = InformacionManualVueloFragmentDirections.actionInformacionManualVueloFragment2ToNavOrdenCargaCombustible(
                    flightEntity
                )
                findNavController().navigate(action)
            }

            Constants.Modulos.Search_list.toString() -> {
                val action = InformacionManualVueloFragmentDirections.actionInformacionManualVueloFragment2ToNavSearchList(
                    flightEntity
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupActionBar(){
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.busqueda_mnual)

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