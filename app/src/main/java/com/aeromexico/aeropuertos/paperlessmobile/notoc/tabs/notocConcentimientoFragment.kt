package com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentNotocConcentimientoBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableSignatureImageView
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Capitan
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Consentimiento
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Oficial
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.CoreApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class notocConcentimientoFragment : Fragment() {
    lateinit var binding: FragmentNotocConcentimientoBinding
    //lateinit var model: NotocViewModel
    lateinit var d: Dialogo
    private  val viewModel: NotocViewModel by activityViewModels()
    var capitan: UsuarioCore? = null
    var oficinalOperaciones: UsuarioCore? = null
    var firmaCapitan: String? = null
    var firmaoficinalOperaciones: String? = null
    private lateinit var notoc: RequestNotoc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotocConcentimientoBinding.inflate(layoutInflater)
        //model = NotocViewModel(CoreRepository(WebServiceApi().getCoreApi()))
        d = Dialogo(requireContext())
        setOberversViewModel()

        binding.apply {
            btnBuscarEmpleadoCapitan.setOnClickListener {
                it.hideKeyboard()
                getCapitan()
            }
            btnBuscarEmpleadoOficialOperaciones.setOnClickListener {
                it.hideKeyboard()
                getOficial()
            }
            btnFirmarOfCapitan.setOnClickListener {
                firmaCapitan()
            }
            firmaCapitan.setOnClickListener {
                firmaCapitan()
            }
            btnFirmarOFOperacion.setOnClickListener {
                firmaOfOperaciones()
            }
            firmaOFOperacion.setOnClickListener {
                firmaOfOperaciones()
            }
            setSwitchExterno()
            setEmpleadoManualListeners()

        }
        return binding.root
    }
    private fun setEmpleadoManualListeners(){
        binding.textOficialOperaciones.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.notoc.consentimiento.oficial.nombre  = binding.textOficialOperaciones.text.toString().uppercase()
            }
        })
        binding.editNumOficial.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /*binding.textOficialOperaciones.setText("")
                viewModel.notoc.consentimiento.oficial.nombre = ""*/
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Toast.makeText(context,"Texto de numero de oficial",Toast.LENGTH_SHORT).show()

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.notoc.consentimiento.oficial.numEmpleado =
                    if (binding.swEmpExternoOfi.isChecked)
                        binding.editNumOficial.text.toString()
                    else{
                        if (!binding.editNumOficial.text?.contains("AM")!!){
                            "AM" + binding.editNumOficial.text.toString()
                        }
                        else{
                            binding.editNumOficial.text.toString()
                        }

                    }
            }
        })

        binding.editNumCapitan.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.notoc.consentimiento.capitan.numEmpleado =
                    if(!binding.editNumCapitan.text.toString().contains("AM")){
                        "AM" + binding.editNumCapitan.text.toString()
                    }
                    else{
                        binding.editNumCapitan.text.toString()
                    }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
    private fun setOberversViewModel(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            notoc=it
            Log.i("Consentimiento",notoc.consentimiento.toString())
            if (notoc.consentimiento != null) {
                if (!it.editable)
                    setInfoConsentimiento(notoc.consentimiento)

            }
        }
    }
    private fun setSwitchExterno(){
        binding.swEmpExternoOfi.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked)
                showNoAeromexicoOficial()
            else
                showAeromexicoOficial()
        }
    }
    private fun setInfoConsentimiento(consentimiento: Consentimiento){
        setInfoCapitan(consentimiento.capitan)
        setInfooficial(consentimiento.oficial)
    }
    private fun setInfoCapitan(capitan: Capitan){
        binding.apply {
            if (!capitan.numEmpleado.isNullOrEmpty()) {
                btnBuscarEmpleadoCapitan.isEnabled = false
                editNumCapitan.setText(capitan.numEmpleado)
                editNumCapitan.isEnabled = false
            }
            Log.i("Capitan","${capitan.numEmpleado} ${capitan.nombre}")
            if (!capitan.nombre.isNullOrEmpty()) {
                textNombreCapitan.setText(capitan.nombre)
                textNombreCapitan.isEnabled = false
                tiltextNombreCapitan.visibility = View.VISIBLE
            }
            if (!capitan.firmaB64.isNullOrEmpty()) {
                firmaCapitan.apply {
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(capitan.firmaB64))
                }
                firmaCapitan.visibility = View.VISIBLE
                firmaCapitan.isEnabled=false
            }
        }
    }
    private fun setInfooficial(oficial: Oficial){
        binding.apply {
            if (!oficial.numEmpleado.isNullOrEmpty()) {

                if(oficial.numEmpleado.contains("AM"))
                    swEmpExternoOfi.isChecked = false
                else
                    swEmpExternoOfi.isChecked = true

                swEmpExternoOfi.isEnabled=false
                btnBuscarEmpleadoOficialOperaciones.isEnabled = false
                editNumOficial.setText(oficial.numEmpleado)
                editNumOficial.isEnabled = false
            }
            Log.i("oficial","${oficial.numEmpleado} ${oficial.nombre}")
            if (!oficial.nombre.isNullOrEmpty()) {
                textOficialOperaciones.setText(oficial.nombre)
                textOficialOperaciones.isEnabled = false
                tiltextOficialOperaciones.visibility = View.VISIBLE
            }
            if (!oficial.firmaB64.isNullOrEmpty()) {
                firmaOFOperacion.apply {
                    setImageBitmap(CreateImageFile.setBitmapFromB64String(oficial.firmaB64))
                }
                firmaOFOperacion.visibility = View.VISIBLE
                firmaOFOperacion.isEnabled = false
            }
        }
    }
    private fun showNoAeromexicoOficial() {
        binding.apply {
            tiltextOficialOperaciones.visibility = View.VISIBLE
            tiltextOficialOperaciones.isEnabled = true
            tilNumOficial.prefixText = ""
            editNumOficial.setText("")
            btnFirmarOFOperacion.visibility = View.VISIBLE
            tiltextOficialOperaciones.visibility = View.VISIBLE
            firmaOFOperacion.visibility = View.VISIBLE
            btnBuscarEmpleadoOficialOperaciones.visibility = View.GONE
        }
    }
    private fun showAeromexicoOficial() {
        binding.apply {
            tiltextOficialOperaciones.visibility = View.GONE
            tiltextOficialOperaciones.isEnabled = false
            tilNumOficial.prefixText = "AM"
            editNumOficial.setText("")
            btnFirmarOFOperacion.visibility = View.GONE
            tiltextOficialOperaciones.visibility = View.GONE
            firmaOFOperacion.visibility = View.GONE
            btnBuscarEmpleadoOficialOperaciones.visibility = View.VISIBLE
        }
    }

    private fun firmaCapitan(){
        parentFragmentManager.let {
            DialogFragmentSignature() { bitmap ->
                binding.firmaCapitan.setImageBitmap(bitmap)
                firmaCapitan = CreateImageFile.getB64FromBitmap(bitmap)
                viewModel.notoc.consentimiento.capitan.firmaB64 = firmaCapitan.toString()
            }.show(it, "SignatureFragment")
        }
    }
    private fun firmaOfOperaciones(){
        parentFragmentManager.let {
            DialogFragmentSignature() { bitmap ->
                binding.firmaOFOperacion.setImageBitmap(bitmap)
                firmaoficinalOperaciones = CreateImageFile.getB64FromBitmap(bitmap)
                viewModel.notoc.consentimiento.oficial.firmaB64 = firmaoficinalOperaciones.toString()
            }.show(it, "SignatureFragment")
        }
    }

    private fun getCapitan() {
        binding.apply {
            if (!editNumCapitan.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                viewModel.getCapitan("${editNumCapitan.text.toString()}").observeOnce(Observer {

                    d.Ocultar()
                    it.let {
                        capitan = it.result.usuarioCore
                        tiltextNombreCapitan.visibility = View.VISIBLE
                        btnFirmarOfCapitan.visibility = View.VISIBLE
                        firmaCapitan.visibility = View.VISIBLE
                        textNombreCapitan.setText("${capitan?.name} ${capitan?.apellidoPaterno} ${capitan?.apellidoMaterno}")
                        viewModel.notoc.consentimiento.capitan.nombre = "${capitan?.name} ${capitan?.apellidoPaterno} ${capitan?.apellidoMaterno}"
                    }
                })
            } else {
                editNumCapitan.error = getString(R.string.error_campo_invalido)
            }
        }
    }

    private fun getOficial() {
        binding.apply {
            if (!editNumOficial.text.isNullOrEmpty()) {
                d.mostrarCargando(null)
                viewModel.getOficialOperaciones("${editNumOficial.text.toString()}")
                    .observeOnce(Observer {
                        d.Ocultar()
                        it.let {

                            oficinalOperaciones = it.result.usuarioCore
                            btnFirmarOFOperacion.visibility = View.VISIBLE
                            tiltextOficialOperaciones.visibility = View.VISIBLE
                            firmaOFOperacion.visibility = View.VISIBLE
                            textOficialOperaciones.setText("${oficinalOperaciones?.name} ${oficinalOperaciones?.apellidoPaterno} ${oficinalOperaciones?.apellidoMaterno}")
                            viewModel.notoc.consentimiento.oficial.nombre = "${oficinalOperaciones?.name} ${oficinalOperaciones?.apellidoPaterno} ${oficinalOperaciones?.apellidoMaterno}"
                        }
                    })
            } else {
                editNumOficial.error = getString(R.string.error_campo_invalido)
            }
        }
    }


}