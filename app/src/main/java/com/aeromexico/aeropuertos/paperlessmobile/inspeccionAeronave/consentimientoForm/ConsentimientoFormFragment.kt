package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.SilkySignature.viewModel.SignatureViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.AveriaInspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentConsentimientoFormBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.ResponsableEstibaAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.ResponsableEstibaClickListener
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel.ConsentimientoFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager.viewModel.PagerViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponsablesEstiba
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.*

class ConsentimientoFormFragment : Fragment(), ResponsableEstibaClickListener {
    //Binding
    private lateinit var mBinding: FragmentConsentimientoFormBinding

    //ViewModels
    private val consentimientoFormViewModel: ConsentimientoFormViewModel by activityViewModels()
    private val inspeccionAeronaveViewModel: InspeccionAeronaveViewModel by activityViewModels()
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()
    private val signatureViewModel: SignatureViewModel by activityViewModels()
    private val pagerViewModel: PagerViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //Main activity (necesaria para ocultar el teclado)
    //Main activity (necesaria para ocultar el teclado)
    private var mActivity: MainActivity? = null

    //Auxiliar para manipular interfaz
    private var firmaSeleccionada: String = ""

    //RecyclerView
    private lateinit var mAdapter: ResponsableEstibaAdapter
    private lateinit var gridLayout: GridLayoutManager

    //Diálogo (loading)
    //private lateinit var dialogoConfirmar: Dialogo
    private var accionBtnCerrar = "CERRAR"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.observeViewModel()

        val prefixViewOfOp =
            mBinding.tilOficialOperacion.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
        prefixViewOfOp.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        prefixViewOfOp.gravity = Gravity.CENTER
        prefixViewOfOp.textSize = 12f


