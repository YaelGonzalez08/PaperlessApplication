package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInpeccionConcentimientoBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemDatosEmpleadoFirmaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.ResponsableEstibaAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.ResponsableEstibaClickListener
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponsablesEstiba
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import ng.softcom.android.utils.ui.showToast
import okhttp3.internal.filterList
import java.util.*
import kotlin.collections.ArrayList

class InpeccionConcentimientoFragment(
    var getInspeccionLocal: () -> InspeccionAeronave?
) : Fragment() {
    lateinit var binding: FragmentInpeccionConcentimientoBinding
    lateinit var model: InspeccionAeronaveViewModel
    var responasblesEstibaList = arrayListOf<ResponsablesEstiba>()
    private lateinit var userLogginData: Usuario
    private  var errorResponse = ""

    var inspeccion: InspeccionAeronave? = null
    var isTecnicoObligatoprio = false
    var isResponEstibaObligatorio = false

    lateinit var adapter: ResponsableEstibaAdapter
    override fun onResume() {
        super.onResume()
        inspeccion = getInspeccionLocal.invoke()
        //aqio falta la carga de datos si ya estaban guardados
        //    showToast(inspeccion.toString())
        if (inspeccion != null) {
            var it = inspeccion
            if (it?.esLlegada == false) {
                binding.contenedorRespEstiba.visibility = View.VISIBLE
                isResponEstibaObligatorio = true
            }else{
                binding.contenedorRespEstiba.visibility = View.GONE
                isResponEstibaObligatorio = false
            }
            val listAvriasExeptionCodes = arrayListOf<String>("sep_dos_pulg","colocacion_redes","ldm","segre_prio_cnx_turi","avih","gpu_cmb_jet","bypass")

            val listDanios = it?.averias?.filter { averia -> averia.tieneAveria == true && !listAvriasExeptionCodes.contains(averia.Codigo)}
            if (!listDanios.isNullOrEmpty()) {
                binding.datosTenico.root.visibility = View.VISIBLE
                isTecnicoObligatoprio = true
            }else{
                binding.datosTenico.root.visibility = View.GONE
                isTecnicoObligatoprio = false
            }
            if (!it?.responsablesEstiba.isNullOrEmpty()) {
                responasblesEstibaList = it?.responsablesEstiba!!
                adapter.setList(responasblesEstibaList)
            }
            binding.apply {
                if (!it?.numEmpleado_OficialOperaciones.isNullOrEmpty()) {
                    datosOficial.txtNumEmpleado.setText(it?.numEmpleado_OficialOperaciones)
                }
                if (!it?.nombre_OficialOperaciones.isNullOrEmpty()) {
                    datosOficial.tilNombreEmpleadoManual.visibility = View.VISIBLE
                    datosOficial.actvOficialNombre.setText(it?.nombre_OficialOperaciones)
                    it?.firmaB64OficialOperaciones = "firma precargada"
                    btnFinalizar.visibility = View.GONE
                }

                if (!it?.numEmpleado_TecnicoMantenimiento.isNullOrEmpty()) {
                    datosTenico.txtNumEmpleado.setText(it?.numEmpleado_TecnicoMantenimiento)
                }
                if (!it?.nombre_TecnicoMantenimiento.isNullOrEmpty()) {
                    datosTenico.tilNombreEmpleadoManual.visibility = View.VISIBLE
                    datosTenico.actvOficialNombre.setText(it?.nombre_TecnicoMantenimiento)
                    it?.firmaB64TecnicoMantenimiento = "firma precargada"
                }

            }


        }
        model.responseError.observe(viewLifecycleOwner, Observer {
            it.let {
                errorResponse = it.toString()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInpeccionConcentimientoBinding.inflate(layoutInflater)
        model = InspeccionAeronaveViewModel()
        binding.apply {
            btnFinalizar.setOnClickListener {
                sendForm()
            }
            adapter = ResponsableEstibaAdapter(responasblesEstibaList, object :
                ResponsableEstibaClickListener {
                override fun onClick(responableEstibaEntity: ResponsablesEstiba) {
                    responasblesEstibaList.remove(responableEstibaEntity)
                    adapter.setList(responasblesEstibaList)
                }

            })
            recyclerViewRespEstiba.adapter = adapter

            btnNuevoResponsableEstibaExterno.setOnClickListener {
                if (!binding.datosResponsableestiba.txtNumEmpleado.text.toString()
                        .isNullOrEmpty() && !binding.datosResponsableestiba.actvOficialNombre.text.toString()
                        .isNullOrEmpty()
                ) {
                    var list = responasblesEstibaList.filterList {
                        noEmpleado == binding.datosResponsableestiba.txtNumEmpleado.text.toString()
                                || nombre == binding.datosResponsableestiba.actvOficialNombre.text.toString()
                    }

                    if (list.isEmpty()) {
                        if (responasblesEstibaList.count() < 6) {
                            //  responasblesEstibaList.add("${binding.datosResponsableestiba.txtNumEmpleado.text} | ${binding.datosResponsableestiba.actvOficialNombre.text}")
                            responasblesEstibaList.add(
                                ResponsablesEstiba(
                                    binding.datosResponsableestiba.txtNumEmpleado.text.toString(),
                                    binding.datosResponsableestiba.actvOficialNombre.text.toString()
                                )
                            )

                            adapter.setList(responasblesEstibaList)
                            binding.datosResponsableestiba.txtNumEmpleado.setText("")
                            binding.datosResponsableestiba.actvOficialNombre.setText("")
                        } else {
                            showToast("Solo puedes agregar 6 responsables de estiba")
                        }

                    } else
                        showToast("Responsable de estiba repetido")

                } else {
                    showToast("Introduce un Responsable de estiba valido")
                }
            }
        }

        binding.datosResponsableestiba.apply {
            txtTitulo.setText("Responsables de estiba")
            btnConsultarEmpleado.setOnClickListener {
                getRespEstiba()
            }
            swEmpExterno.setOnClickListener {
                if (swEmpExterno.isChecked)
                    showNoAeromexicodatosdatosResponsableestiba(binding.datosResponsableestiba)
                else
                    showAeromexicodatosEmpleado(binding.datosResponsableestiba)
            }
            showAeromexicodatosEmpleado(binding.datosResponsableestiba)
        }

        binding.datosTenico.apply {
            txtTitulo.setText("Técnico de mantenimiento")
            btnConsultarEmpleado.setOnClickListener {
                getTecnico()
            }
            btnFirma.setOnClickListener {
                parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirma.visibility = View.VISIBLE
                        ivFirma.setImageBitmap(bitmap)
                        inspeccion?.firmaB64TecnicoMantenimiento =
                            CreateImageFile.getB64FromBitmap(bitmap)
                    }.show(it, "SignatureFragment")
                }
            }
            swEmpExterno.setOnClickListener {
                if (swEmpExterno.isChecked)
                    showNoAeromexicodatosOficial(binding.datosTenico)
                else
                    showAeromexicodatosEmpleado(binding.datosTenico)
            }
            showAeromexicodatosEmpleado(binding.datosTenico)
        }

        binding.datosOficial.apply {
            btnConsultarEmpleado.setOnClickListener {
                getOficial()
            }
            btnFirma.setOnClickListener {
                parentFragmentManager.let {
                    DialogFragmentSignature() { bitmap ->
                        ivFirma.visibility = View.VISIBLE
                        ivFirma.setImageBitmap(bitmap)
                        inspeccion?.firmaB64OficialOperaciones =
                            CreateImageFile.getB64FromBitmap(bitmap)
                    }.show(it, "SignatureFragment")
                }
            }
            swEmpExterno.setOnClickListener {
                if (swEmpExterno.isChecked)
                    showNoAeromexicodatosOficial(binding.datosOficial)
                else
                    showAeromexicodatosEmpleado(binding.datosOficial)
            }
            showAeromexicodatosEmpleado(binding.datosOficial)
        }
        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            userLogginData = userdata
        }

        return binding.root
    }

    private fun sendForm() {
        var todoBien = true
        binding.apply {
            datosOficial.apply {
                //validaciones oficial
                if (txtNumEmpleado.text.toString()
                        .isNullOrEmpty() || actvOficialNombre.text.toString()
                        .isNullOrEmpty() || inspeccion?.firmaB64OficialOperaciones.isNullOrEmpty()
                ) {
                    showToast("Oficial de operaciones incompleto")
                    todoBien = false
                } else {
                    inspeccion?.numEmpleado_OficialOperaciones = txtNumEmpleado.text.toString()
                    inspeccion?.nombre_OficialOperaciones = actvOficialNombre.text.toString()
                }

            }
            if (isTecnicoObligatoprio) {
                datosTenico.apply {
                    //validaciones tecnico
                    if (txtNumEmpleado.text.toString()
                            .isNullOrEmpty() || actvOficialNombre.text.toString()
                            .isNullOrEmpty() || inspeccion?.firmaB64TecnicoMantenimiento.isNullOrEmpty()
                    ) {
                        showToast("Tecnico incompleto")
                        todoBien = false
                    } else {
                        inspeccion?.numEmpleado_TecnicoMantenimiento =
                            txtNumEmpleado.text.toString()
                        inspeccion?.nombre_TecnicoMantenimiento = actvOficialNombre.text.toString()
                    }
                }
            }
            //validaciones estiba
            if (isResponEstibaObligatorio) {
                if (responasblesEstibaList.isNullOrEmpty()) {
                    showToast("Ingresa al menos un responsable de estiba")
                    todoBien = false
                } else {
                    inspeccion?.responsablesEstiba = responasblesEstibaList
                }
            }
            if (todoBien) {
                saveForm()
            }
        }
    }

    private fun saveForm() {

        inspeccion?.modificadoPor = userLogginData.userGuid
        inspeccion?.fechaModificacion = Fecha().parser.format(Date()).toString()
        inspeccion?.enviadoPor = userLogginData.userGuid

        var dialogo = Dialogo(requireContext())
        dialogo.mostrarCargando(null)
        model.saveInspeccion(inspeccion!!).observe(viewLifecycleOwner, Observer {
            dialogo.Ocultar()
            //showToast(it.toString())
            if (it != null) {
                if (!it.error) {
                    model.deleteByguid(inspeccion?.guidVuelo.toString(),inspeccion?.esLlegada!!)
                    dialogo.mostrarMensajeConfirmacion(
                        "Formato enviado",
                        "Se envio con exito con el folio${it?.result?.InspeccionAeronave.toString()}"
                    )
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                        activity?.onBackPressed()
                    }
                    dialogo.btnAceptar.setOnClickListener {
                        dialogo.Ocultar()
                        activity?.onBackPressed()
                    }
                } else {
                    dialogo.mostrarError("Error!", it.errorMessage)
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                }
            } else {
                // guardado local
                model.saveLocal(inspeccion!!)
                dialogo.mostrarAviso(  "Error ","${errorResponse?:""}\nEste formato se guardo de manera local")

                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                    activity?.onBackPressed()
                }
                dialogo.btnAceptar.setOnClickListener {
                    dialogo.Ocultar()
                    activity?.onBackPressed()
                }

            }

        })

    }

    private fun getRespEstiba() {
        var d = Dialogo(requireContext())
        binding.datosResponsableestiba.apply {
            if (!txtNumEmpleado.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getEmpleado("${txtNumEmpleado.text.toString()}").observeOnce(Observer {
                    d.Ocultar()
                    if (it != null) {
                        it.result.usuarioCore.let { employe ->
                            tilNombreEmpleadoManual.visibility = View.VISIBLE
                            tilNumEmpleado.visibility = View.VISIBLE
                            txtNumEmpleado.setText("${employe.employeeNumber}")
                            actvOficialNombre.setText("${employe?.name} ${employe?.apellidoPaterno} ${employe?.apellidoMaterno}")
                            btnFirma.visibility = View.GONE
                        }
                    } else {
                        showToast("Error al obtener empleado")
                    }
                })
            } else {
                txtNumEmpleado.error = getString(R.string.error_campo_invalido)
            }
        }
    }


    private fun showNoAeromexicodatosdatosResponsableestiba(it: ItemDatosEmpleadoFirmaBinding) {
        it.apply {

            tilNombreEmpleadoManual.visibility = View.VISIBLE
            tilNombreEmpleadoManual.isErrorEnabled = false
            btnFirma.visibility = View.GONE

            actvOficialNombre.setText("")
            txtNumEmpleado.setText("")

            btnConsultarEmpleado.visibility = View.GONE

            actvOficialNombre.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumEmpleado.prefixTextView.apply {
                text = ""
            }
        }
    }

    private fun getTecnico() {
        var d = Dialogo(requireContext())
        binding.datosTenico.apply {
            if (!txtNumEmpleado.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getEmpleado("${txtNumEmpleado.text.toString()}").observeOnce(Observer {
                    d.Ocultar()
                    if (it != null) {
                        it.result.usuarioCore.let { employe ->
                            tilNombreEmpleadoManual.visibility = View.VISIBLE
                            tilNumEmpleado.visibility = View.VISIBLE
                            txtNumEmpleado.setText("${employe.employeeNumber}")
                            actvOficialNombre.setText("${employe?.name} ${employe?.apellidoPaterno} ${employe?.apellidoMaterno}")
                            btnFirma.visibility = View.VISIBLE
                        }
                    } else {
                        showToast("Error al obtener empleado")
                    }
                })
            } else {
                txtNumEmpleado.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    private fun getOficial() {
        var d = Dialogo(requireContext())
        binding.datosOficial.apply {
            if (!txtNumEmpleado.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getEmpleado("${txtNumEmpleado.text.toString()}").observeOnce(Observer {
                    d.Ocultar()
                    if (it != null) {
                        it.result.usuarioCore.let { employe ->
                            tilNombreEmpleadoManual.visibility = View.VISIBLE
                            tilNumEmpleado.visibility = View.VISIBLE
                            txtNumEmpleado.setText("${employe.employeeNumber}")
                            actvOficialNombre.setText("${employe?.name} ${employe?.apellidoPaterno} ${employe?.apellidoMaterno}")
                            btnFirma.visibility = View.VISIBLE
                        }
                    } else {
                        showToast("Error al obtener empleado")
                    }
                })
            } else {
                txtNumEmpleado.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    fun showAeromexicodatosEmpleado(it: ItemDatosEmpleadoFirmaBinding) {
        it.apply {
            btnFirma.visibility = View.GONE
            ivFirma.visibility = View.GONE
            tilNombreEmpleadoManual.visibility = View.GONE
            tilNombreEmpleadoManual.isErrorEnabled = false
            actvOficialNombre.setText("")
            txtNumEmpleado.setText("")

            btnConsultarEmpleado.visibility = View.VISIBLE

            actvOficialNombre.apply {
                isEnabled = false
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumEmpleado.prefixTextView.apply {
                text = "AM"
            }
        }
    }

    fun showNoAeromexicodatosOficial(it: ItemDatosEmpleadoFirmaBinding) {
        it.apply {

            tilNombreEmpleadoManual.visibility = View.VISIBLE
            tilNombreEmpleadoManual.isErrorEnabled = false
            btnFirma.visibility = View.VISIBLE

            actvOficialNombre.setText("")
            txtNumEmpleado.setText("")

            btnConsultarEmpleado.visibility = View.GONE

            actvOficialNombre.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumEmpleado.prefixTextView.apply {
                text = ""
            }
        }
    }

}