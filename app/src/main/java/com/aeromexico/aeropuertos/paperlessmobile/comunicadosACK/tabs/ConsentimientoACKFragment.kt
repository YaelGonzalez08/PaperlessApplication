package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs

import DetalleComunicado
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.comunicadosViewModel
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository.ComunicadosRepository
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentConsentimientoACKBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableButtonEditText
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast

class ConsentimientoACKFragment(
    var detalleComunicado: DetalleComunicado,
    var request: (item: RequestEnvioCuesitonario) -> Unit
) : Fragment() {
    lateinit var biding: FragmentConsentimientoACKBinding
    lateinit var model: comunicadosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        biding = FragmentConsentimientoACKBinding.inflate(layoutInflater)
        model = comunicadosViewModel(
            ComunicadosRepository(WebServiceApi().getComuncadisApi()),
            CoreRepository(WebServiceApi().getCoreApi())
        )
        biding.apply {
            mbEnviar.setOnClickListener {
                if (valdiateCredentials()) {
                    // Toast.makeText(requireContext(), "Todo bien pasamos a enviarlo", Toast.LENGTH_SHORT).show()
                    enviarDatos()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Faltan campos obligatortios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            swEmpExterno.setOnClickListener {
                if (swEmpExterno.isChecked)
                    showNoAeromexicodatosEmpleado()
                else
                    showAeromexicodatosEmpleado()
            }
            showAeromexicodatosEmpleado()

            unableButtonEditText(checkBoxAceppt.isChecked, mbEnviar)
            checkBoxAceppt.setOnClickListener {
                unableButtonEditText(checkBoxAceppt.isChecked, mbEnviar)
            }
            btnConsultarEmpleado.setOnClickListener {
                getEmpleado()
            }
        }


        return biding.root
    }

    private fun getEmpleado() {
        var d = Dialogo(requireContext())
        biding.apply {
            if (!txtNumEmpleado.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                model.getEmpleado("${txtNumEmpleado.text.toString()}").observeOnce(Observer {
                    d.Ocultar()
                    if(it != null) {
                        it.result.usuarioCore.let { employe->
                            tilNombreEmpleadoManual.visibility = View.VISIBLE
                            tilNumEmpleado.visibility = View.VISIBLE

                            txtNumEmpleado.setText("${employe.employeeNumber}")
                            actvOficialNombre.setText("${employe?.name} ${employe?.apellidoPaterno} ${employe?.apellidoMaterno}")
                            txtPuesto.setText("${employe.puesto}")

                        }

                    }else{
                        showToast("Error al obtener empleado")
                    }
                })
            } else {
                txtNumEmpleado.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    private fun enviarDatos() {
        var rq = RequestEnvioCuesitonario(
            puesto = biding.txtPuesto.text.toString().trim(),
            correo = biding.txtemail.text.toString().trim(),
            noEmpleado = biding.txtNumEmpleado.text.toString().trim(),
            nombre = biding.actvOficialNombre.text.toString().trim()
        )
        request.invoke(rq)
    }

    private fun valdiateCredentials(): Boolean {
        biding.apply {
            if (detalleComunicado.solicitarNumEmpleado) {
                if (txtNumEmpleado.text.toString().trim().isNullOrEmpty()) {
                    txtNumEmpleado.error = resources.getString(R.string.error_campo_invalido)
                    return false
                }
            }
            if (detalleComunicado.solicitarNombre) {
                if (actvOficialNombre.text.toString().trim().isNullOrEmpty()) {
                    actvOficialNombre.error = resources.getString(R.string.error_campo_invalido)
                    return false
                }
            }
            if (detalleComunicado.solicitarPuesto) {
                if (txtPuesto.text.toString().trim().isNullOrEmpty()) {
                    txtPuesto.error = resources.getString(R.string.error_campo_invalido)
                    return false
                }
            }
            if (detalleComunicado.solicitarCorreo) {
                if (txtemail.text.toString().trim().isNullOrEmpty()) {
                    txtemail.error = resources.getString(R.string.error_campo_invalido)
                    return false
                }
            }

        }
        return true
    }

    private fun showAeromexicodatosEmpleado() {
        biding.apply {

            tilNombreEmpleadoManual.visibility = View.GONE
            tilNombreEmpleadoManual.isErrorEnabled = false
            actvOficialNombre.setText("")
            txtNumEmpleado.setText("")
            tilPuesto.visibility = View.GONE
            tilPuesto.isErrorEnabled = false
            txtPuesto.setText("")

            btnConsultarEmpleado.visibility = View.VISIBLE
            txtPuesto.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            actvOficialNombre.apply {
                isEnabled = false
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumEmpleado.prefixTextView.apply {
                text = "AM"
            }
            if (detalleComunicado.solicitarCorreo) {
                tilemail.visibility = View.VISIBLE
            }

            if (detalleComunicado.solicitarPuesto) {
                tilPuesto.visibility = View.VISIBLE
            }

        }
    }


    private fun showNoAeromexicodatosEmpleado() {
        biding.apply {

            tilNombreEmpleadoManual.visibility = View.VISIBLE
            tilNombreEmpleadoManual.isErrorEnabled = false
            tilPuesto.visibility = View.VISIBLE
            tilPuesto.isErrorEnabled = false
            actvOficialNombre.setText("")
            txtNumEmpleado.setText("")
            txtPuesto.setText("")
            btnConsultarEmpleado.visibility = View.GONE

            actvOficialNombre.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            txtPuesto.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black))
            }
            tilNumEmpleado.prefixTextView.apply {
                text = ""
            }
            if (detalleComunicado.solicitarCorreo) {
                tilemail.visibility = View.VISIBLE
            }
            if (detalleComunicado.solicitarNombre) {
                tilNombreEmpleadoManual.visibility = View.VISIBLE
            }
            if (detalleComunicado.solicitarNumEmpleado) {
                tilNumEmpleado.visibility = View.VISIBLE
            }
            if (detalleComunicado.solicitarPuesto) {
                tilPuesto.visibility = View.VISIBLE
            }

        }
    }


}