        val prefixViewtecMant =
            mBinding.tilTecnicoMantenimiento.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
        prefixViewtecMant.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        prefixViewtecMant.gravity = Gravity.CENTER
        prefixViewtecMant.textSize = 12f
    }


    fun observeViewModel() {

        this.consentimientoFormViewModel.getResponsablesEstiba().observe(viewLifecycleOwner, {
           // this.mAdapter.setList(it)
        })

        inspeccionAeronaveViewModel.getGuardadoLocal().observe(viewLifecycleOwner, {
            if (it) {
                Log.i(
                    "INSPEC_AERONAVE",
                    "Se ha guardado exitosamente el reporte completo en bd local"
                )
                mainViewModel.inspecAeronaveCount()
            }
        })

        consentimientoFormViewModel.getOfOpEsExterno().observe(viewLifecycleOwner, {

            mBinding.switchEsAM.isChecked = it
            mBinding.btnFirmarOfOperaciones.visibility = View.GONE

            if (it) {
                mBinding.containerOfOpExterno.visibility = View.VISIBLE
                mBinding.containerOfOpInterno.visibility = View.GONE

                mBinding.tvNotaOfOperacion.text = getString(R.string.ingresa_nombre_emp_y_firma)

            } else {
                mBinding.containerOfOpExterno.visibility = View.GONE
                mBinding.containerOfOpInterno.visibility = View.VISIBLE

                mBinding.tvNotaOfOperacion.text = getString(R.string.ingresa_no_emp_y_firma)

            }
        })

        consentimientoFormViewModel.getTecManEsExterno().observe(viewLifecycleOwner, {

            mBinding.switchEsAMTecMant.isChecked = it
            mBinding.btnFirmarTecMantenimiento.visibility = View.GONE

            if (it) {
                mBinding.containerTecMantExterno.visibility = View.VISIBLE
                mBinding.containerTecMantInterno.visibility = View.GONE

                mBinding.tvNotaTecnicoMantenimiento.text =
                    getString(R.string.ingresa_nombre_emp_y_firma)

            } else {
                mBinding.containerTecMantExterno.visibility = View.GONE
                mBinding.containerTecMantInterno.visibility = View.VISIBLE

                mBinding.tvNotaTecnicoMantenimiento.text =
                    getString(R.string.ingresa_no_emp_y_firma)

            }
        })

        consentimientoFormViewModel.getRespEstibaEsExterno().observe(viewLifecycleOwner, {
            if (it) {
                mBinding.containerRespEstibaExterno.visibility = View.VISIBLE
                mBinding.containerRespEstibaInterno.visibility = View.GONE
            } else {
                mBinding.containerRespEstibaExterno.visibility = View.GONE
                mBinding.containerRespEstibaInterno.visibility = View.VISIBLE
            }
        })

        inspeccionFormViewModel.getEsLlegada().observe(viewLifecycleOwner, {
            if(it){
                mBinding.containerRespEstiba.visibility = View.GONE
                mBinding.recyclerViewRespEstiba.visibility = View.GONE
                mBinding.tvResponsablesEstiba.visibility = View.GONE
                mBinding.tvEsAmRespEstiba.visibility = View.GONE
                mBinding.switchEsAMRespEstiba.visibility = View.GONE
                mBinding.tvNotaResponsablesEstiba.visibility = View.GONE


            }else{
                mBinding.containerRespEstiba.visibility = View.VISIBLE
                mBinding.recyclerViewRespEstiba.visibility = View.VISIBLE
                mBinding.tvResponsablesEstiba.visibility = View.VISIBLE
                mBinding.tvEsAmRespEstiba.visibility = View.VISIBLE
                mBinding.switchEsAMRespEstiba.visibility = View.VISIBLE
                mBinding.tvNotaResponsablesEstiba.visibility = View.VISIBLE
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consentimientoFormViewModel.responsablesEstiba.value = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentConsentimientoFormBinding.inflate(inflater, container, false)

        this.setListeners()
        this.setListenersStepper()

        mActivity = activity as? MainActivity

        this.setupRecyclerView()

        return mBinding.root
    }

    //Listeners de stepper
    fun setListenersStepper() {
        this.mBinding.btnAnterior.setOnClickListener {
            this.pagerViewModel.anteriorStep()
        }
    }


    override fun onResume() {
        super.onResume()

        val firmaOfOp = this.consentimientoFormViewModel.getBase64OfOperaciones().value
        if (firmaOfOp != null && firmaOfOp != "") {
            this.mostrarFirmaOfOperaciones(firmaOfOp)
        }

        val nombreOfOp = this.consentimientoFormViewModel.getNombreOfOperaciones().value
        if (nombreOfOp != null && nombreOfOp != "") {
            this.mostrarNombreOfOperaciones(nombreOfOp)
        }


        val firmaTecMant = this.consentimientoFormViewModel.getBase64TecMantenimiento().value
        if (firmaTecMant != null && firmaTecMant != "") {
            this.mostrarFirmaTecMantenimiento(firmaTecMant)
        }

        val nombreTecMan = this.consentimientoFormViewModel.getNombreTecMantenimiento().value
        if (nombreTecMan != null && nombreTecMan != "") {
            this.mostrarNombretecMantenimiento(nombreTecMan)
        }

        if (inspeccionFormViewModel.existenAverias()) {
            mBinding.tvEsAmTecMant.visibility = View.VISIBLE
            mBinding.switchEsAMTecMant.visibility = View.VISIBLE
            mBinding.divider1.visibility = View.VISIBLE
            mBinding.tvTecnicoMantenimiento.visibility = View.VISIBLE
            mBinding.tvNotaTecnicoMantenimiento.visibility = View.VISIBLE
            mBinding.btnBuscarEmpleadoTecMantenimiento.visibility = View.VISIBLE
            mBinding.tilTecnicoMantenimiento.visibility = View.VISIBLE
            mBinding.containerFirmaTecnicoMantenimiento.visibility = View.VISIBLE
        } else {
            mBinding.tvEsAmTecMant.visibility = View.GONE
            mBinding.switchEsAMTecMant.visibility = View.GONE
            mBinding.divider1.visibility = View.GONE
            mBinding.tvTecnicoMantenimiento.visibility = View.GONE
            mBinding.tvNotaTecnicoMantenimiento.visibility = View.GONE
            mBinding.btnBuscarEmpleadoTecMantenimiento.visibility = View.GONE
            mBinding.tilTecnicoMantenimiento.visibility = View.GONE
            mBinding.containerFirmaTecnicoMantenimiento.visibility = View.GONE
        }

    }

    //Listeners de botones y firma
    private fun setListeners() {
        //Botones para capturar firma
        mBinding.btnFirmarOfOperaciones.setOnClickListener {
            this.consentimientoFormViewModel.setBase64OfOperaciones("")
            this.launchSignaturePad("oficial_operaciones")
        }
        mBinding.btnFirmarTecMantenimiento.setOnClickListener {
            this.consentimientoFormViewModel.setBase64TecMantenimiento("")
            this.launchSignaturePad("tecnico_mantenimiento")
        }

        //Oficial de operaciones, técnico de mantenimiento y responsables de estiba (interno / externo)
        mBinding.switchEsAM.setOnClickListener {
            val esAM = mBinding.switchEsAM.isChecked

            consentimientoFormViewModel.setBase64OfOperaciones("")
            consentimientoFormViewModel.setNombreOfOperaciones("")
            consentimientoFormViewModel.setNoEmpOfOperaciones("")
            mBinding.ivOfOperaciones.visibility = View.GONE
            mBinding.btnFirmarOfOperaciones.text = getString(R.string.firmar)
            mBinding.btnFirmarOfOperaciones.visibility = View.GONE

            mBinding.tvNombreOfOperaciones.visibility = View.GONE
            mBinding.etOfialNombreOperacionExterno.setText("")
            mBinding.etNoEmpOfialOperacion.setText("")
            mBinding.etNoEmpOfialOperacionExterno.setText("")


            consentimientoFormViewModel.setOfOpEsExterno(esAM)
        }

        mBinding.switchEsAMTecMant.setOnClickListener {
            val esAM = mBinding.switchEsAMTecMant.isChecked

            consentimientoFormViewModel.setBase64TecMantenimiento("")
            consentimientoFormViewModel.setNombreTecMantenimiento("")
            consentimientoFormViewModel.setNoEmpTecMantenimiento("")
            mBinding.ivTecnicoMantenimiento.visibility = View.GONE
            mBinding.btnFirmarTecMantenimiento.text = getString(R.string.firmar)
            mBinding.btnFirmarTecMantenimiento.visibility = View.GONE

            mBinding.tvNombreTecMantenimiento.visibility = View.GONE
            mBinding.etTecnicoNombreMantenimientoExterno.setText("")
            mBinding.etNoEmpTecnicoMantenimiento.setText("")
            mBinding.etNoEmpTecnicoMantenimientoExterno.setText("")


            consentimientoFormViewModel.setTecManEsExterno(esAM)
        }

        mBinding.switchEsAMRespEstiba.setOnClickListener {
            val esAM = mBinding.switchEsAMRespEstiba.isChecked

            mBinding.etNoEmpRespEstibaExterno.setText("")
            mBinding.etNombreRespEstibaExterno.setText("")
            mBinding.etResponsableEstiba.setText("")

            consentimientoFormViewModel.setRespEstibaEsExterno(esAM)
        }

        mBinding.etOfialNombreOperacionExterno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if (consentimientoFormViewModel.getOfOpEsExterno().value!!) {
                    consentimientoFormViewModel.setNombreOfOperaciones(
                        mBinding.etOfialNombreOperacionExterno.text.toString().trim()
                    )

                    if (!consentimientoFormViewModel.getNoEmpOfOperaciones().value.isNullOrEmpty() && !consentimientoFormViewModel.getNombreOfOperaciones().value.isNullOrEmpty()) {
                        mBinding.btnFirmarOfOperaciones.visibility = View.VISIBLE
                    } else {
                        mBinding.btnFirmarOfOperaciones.visibility = View.GONE
                        reiniciarFirmaOfOp()
                    }
                }

            }
        })

        mBinding.etNoEmpOfialOperacionExterno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (consentimientoFormViewModel.getOfOpEsExterno().value!!) {
                    consentimientoFormViewModel.setNoEmpOfOperaciones(
                        mBinding.etNoEmpOfialOperacionExterno.text.toString().trim()
                    )

                    if (!consentimientoFormViewModel.getNoEmpOfOperaciones().value.isNullOrEmpty() && !consentimientoFormViewModel.getNombreOfOperaciones().value.isNullOrEmpty()) {
                        mBinding.btnFirmarOfOperaciones.visibility = View.VISIBLE
                    } else {
                        mBinding.btnFirmarOfOperaciones.visibility = View.GONE
                        reiniciarFirmaOfOp()
                    }
                }

            }
        })


        mBinding.etTecnicoNombreMantenimientoExterno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if (consentimientoFormViewModel.getTecManEsExterno().value!!) {
                    consentimientoFormViewModel.setNombreTecMantenimiento(
                        mBinding.etTecnicoNombreMantenimientoExterno.text.toString().trim()
                    )

                    if (!consentimientoFormViewModel.getNoEmpTecMantenimiento().value.isNullOrEmpty() && !consentimientoFormViewModel.getNombreTecMantenimiento().value.isNullOrEmpty()) {
                        mBinding.btnFirmarTecMantenimiento.visibility = View.VISIBLE
                    } else {
                        mBinding.btnFirmarTecMantenimiento.visibility = View.GONE
                        reiniciarFirmaTecMant()
                    }
                }

            }
        })

        mBinding.etNoEmpTecnicoMantenimientoExterno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (consentimientoFormViewModel.getTecManEsExterno().value!!) {
                    consentimientoFormViewModel.setNoEmpTecMantenimiento(
                        mBinding.etNoEmpTecnicoMantenimientoExterno.text.toString().trim()
                    )

                    if (!consentimientoFormViewModel.getNoEmpTecMantenimiento().value.isNullOrEmpty() && !consentimientoFormViewModel.getNombreTecMantenimiento().value.isNullOrEmpty()) {
                        mBinding.btnFirmarTecMantenimiento.visibility = View.VISIBLE
                    } else {
                        mBinding.btnFirmarTecMantenimiento.visibility = View.GONE
                        reiniciarFirmaTecMant()
                    }
                }

            }
        })


        //Botones para búsquedas en CORE
        mBinding.btnBuscarEmpleadoOfOperaciones.setOnClickListener {

            //Bloquear botón
            this.mBinding.btnBuscarEmpleadoOfOperaciones.isEnabled = false

            //Validando formato de no. de empleado ingresado
            var noEmpleado = mBinding.etNoEmpOfialOperacion.text.toString().trim()
            if (this.validaNoEmpleado(noEmpleado)) {

                //Mostrar loading
                val dialogo = Dialogo(requireContext())
                dialogo.mostrarCargando(getString(R.string.buscando_empleado))

                //Realizar petición
                consentimientoFormViewModel.buscarOficial(noEmpleado)
                    .observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            if (it.result != null && it.result.usuarioCore != null) {
                                var nombre = it.result.usuarioCore.name + " " + it.result.usuarioCore.apellidoPaterno + " " + it.result.usuarioCore.apellidoMaterno
                                this.consentimientoFormViewModel.setNombreOfOperaciones(nombre)
                                this.consentimientoFormViewModel.setNoEmpOfOperaciones(it.result.usuarioCore.employeeNumber.toString())
                                this.mostrarNombreOfOperaciones(nombre)
                                mBinding.root.hideKeyboard()
                                dialogo.Ocultar();
                                this.mBinding.btnBuscarEmpleadoOfOperaciones.isEnabled = true
                            } else {
                                dialogo.Ocultar()
                                Snackbar.make(
                                    mBinding.root,
                                    "Ha ocurrido un error.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                this.mBinding.btnBuscarEmpleadoOfOperaciones.isEnabled = true
                            }
                        } else {
                            dialogo.Ocultar()
                            Snackbar.make(
                                mBinding.root,
                                "Ha ocurrido un error.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            this.mBinding.btnBuscarEmpleadoOfOperaciones.isEnabled = true
                        }

                    })

            } else {
                Snackbar.make(
                    mBinding.root,
                    "Por favor ingresa un número de empleado válido.",
                    Snackbar.LENGTH_SHORT
                ).show()
                this.mBinding.btnBuscarEmpleadoOfOperaciones.isEnabled = true
            }

        }

        mBinding.btnBuscarEmpleadoTecMantenimiento.setOnClickListener {

            //Bloquear botón
            this.mBinding.btnBuscarEmpleadoTecMantenimiento.isEnabled = false

            //Validando formato de no. de empleado ingresado
            var noEmpleado = mBinding.etNoEmpTecnicoMantenimiento.text.toString().trim()
            if (this.validaNoEmpleado(noEmpleado)) {

                //Mostrar loading
                val dialogo = Dialogo(requireContext())
                dialogo.mostrarCargando(getString(R.string.buscando_empleado))

                //Realizar petición
                consentimientoFormViewModel.buscarTecnico(noEmpleado)
                    .observe(viewLifecycleOwner, Observer {
                        if (it != null) {

                                if (it.result != null && it.result.usuarioCore != null) {
                                    //Mostrar resultado
                                    var nombre =
                                        it.result.usuarioCore.name + " " + it.result.usuarioCore.apellidoPaterno + " " + it.result.usuarioCore.apellidoMaterno

                                    this.consentimientoFormViewModel.setNombreTecMantenimiento(
                                        nombre
                                    )
                                    this.consentimientoFormViewModel.setNoEmpTecMantenimiento(it.result.usuarioCore.employeeNumber.toString())
                                    this.mostrarNombretecMantenimiento(nombre)
                                    mBinding.root.hideKeyboard()
                                    dialogo.Ocultar()
                                    this.mBinding.btnBuscarEmpleadoTecMantenimiento.isEnabled = true
                                } else {
                                    dialogo.Ocultar()
                                    Snackbar.make(
                                        mBinding.root,
                                        "Ha ocurrido un error.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    this.mBinding.btnBuscarEmpleadoTecMantenimiento.isEnabled = true
                                }
                        } else {
                            dialogo.Ocultar()
                            Snackbar.make(
                                mBinding.root,
                                "Ha ocurrido un error.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            this.mBinding.btnBuscarEmpleadoTecMantenimiento.isEnabled = true

                        }


                    })
            } else {
                Snackbar.make(
                    mBinding.root,
                    "Por favor ingresa un número de empleado válido.",
                    Snackbar.LENGTH_SHORT
                ).show()
                this.mBinding.btnBuscarEmpleadoTecMantenimiento.isEnabled = true
            }

        }

        mBinding.btnNuevoResponsableEstiba.setOnClickListener {
            //Bloquear botón
            this.mBinding.btnNuevoResponsableEstiba.isEnabled = false

            //Validando cantidad de responsables de estiba (se admiten hasta 6)
            if (this.mAdapter.itemCount < 6) {

                //Validando formato de no. de empleado ingresado
                var noEmpleado = mBinding.etResponsableEstiba.text.toString().trim()
                if (this.validaNoEmpleado(noEmpleado)) {

                    //Validando que no sea un no. de empleado repetido
                    if (this.consentimientoFormViewModel.getByEmployeeNumber(noEmpleado.toString()) == null) {

                        //Mostrar loading
                        val dialogo = Dialogo(requireContext())
                        dialogo.mostrarCargando(getString(R.string.buscando_empleado))

                        //Realizar petición
                        consentimientoFormViewModel.buscarResponsable(noEmpleado)
                            .observe(viewLifecycleOwner, Observer {
                                if (it != null) {
                                        if (it.result != null && it.result.usuarioCore != null) {
                                            //Mostrar nombre obtenido
                                            /* consentimientoFormViewModel.addResponsableEstiba(
                                                 ResponsablesEstiba(
                                                     "AM" + it.result.usuarioCore.employeeNumber,
                                                     it.result.usuarioCore.name.toString() + " " + it.result.usuarioCore.apellidoPaterno.toString() + " " + it.result.usuarioCore.apellidoMaterno.toString()
                                                 )
                                             )*/

                                            dialogo.Ocultar()
                                            mBinding.root.hideKeyboard()
                                            this.mBinding.tilResponsableEstiba.editText?.text?.clear()
                                            this.mBinding.tilResponsableEstiba.clearFocus()
                                            if (this.mAdapter.itemCount == 6) {
                                                this.mBinding.tilResponsableEstiba.editText?.isEnabled =
                                                    false
                                            }

                                            this.mBinding.btnNuevoResponsableEstiba.isEnabled = true

                                        } else {
                                            dialogo.Ocultar()
                                            Snackbar.make(
                                                mBinding.root,
                                                "Ha ocurrido un error.",
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                            this.mBinding.btnNuevoResponsableEstiba.isEnabled = true
                                        }
                                } else {
                                    dialogo.Ocultar()
                                    Snackbar.make(
                                        mBinding.root,
                                        "Ha ocurrido un error.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    this.mBinding.btnNuevoResponsableEstiba.isEnabled = true
                                }
                            })
                    } else {
                        Snackbar.make(
                            mBinding.root,
                            "Este responsable de estiba ya se encuentra registrado.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        this.mBinding.btnNuevoResponsableEstiba.isEnabled = true
                    }
                } else {
                    Snackbar.make(
                        mBinding.root,
                        "Por favor ingresa un número de empleado válido.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    this.mBinding.btnNuevoResponsableEstiba.isEnabled = true
                }
            } else {
                Snackbar.make(
                    mBinding.root,
                    "Se permite un máximo de 6 responsables de estiba.",
                    Snackbar.LENGTH_SHORT
                ).show()
                this.mBinding.btnNuevoResponsableEstiba.isEnabled = true

            }
        }

        mBinding.btnNuevoResponsableEstibaExterno.setOnClickListener {
            var noEmpleado = mBinding.etNoEmpRespEstibaExterno.text.toString().trim()

            if (this.consentimientoFormViewModel.getByEmployeeNumber(noEmpleado.toString()) == null) {
               /* consentimientoFormViewModel.addResponsableEstiba(
                    ResponsablesEstiba(
                        mBinding.etNoEmpRespEstibaExterno.text.toString().toUpperCase(),
                        mBinding.etNombreRespEstibaExterno.text.toString().toUpperCase()
                    )
                )*/


                mBinding.etNoEmpRespEstibaExterno.setText("")
                mBinding.etNombreRespEstibaExterno.setText("")

            } else {
                Snackbar.make(
                    mBinding.root,
                    "Este número de empleado ya se encuentra registrado.",
                    Snackbar.LENGTH_SHORT
                ).show()
                this.mBinding.btnNuevoResponsableEstiba.isEnabled = true
            }

        }

        //Observador de viewmodel de firma
        signatureViewModel.base64.observe(viewLifecycleOwner, {
            this.procesarFirma(it)
        })


        //Finalizar
        this.mBinding.btnFinalizar.setOnClickListener {

            var dialogoConfirmar = Dialogo(requireContext())

            dialogoConfirmar.btnAceptar.setOnClickListener {

                dialogoConfirmar.Ocultar()
                this.createRequest()

                //Envío al API
                inspeccionAeronaveViewModel.createInsieccionAeronave();
                observersCreateInspecAeronave()

            }

            dialogoConfirmar.btnCerrar.setOnClickListener {
                when (accionBtnCerrar) {
                    "CERRAR" -> {
                        dialogoConfirmar.Ocultar()
                    }

                    "REDIRIGIR" -> {
                        dialogoConfirmar.btnCerrar.setText(getString(R.string.cerrar))
                        dialogoConfirmar.Ocultar()
                        reiniciarViewModels()
                        accionBtnCerrar = "CERRAR"
                        findNavController().popBackStack()
                        activity?.onBackPressed()
                    }
                }

            }


            if (this.validateConsentimiento()) {
                dialogoConfirmar.mostrarPregunta(
                    getString(R.string.confirmar),
                    getString(R.string.confirma_envio_inpec_aeronave)
                )
            }
        }

    }

    private fun observersCreateInspecAeronave() {
        val dialogo = Dialogo(requireContext())

        inspeccionAeronaveViewModel.responseState!!.observe(viewLifecycleOwner, {
            if (it.state == RequestState.REQ_IN_PROGRESS) {
                Log.i("INSPEC_AERONAVE", "Enviando request en proceso...")
                dialogo.mostrarCargando(getString(R.string.cargando))
            } else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK) {
                dialogo.Ocultar()
                Log.i("INSPEC_AERONAVE", "Se obtuvo una respuesta: " + it.state)
                if (it.state == RequestState.REQ_BAD) {

                    Log.i(
                        "INSPEC_AERONAVE",
                        "Ha ocurrido un error al crear el reporte, se agregará a bd local."
                    )

                    inspeccionAeronaveViewModel.addInspecAeronave()
                    mainViewModel.getInspecAeronaveCount()
                    dialogo.btnCerrar.setText(getString(R.string.aceptar))
                    accionBtnCerrar = "REDIRIGIR"


                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                        reiniciarViewModels()
                        findNavController().popBackStack()
                        activity?.onBackPressed()
                    }
                    dialogo.mostrarError(
                        getString(R.string.error_al_enviar_a_la_web),
                        getString(R.string.msg_error_inspec_aeronave)
                    )

                    val respJSON = Gson().toJson(it)
                    Log.i(
                        "INSPEC_AERONAVE",
                        "SE OBTUVO ESTATUS DE RESPUESTA CON ERROR: " + respJSON
                    )

                }
            }
        })
        inspeccionAeronaveViewModel.createInspecBaseResponse.observe(viewLifecycleOwner, {
            val respJSON = Gson().toJson(it)
            Log.i("INSPEC_AERONAVE", "Respuesta obtenida del servidor: " + respJSON)

            if (it.status == RequestState.REQ_OK) {

                var idInspeccion: Long = it.result.InspeccionAeronave
                Log.i("INSPEC_AERONAVE", "Id de inspección creado: " + idInspeccion)

                if (idInspeccion != null && idInspeccion != 0L) {
                    inspeccionAeronaveViewModel.setIdInspeccionCreada(idInspeccion)
                    inspeccionAeronaveViewModel.hasFinished.value = true
                    this.pagerViewModel.siguienteStep()
                }

            }
        })
    }

    //Lanzar signature pad y obtener firma
    private fun launchSignaturePad(firmaSeleccionada: String) {
        this.firmaSeleccionada = firmaSeleccionada
        findNavController().navigate(R.id.action_inspecionAeronaveMainFragment_to_signatureFragment2)
    }

    private fun procesarFirma(firmaB64: String) {
        when (this.firmaSeleccionada) {
            "oficial_operaciones" -> {
                this.consentimientoFormViewModel.setBase64OfOperaciones(firmaB64)
                this.mostrarFirmaOfOperaciones(firmaB64)
            }
            "tecnico_mantenimiento" -> {
                this.consentimientoFormViewModel.setBase64TecMantenimiento(firmaB64)
                this.mostrarFirmaTecMantenimiento(firmaB64)
            }
        }
    }

    //Modificar interfaz cuando se obtiene una firma
    private fun mostrarFirmaOfOperaciones(firmaB64: String) {
        val imageBytes = decode(firmaB64, DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        mBinding.btnFirmarOfOperaciones.visibility = View.VISIBLE
        mBinding.btnFirmarOfOperaciones.text = "Reintentar firma"
        mBinding.btnFirmarOfOperaciones.setIconResource(R.drawable.ic_redo)
        mBinding.ivOfOperaciones.visibility = View.VISIBLE
        mBinding.ivOfOperaciones.setImageBitmap(decodedImage)

    }

    private fun reiniciarFirmaOfOp() {
        consentimientoFormViewModel.setBase64OfOperaciones("")
        mBinding.ivOfOperaciones.visibility = View.GONE
        mBinding.btnFirmarOfOperaciones.text = getString(R.string.firmar)
        mBinding.btnFirmarOfOperaciones.setIconResource(R.drawable.ic_draw)
    }

    private fun mostrarFirmaTecMantenimiento(firmaB64: String) {
        val imageBytes = decode(firmaB64, DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        mBinding.btnFirmarTecMantenimiento.visibility = View.VISIBLE
        mBinding.btnFirmarTecMantenimiento.text = "Reintentar firma"
        mBinding.btnFirmarTecMantenimiento.setIconResource(R.drawable.ic_redo)
        mBinding.ivTecnicoMantenimiento.visibility = View.VISIBLE
        mBinding.ivTecnicoMantenimiento.setImageBitmap(decodedImage)
    }

    private fun reiniciarFirmaTecMant() {
        consentimientoFormViewModel.setBase64TecMantenimiento("")
        mBinding.ivTecnicoMantenimiento.visibility = View.GONE
        mBinding.btnFirmarTecMantenimiento.text = getString(R.string.firmar)
        mBinding.btnFirmarTecMantenimiento.setIconResource(R.drawable.ic_draw)
    }


    //Modificar interfaz cuando se obtiene un nombre de empleado desde CORE
    private fun mostrarNombreOfOperaciones(nombre: String) {
        this.mBinding.tvNombreOfOperaciones.text = nombre
        this.mBinding.tvNombreOfOperaciones.visibility = View.VISIBLE
        this.mBinding.btnFirmarOfOperaciones.visibility = View.VISIBLE

    }

    private fun mostrarNombretecMantenimiento(nombre: String) {
        this.mBinding.tvNombreTecMantenimiento.text = nombre
        this.mBinding.tvNombreTecMantenimiento.visibility = View.VISIBLE
        this.mBinding.btnFirmarTecMantenimiento.visibility = View.VISIBLE

    }

    //RecyclerView y responsables de estiba
    private fun setupRecyclerView() {
        //ESTO LO TENGO QUE MOVER DE AQUÍ
        mAdapter = ResponsableEstibaAdapter(arrayListOf(), this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerViewRespEstiba.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    override fun onClick(responsableEstiba: ResponsablesEstiba) {

       // this.consentimientoFormViewModel.deleteResponsableEstiba(responsableEstiba)

        if (mAdapter.itemCount < 6) {
            this.mBinding.tilResponsableEstiba.editText?.isEnabled = true
        }
    }

    private fun getResponsablesEstibaString(): String {
        var responsablesString = ""

        this.consentimientoFormViewModel.responsablesEstiba.value!!.forEach {
            val nombreCompleto = it.nombre
            responsablesString += "${it.noEmpleado}|$nombreCompleto,"
        }


        if (responsablesString.takeLast(1) == ",") {
            responsablesString = responsablesString.dropLast(1)
        }

        return responsablesString
    }

    //Submit y validaciones
    private fun validateConsentimiento(): Boolean {

        var correcto: Boolean = true

        if (this.consentimientoFormViewModel.nombre_oficial_operaciones.value == null || this.consentimientoFormViewModel.nombre_oficial_operaciones.value == "" ||
            this.consentimientoFormViewModel.b64_oficial_operaciones.value == null || this.consentimientoFormViewModel.b64_oficial_operaciones.value == ""
        ) {
            correcto = false
            this.mBinding.tvErrorOfOperaciones.visibility = View.VISIBLE
        } else {
            this.mBinding.tvErrorOfOperaciones.visibility = View.GONE
        }


        if (inspeccionFormViewModel.existenAverias()) {
            if (this.consentimientoFormViewModel.nombre_tecnico_mantenimientos.value == null || this.consentimientoFormViewModel.nombre_tecnico_mantenimientos.value == "" ||
                this.consentimientoFormViewModel.b64_tecnico_mantenimiento.value == null || this.consentimientoFormViewModel.b64_tecnico_mantenimiento.value == ""
            ) {
                correcto = false
                this.mBinding.tvErrorTecMantenimiento.visibility = View.VISIBLE
            } else {
                this.mBinding.tvErrorTecMantenimiento.visibility = View.GONE
            }
        }


        if(!inspeccionFormViewModel.getEsLlegada().value!!){
            if (this.consentimientoFormViewModel.responsablesEstiba.value == null || this.consentimientoFormViewModel.responsablesEstiba.value!!.size < 1) {
                correcto = false
                this.mBinding.tvErrorRespEstiba.visibility = View.VISIBLE
            } else {
                this.mBinding.tvErrorRespEstiba.visibility = View.GONE
            }
        }



        return correcto
    }

    private fun validaNoEmpleado(noEmpleado: String): Boolean {
        val regex = """^[\d]{4,6}${'$'}""".toRegex()
        return regex.matches(noEmpleado)
    }

    private fun createRequest() {

        Log.i("INSPEC_AERONAVE", "Creando RQ de inspección de aeronave...")

        //Datos generales del RQ
        Log.i("INSPEC_AERONAVE", "Llenando datos generales del vuelo")

        this.inspeccionAeronaveViewModel.request.value!!.GuidVuelo =
            this.inspeccionAeronaveViewModel.getGuidVuelo().value!!
        this.inspeccionAeronaveViewModel.request.value!!.FechaCreacion =
            Fecha().parser.format(Date()).toString()

        //Datos del vuelo
        this.inspeccionAeronaveViewModel.request.value!!.Origen =
            this.inspeccionAeronaveViewModel.getOrigen().value!!
        this.inspeccionAeronaveViewModel.request.value!!.Destino =
            this.inspeccionAeronaveViewModel.getDestino().value!!
        this.inspeccionAeronaveViewModel.request.value!!.NumVuelo =
            this.inspeccionAeronaveViewModel.getNoVuelo().value!!
        this.inspeccionAeronaveViewModel.request.value!!.FechaVuelo =
            this.inspeccionAeronaveViewModel.getFechaVuelo().value!!
        this.inspeccionAeronaveViewModel.request.value!!.Matricula =
            this.inspeccionAeronaveViewModel.getMatricula().value!!
        this.inspeccionAeronaveViewModel.request.value!!.Equipo =
            this.inspeccionAeronaveViewModel.getEquipo().value!!


        //Inspección (step 1)
        Log.i("INSPEC_AERONAVE", "Llenando lista de inspección")

        inspeccionAeronaveViewModel.request.value!!.Pernocta =
            inspeccionFormViewModel.getEsPernocta().value!!

        inspeccionAeronaveViewModel.request.value!!.EsLlegada =
            inspeccionFormViewModel.getEsLlegada().value!!

        if (this.inspeccionFormViewModel.radomo_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.radomo.name,
                    true,
                    this.inspeccionFormViewModel.radomo_descripcionAveria.value.toString(),
                    this.inspeccionFormViewModel.radomo_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.radomo.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.radomo_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.radomo_ImgB64.value else ""
                )
            )
        }


        if (this.inspeccionFormViewModel.compCargaDelantero_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_carga_delantero.name,
                    true,
                    this.inspeccionFormViewModel.compCargaDelantero_descripcionAveria.value,
                    this.inspeccionFormViewModel.compCargaDelantero_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_carga_delantero.name,
                    false,
                    "",
                    this.inspeccionFormViewModel.compCargaDelantero_ImgB64.value
                )
            )
        }

        if (this.inspeccionFormViewModel.compCargaTrasero_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_carga_trasero.name,
                    true,
                    this.inspeccionFormViewModel.compCargaTrasero_descripcionAveria.value,
                    this.inspeccionFormViewModel.compCargaTrasero_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_carga_trasero.name,
                    false,
                    "",
                    this.inspeccionFormViewModel.compCargaTrasero_ImgB64.value
                )
            )
        }

        if (inspeccionAeronaveViewModel.getEsWideBody().value!!) {
            if (this.inspeccionFormViewModel.compCargaBulk_tieneAveria.value!!) {
                this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                    AveriaInspeccionAeronaveEntity(
                        0,
                        Constants.TiposAveria.comp_carga_bulk.name,
                        true,
                        this.inspeccionFormViewModel.compCargaBulk_descripcionAveria.value,
                        this.inspeccionFormViewModel.compCargaBulk_ImgB64.value
                    )
                )
            } else {
                this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                    AveriaInspeccionAeronaveEntity(
                        0,
                        Constants.TiposAveria.comp_carga_bulk.name,
                        false,
                        "",
                        this.inspeccionFormViewModel.compCargaBulk_ImgB64.value
                    )
                )
            }
        }

        if (this.inspeccionFormViewModel.compIntDelantero_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_int_delantero.name,
                    true,
                    this.inspeccionFormViewModel.compIntDelantero_descripcionAveria.value,
                    this.inspeccionFormViewModel.compIntDelantero_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_int_delantero.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.compIntDelantero_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.compIntDelantero_ImgB64.value else ""
                )
            )
        }

        if (this.inspeccionFormViewModel.compIntTrasero_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_int_trasero.name,
                    true,
                    this.inspeccionFormViewModel.compIntTrasero_descripcionAveria.value,
                    this.inspeccionFormViewModel.compIntTrasero_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.comp_int_trasero.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.compIntTrasero_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.compIntTrasero_ImgB64.value else ""

                )
            )
        }

        if (inspeccionAeronaveViewModel.getEsWideBody().value!!) {
            if (this.inspeccionFormViewModel.compIntBulk_tieneAveria.value!!) {
                this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                    AveriaInspeccionAeronaveEntity(
                        0,
                        Constants.TiposAveria.comp_int_bulk.name,
                        true,
                        this.inspeccionFormViewModel.compIntBulk_descripcionAveria.value,
                        this.inspeccionFormViewModel.compIntBulk_ImgB64.value
                    )
                )
            } else {
                this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                    AveriaInspeccionAeronaveEntity(
                        0,
                        Constants.TiposAveria.comp_int_bulk.name,
                        false,
                        "",
                        if (!inspeccionFormViewModel.compIntBulk_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.compIntBulk_ImgB64.value else ""
                    )
                )
            }
        }

        if (this.inspeccionFormViewModel.semialaIzq_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.semiala_izq.name,
                    true,
                    this.inspeccionFormViewModel.semialaIzq_descripcionAveria.value,
                    this.inspeccionFormViewModel.semialaIzq_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.semiala_izq.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.semialaIzq_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.semialaIzq_ImgB64.value else ""
                )
            )
        }

        if (this.inspeccionFormViewModel.semialaDer_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.semiala_der.name,
                    true,
                    this.inspeccionFormViewModel.semialaDer_descripcionAveria.value,
                    this.inspeccionFormViewModel.semialaDer_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.semiala_der.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.semialaDer_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.semialaDer_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.aguaPotable_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.agua_potable.name,
                    true,
                    this.inspeccionFormViewModel.aguaPotable_descripcionAveria.value,
                    this.inspeccionFormViewModel.aguaPotable_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.agua_potable.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.aguaPotable_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.aguaPotable_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.aguaNegra_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.agua_negra.name,
                    true,
                    this.inspeccionFormViewModel.aguaNegra_descripcionAveria.value,
                    this.inspeccionFormViewModel.aguaNegra_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.agua_negra.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.aguaNegra_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.aguaNegra_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.puertaPrincipal_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.puerta_principal.name,
                    true,
                    this.inspeccionFormViewModel.puertaPrincipal_descripcionAveria.value,
                    this.inspeccionFormViewModel.puertaPrincipal_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.puerta_principal.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.puertaPrincipal_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.puertaPrincipal_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.servicioTrasera_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.puerta_trasera.name,
                    true,
                    this.inspeccionFormViewModel.servicioTrasera_descripcionAveria.value,
                    this.inspeccionFormViewModel.servicioTrasera_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.puerta_trasera.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.servicioTrasera_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.servicioTrasera_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.sepDosIn_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.sep_dos_pulg.name,
                    true,
                    this.inspeccionFormViewModel.sepDosIn_descripcionAveria.value,
                    this.inspeccionFormViewModel.sepDosIn_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.sep_dos_pulg.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.sepDosIn_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.sepDosIn_ImgB64.value else ""
                )
            )
        }

        if (this.inspeccionFormViewModel.colocacionRedes_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.colocacion_redes.name,
                    true,
                    this.inspeccionFormViewModel.colocacionRedes_descripcionAveria.value,
                    this.inspeccionFormViewModel.colocacionRedes_ImgB64.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.colocacion_redes.name,
                    false,
                    "",
                    if (!inspeccionFormViewModel.colocacionRedes_ImgB64.value.isNullOrEmpty()) inspeccionFormViewModel.colocacionRedes_ImgB64.value else ""

                )
            )
        }

        if (this.inspeccionFormViewModel.otros_tieneAveria.value!!) {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.otro.name,
                    true,
                    this.inspeccionFormViewModel.otros_nombre.value + ": " + this.inspeccionFormViewModel.otros_descripcionAveria.value,
                    "",
                    0,
                    inspeccionFormViewModel.otros_imagenes.value
                )
            )
        } else {
            this.inspeccionAeronaveViewModel.request.value!!.Averias.add(
                AveriaInspeccionAeronaveEntity(
                    0,
                    Constants.TiposAveria.otro.name,
                    false,
                    "",
                    "",
                    0,
                    inspeccionFormViewModel.otros_imagenes.value

                )
            )
        }

        //Consentimiento (step 2)
        Log.i(
            "INSPEC_AERONAVE",
            "Llenando datos de oficial de op, técnico mantenimiento y responsables estiba."
        )

        this.inspeccionAeronaveViewModel.request.value!!.NoEmpleadoOficialOperaciones =
            this.consentimientoFormViewModel.getNoEmpOfOperaciones().value.toString()
        if (!consentimientoFormViewModel.getOfOpEsExterno().value!!) {
            inspeccionAeronaveViewModel.request.value!!.NoEmpleadoOficialOperaciones =
                "AM" + inspeccionAeronaveViewModel.request.value!!.NoEmpleadoOficialOperaciones
        }
        this.inspeccionAeronaveViewModel.request.value!!.NombreOficialOperaciones =
            this.consentimientoFormViewModel.getNombreOfOperaciones().value!!
        this.inspeccionAeronaveViewModel.request.value!!.FirmaB64OficialOperaciones =
            this.consentimientoFormViewModel.getBase64OfOperaciones().value!!

        if (inspeccionFormViewModel.existenAverias()) {
            this.inspeccionAeronaveViewModel.request.value!!.NoEmpleadoTecnicoMantenimiento =
                this.consentimientoFormViewModel.getNoEmpTecMantenimiento().value.toString()
            if (!consentimientoFormViewModel.getTecManEsExterno().value!!) {
                inspeccionAeronaveViewModel.request.value!!.NoEmpleadoTecnicoMantenimiento =
                    "AM" + inspeccionAeronaveViewModel.request.value!!.NoEmpleadoTecnicoMantenimiento
            }
            this.inspeccionAeronaveViewModel.request.value!!.NombreTecnicoMantenimiento =
                this.consentimientoFormViewModel.getNombreTecMantenimiento().value!!
            this.inspeccionAeronaveViewModel.request.value!!.FirmaB64TecnicoMantenimiento =
                this.consentimientoFormViewModel.getBase64TecMantenimiento().value!!

        } else {
            this.inspeccionAeronaveViewModel.request.value!!.NoEmpleadoTecnicoMantenimiento = ""
            this.inspeccionAeronaveViewModel.request.value!!.NombreTecnicoMantenimiento = ""
            this.inspeccionAeronaveViewModel.request.value!!.FirmaB64TecnicoMantenimiento = ""
        }


        this.inspeccionAeronaveViewModel.request.value!!.ResponsablesEstiba =
            this.getResponsablesEstibaString()


        val a = Gson().toJson(this.inspeccionAeronaveViewModel.request.value)
        Log.i("INSPEC_AERONAVE", "Request creado exitosamente: " + a.toString())


    }

    private fun reiniciarViewModels() {
        inspeccionAeronaveViewModel.reiniciarVM()
        inspeccionFormViewModel.reiniciarVM()
        consentimientoFormViewModel.reiniciarVM()
        pagerViewModel.reiniciarVM()
    }
}