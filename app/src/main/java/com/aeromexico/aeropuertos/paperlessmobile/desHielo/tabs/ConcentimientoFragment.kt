package com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.DialogFragmentSignature
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentConcentimientoBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.viewmodel.DesHieloViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.snackbar.Snackbar

class ConcentimientoFragment(val setScrollState: (ScrollState?) -> Unit) : Fragment(),
    ObservableScrollViewCallbacks {
    lateinit var binding: FragmentConcentimientoBinding
    lateinit var model: DesHieloViewModel
    var capitan: UsuarioCore? = null
    var aplicador: UsuarioCore? = null
    var oficinalOperaciones: UsuarioCore? = null
    var firmaoficinalOperaciones: String? = null
    lateinit var d: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConcentimientoBinding.inflate(layoutInflater)
        model = DesHieloViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        d = Dialogo(requireContext())
        binding.apply {
            scrollView.setScrollViewCallbacks(this@ConcentimientoFragment)
            unableSignatureImageView(true,firmaOficialOperaciones)
            btnBuscarEmpleadoCapitan.setOnClickListener {
                getCapitan()
            }
            btnBuscarEmpleadoAplicador.setOnClickListener {
                getAplicador()
            }
            btnBuscarEmpleadoOficialOperaciones.setOnClickListener {
                getOficial()
            }
            btnFirmarOfOperaciones.setOnClickListener { showSignatureDialog() }
            firmaOficialOperaciones.setOnClickListener { showSignatureDialog() }

            swEmpExternoOfi.setOnClickListener {

                oficinalOperaciones = null
                firmaoficinalOperaciones = null
                firmaOficialOperaciones.visibility= View.GONE
                btnFirmarOfOperaciones.visibility= View.GONE

                textOficialOperaciones.setText("")
                editTextOficial.setText("")

                if (swEmpExternoOfi.isChecked) {
                    println("externo")
                    tiltextOficialOperaciones.visibility = View.VISIBLE
                    textOficialOperaciones.isEnabled = true
                    tilTextOficial.prefixText = ""

                    tvNotaOficialOperaciones.text = getString(R.string.ingresa_nombre_emp_y_firma)
                    btnBuscarEmpleadoOficialOperaciones.visibility = View.GONE
                    textOficialOperaciones.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            if (textOficialOperaciones.text.toString().trim().length < 5) {
                                textOficialOperaciones.error =
                                    getString(R.string.error_campo_invalido)
                            } else {
                                textOficialOperaciones.error = null
                                btnFirmarOfOperaciones.visibility = View.VISIBLE
                            }
                        }

                    })

                } else {
                    println("interno")
                    tiltextOficialOperaciones.visibility = View.GONE
                    textOficialOperaciones.isEnabled = false
                    tilTextOficial.prefixText = "AM"
                    btnBuscarEmpleadoOficialOperaciones.visibility = View.VISIBLE
                    tvNotaOficialOperaciones.text = getString(R.string.ingresar_n_mero_de_empleado)

                }
            }
        }

        return binding.root
    }

    private fun showSignatureDialog() {
        parentFragmentManager.let {
            DialogFragmentSignature() { bitmap ->
                unableSignatureImageView(false,binding.firmaOficialOperaciones)
                binding.firmaOficialOperaciones.setImageBitmap(bitmap)
                firmaoficinalOperaciones = CreateImageFile.getB64FromBitmap(bitmap)
            }.show(it, "SignatureFragment")
        }
    }

    private fun getOficial() {
        binding.apply {
            if (!editTextOficial.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getOficialOperaciones("${editTextOficial.text.toString()}" ).observeOnce(Observer {
                    d.Ocultar()
                    it.let {
                        oficinalOperaciones = it.result.usuarioCore
                        btnFirmarOfOperaciones.visibility= View.VISIBLE
                        tiltextOficialOperaciones.visibility = View.VISIBLE
                        textOficialOperaciones.setText("${ oficinalOperaciones?.name} ${ oficinalOperaciones?.apellidoPaterno} ${ oficinalOperaciones?.apellidoMaterno}")
                    }
                })
            } else {
                editTextOficial.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    private fun getAplicador() {
        binding.apply {
            if (!editTextAplicador.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getAplicador("${editTextAplicador.text.toString()}" ).observeOnce(Observer {
                    d.Ocultar()
                    it.let {
                        aplicador = it.result.usuarioCore
                        tiltextNombreAplicador.visibility = View.VISIBLE
                        textNombreAplicador.setText("${ aplicador?.name} ${ aplicador?.apellidoPaterno} ${ aplicador?.apellidoMaterno}")
                    }
                })
            } else {
                editTextAplicador.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    private fun getCapitan() {
        binding.apply {
            if (!editTextCapitan.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getCapitan("${editTextCapitan.text.toString()}" ).observeOnce(Observer {
                    d.Ocultar()
                    it.let {
                        capitan = it.result.usuarioCore
                        tiltextNombreCapitan.visibility = View.VISIBLE
                        textNombreCapitan.setText("${ capitan?.name} ${ capitan?.apellidoPaterno} ${ capitan?.apellidoMaterno}")
                    }
                })
            } else {
                editTextCapitan.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    fun comprobacion(): Boolean {
        var allOk = false
        if (binding.swEmpExternoOfi.isChecked) {
            if (!binding.editTextOficial.text.toString().trim().isNullOrEmpty()) {

                if (binding.textOficialOperaciones.text.toString().isNullOrEmpty()) {
                    binding.textOficialOperaciones.error = getString(R.string.error_campo_invalido)
                } else {

                    var externo = UsuarioCore(
                        employeeNumber = binding.editTextOficial.text.toString().toInt(),
                        name = binding.textOficialOperaciones.text.toString(),
                        apellidoMaterno = "",
                        apellidoPaterno = ""
                    )
                    oficinalOperaciones = externo
                }
            } else {
                binding.editTextOficial.error = getString(R.string.error_campo_invalido)
            }
        }

        if (capitan?.name.isNullOrEmpty() || capitan == null) {
            getCapitan()
            Snackbar.make(binding.root, "Se requiere capitan", Snackbar.LENGTH_SHORT).show()
        } else if (aplicador?.name.isNullOrEmpty() || aplicador == null) {
            getAplicador()
            Snackbar.make(binding.root, "Se requiere aplicador", Snackbar.LENGTH_SHORT).show()
        } else if (oficinalOperaciones?.name.isNullOrEmpty() || oficinalOperaciones == null) {
            getOficial()
            Snackbar.make(binding.root, "Se requiere oficinal de operaciones", Snackbar.LENGTH_SHORT).show()
        } else if (firmaoficinalOperaciones.isNullOrEmpty()) {
            unableSignatureImageView(true,binding.firmaOficialOperaciones)
            Snackbar.make(binding.root, "Se requiere firma de  oficinal de operaciones", Snackbar.LENGTH_SHORT).show()
        } else {
            allOk = true
        }
        return allOk
    }
    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {

    }

    override fun onDownMotionEvent() {

    }

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
        println(" -> scroll consentimiento ${binding.scrollView.scrollY}" )
        if(scrollState == ScrollState.DOWN){
            if (binding.scrollView.scrollY < 100) {
                setScrollState.invoke(scrollState)
            }
        }else{
            setScrollState.invoke(scrollState)
        }

    }

}