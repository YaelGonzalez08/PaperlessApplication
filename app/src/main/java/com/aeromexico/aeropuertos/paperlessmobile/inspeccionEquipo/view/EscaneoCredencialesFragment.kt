package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentEscaneoCredencialesBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.viewModels.EscaneoCredencialesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResultEquipo
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.snackbar.Snackbar

class EscaneoCredencialesFragment : Fragment() {
    lateinit var binding: FragmentEscaneoCredencialesBinding
    lateinit var model: EscaneoCredencialesViewModel
    lateinit var dialogo: Dialogo
    private var mActivity: MainActivity? = null
    var operador: UsuarioCore? = null
    var supervisor: UsuarioCore? = null
    var resultEquipo: ResultEquipo? = ResultEquipo(null, null)


    override fun onResume() {
        operador = null
        supervisor = null
        showAeromexicodatosSupervisor()
        showAeromexicodatosdatosOperador()

        resultEquipo = ResultEquipo(null, null)
        binding.apply {
            txtCodigoEquipo.setText("")
        }
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEscaneoCredencialesBinding.inflate(inflater, container, false)
        setupActionBar()
        model = EscaneoCredencialesViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        dialogo = Dialogo(requireContext())
        setBackButtonSystem(requireActivity())

        binding.apply {
            btnContinuar.setOnClickListener {
                it.hideKeyboard()
                if (verificarForm()) {
                    val action = EscaneoCredencialesFragmentDirections.actionEscaneoCredencialesFragmentToCheckListDiarioFragment(operador,supervisor,resultEquipo)
                    findNavController().navigate(action)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Verifica que los campos estén completos.",
                        Snackbar.LENGTH_SHORT
                    ).show();
                }
            }

            btnRegresar.setOnClickListener {
                mActivity?.onBackPressed()
            }

            txtCodigoEquipo.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getEquipo()
                    v.hideKeyboard()
                }
                    true
            }
            datosSupervisor.btnConsultarEmpleado.setOnClickListener {
                getSupervisor()
            }
            datosSupervisor.txtTitulo.text = getString(R.string.txt_supervisor)
            datosSupervisor.swEmpExterno.setOnClickListener {
                if (datosSupervisor.swEmpExterno.isChecked)
                    showNoAeromexicodatosSupervisor()
                else
                    showAeromexicodatosSupervisor()
            }
            datosOperador.btnConsultarEmpleado.setOnClickListener {
                getOperador()
            }
            datosOperador.txtTitulo.text = getString(R.string.operador)
            datosOperador.swEmpExterno.setOnClickListener {
                if (datosOperador.swEmpExterno.isChecked)
                    showNoAeromexicodatosdatosOperador()
                else
                    showAeromexicodatosdatosOperador()
            }

            txtCodigoEquipo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (txtCodigoEquipo.text.toString().length > 4) {
                        var escan = txtCodigoEquipo.text.toString()
                        if (escan.indexOf("Eco.\"") != -1) {
                            var esca2 = escan.substring(escan.indexOf("Eco.\""), escan.length).trim()
                            txtCodigoEquipo.setText(esca2.substringAfter(" ") ?: esca2)
                        } else
                            if (escan.indexOf("\n") != -1) {
                                var esca2 = escan.substringBefore("\n")
                                txtCodigoEquipo.setText(esca2.substringAfter(" ") ?: esca2)
                            } else if (txtCodigoEquipo.text.toString().length > 10) {
                                var esca2 = escan.substring(0, 6)
                                txtCodigoEquipo.setText(esca2.substringAfter(" ") ?: esca2)
                            }
                    }
                }

            })


            btnScannEquipo.setOnClickListener {
                getEquipo()
                it.hideKeyboard()
            }

            btnRescanEquipo.setOnClickListener {
                resetEquipo()
            }

        }

        return binding.root
    }

    private fun verificarForm(): Boolean {
        binding.apply {
            if (datosOperador.swEmpExterno.isChecked) {

                if (datosOperador.txtNumEmpleado.text.isNullOrEmpty()) {
                    return false
                } else if (datosOperador.actvOficialNombre.text.isNullOrEmpty()) {
                    return false
                } else {
                    operador = UsuarioCore()
                    operador?.employeeNumber =
                        datosOperador.txtNumEmpleado.text.toString().toInt()
                    operador?.name = datosOperador.actvOficialNombre.text.toString()
                }
            }

            if (datosSupervisor.swEmpExterno.isChecked) {
                if (datosSupervisor.txtNumEmpleado.text.isNullOrEmpty()) {
                    return false
                } else if (datosSupervisor.actvOficialNombre.text.isNullOrEmpty()) {
                    return false
                } else {
                    supervisor = UsuarioCore()

                    supervisor?.employeeNumber =
                        datosSupervisor.txtNumEmpleado.text.toString().toInt()
                    supervisor?.name = datosSupervisor.actvOficialNombre.text.toString()
                }
            }

            return !(operador == null || supervisor == null || resultEquipo!!.equipo == null)
        }
    }

    private fun showAeromexicodatosdatosOperador() {
        binding.apply {
            datosOperador.apply {
                tilNombreEmpleadoManual.visibility = View.GONE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.VISIBLE
/*
                unableSignatureImageView(false, ivFirma)
                ivFirma.visibility = View.GONE

                btnFirma.apply {
                    visibility = View.GONE
                    isEnabled = true
                }*/
                actvOficialNombre.apply {
                    isEnabled = false
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = "AM"
                }
            }
        }

        operador = null
    }

    private fun showNoAeromexicodatosdatosOperador() {
        binding.apply {
            datosOperador.apply {
                tilNombreEmpleadoManual.visibility = View.VISIBLE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.GONE
/*
                unableSignatureImageView(false, ivFirma)

                btnFirma.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }*/
                actvOficialNombre.apply {
                    isEnabled = true
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = ""
                }
            }
        }
        operador = null
    }

    private fun showAeromexicodatosSupervisor() {
        binding.apply {
            datosSupervisor.apply {
                tilNombreEmpleadoManual.visibility = View.GONE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.VISIBLE
/*
                unableSignatureImageView(false, ivFirma)
                ivFirma.visibility = View.GONE

                btnFirma.apply {
                    visibility = View.GONE
                    isEnabled = true
                }*/
                actvOficialNombre.apply {
                    isEnabled = false
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = "AM"
                }
            }
        }

        supervisor = null

    }

    private fun showNoAeromexicodatosSupervisor() {
        binding.apply {
            datosSupervisor.apply {
                tilNombreEmpleadoManual.visibility = View.VISIBLE
                tilNombreEmpleadoManual.isErrorEnabled = false
                actvOficialNombre.setText("")
                txtNumEmpleado.setText("")
                btnConsultarEmpleado.visibility = View.GONE
/*
                unableSignatureImageView(false, ivFirma)

                btnFirma.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }*/
                actvOficialNombre.apply {
                    isEnabled = true
                    setTextColor(resources.getColor(R.color.black))
                }
                tilNumEmpleado.prefixTextView.apply {
                    text = ""
                }
            }
        }
        supervisor = null
    }

    private fun resetEquipo() {
        binding.apply {
            resultEquipo?.equipo = null
            txtCodigoEquipo.setText("")
            txtCodigoEquipo.visibility = View.VISIBLE
            txtEquipoDescription.visibility = View.GONE
            txtNoEquipo.visibility = View.GONE
        }
    }

    private fun getSupervisor() {
        binding.apply {
            if (datosSupervisor.txtNumEmpleado.text.isNullOrEmpty()) {
                datosSupervisor.txtNumEmpleado.error = getString(R.string.error_campo_invalido)

            } else {
                dialogo.mostrarCargando(null)
                model.getSupervisor("${datosSupervisor.txtNumEmpleado.text.toString()}")
                    .observe(viewLifecycleOwner, Observer {
                        dialogo.Ocultar()
                        binding.root.hideKeyboard()
                        if (it == null) {
                            Snackbar.make(binding.root, "Error Revisa tu conexion", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.root, "${if(it.status == RequestState.REQ_OK && it.result != null) "Consulta exitosa." else "Verifica supervisor."}", Snackbar.LENGTH_SHORT).show();
                            if (it.status == RequestState.REQ_OK && it.result != null) {
                                supervisor = it.result.usuarioCore
                                datosSupervisor.txtNumEmpleado.isActivated = false
                                datosSupervisor.tilNombreEmpleadoManual.visibility = View.VISIBLE
                                datosSupervisor.actvOficialNombre.visibility = View.VISIBLE
                                datosSupervisor.actvOficialNombre.setText( supervisor?.name)
                                datosSupervisor.txtNumEmpleado.setText(supervisor?.employeeNumber.toString())
                                datosSupervisor.actvOficialNombre.setText( "${supervisor?.name} ${supervisor?.apellidoPaterno} ${supervisor?.apellidoMaterno}")

                            }
                        }
                    })
            }
        }
    }

    private fun getOperador() {

        binding.apply {
            if (datosOperador.txtNumEmpleado.text.isNullOrEmpty()) {
                datosOperador.txtNumEmpleado.error = getString(R.string.error_campo_invalido)
            } else {
                dialogo.mostrarCargando(null)
                model.getOperador("${datosOperador.txtNumEmpleado.text.toString()}")
                    .observe(viewLifecycleOwner, Observer {
                        dialogo.Ocultar()
                        binding.root.hideKeyboard()
                        if (it == null) {
                            Snackbar.make(binding.root, getString(R.string.error_conexion), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.root, "${if(it.status == RequestState.REQ_OK && it.result != null) "Consulta exitosa." else "Verifica operador."}", Snackbar.LENGTH_SHORT)
                                .show();
                            if (it.status == RequestState.REQ_OK && it.result != null) {
                                operador = it.result.usuarioCore
                                datosOperador.txtNumEmpleado.isActivated = false
                                datosOperador.tilNombreEmpleadoManual.visibility = View.VISIBLE
                                datosOperador.actvOficialNombre.visibility = View.VISIBLE
                                datosOperador.actvOficialNombre.setText(operador?.name)
                                datosOperador.txtNumEmpleado.setText(operador?.employeeNumber.toString())
                                datosOperador.actvOficialNombre.setText( "${operador?.name} ${operador?.apellidoPaterno} ${operador?.apellidoMaterno}")

                            }
                        }
                    })
            }
        }
    }

    private fun getEquipo() {
        binding.apply {
            if (txtCodigoEquipo.text.isNullOrEmpty()) {
                txtCodigoEquipo.error = getString(R.string.error_campo_invalido)
            } else {
                dialogo.mostrarCargando(null)
                model.getEquipo("${txtCodigoEquipo.text.toString()}")
                    .observe(viewLifecycleOwner, Observer {
                        dialogo.Ocultar()
                        binding.root.hideKeyboard()
                        if (it == null) {
                            Snackbar.make(binding.root, getString(R.string.error_conexion), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.root, "${if(it.status == RequestState.REQ_OK && it.result != null) "Consulta exitosa." else "Verifica equipo"}", Snackbar.LENGTH_SHORT).show();
                            if (it.status == RequestState.REQ_OK && it.result != null) {
                                resultEquipo?.equipo = it.result.equipo
                                txtCodigoEquipo.visibility = View.GONE
                                txtNoEquipo.visibility = View.VISIBLE
                                txtEquipoDescription.visibility = View.VISIBLE
                                txtNoEquipo.text = resultEquipo?.equipo?.numeroActivo.toString()
                                txtEquipoDescription.text = "${resultEquipo?.equipo?.descripcion}"
                                it.result.preguntas.also {
                                    resultEquipo?.preguntas = it
                                  //  Snackbar.make(binding.root, "Checks ${it?.size}", Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        }
                    })
            }
        }
    }

    private fun showHomeFragment(){
        startActivity(Intent(activity, MainActivity::class.java))
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.tittle_checklist_diario)
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
                showHomeFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showHomeFragment()
                }
            }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
        }
}