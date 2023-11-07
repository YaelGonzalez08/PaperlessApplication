package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.views.tabs

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInformacionCombustibleBinding
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.google.android.material.textfield.TextInputEditText
import ng.softcom.android.utils.ui.showSnackBar
import ng.softcom.android.utils.ui.showToast
import java.util.regex.Matcher
import java.util.regex.Pattern


class InformacionCombustible(val vuelo: Vuelos?, var rqOrdenCarga: RequestOrdenCarga,var updateFormBD:(rq:RequestOrdenCarga)-> Unit) : Fragment(), View.OnClickListener{
    lateinit var binding: FragmentInformacionCombustibleBinding
    lateinit var densityValues: List<String>
    lateinit var tipoCargaArray: List<String>
    lateinit var degrees: List<String>
    lateinit var volumen: List<String>
    lateinit var centralTankRow: List<View>
    lateinit var ruledFuel: List<EditText>
    lateinit var camposTIET: List<TextInputEditText>
    lateinit var camposET: List<EditText>
    lateinit var equiposConnect: List<String>
    var flagBox = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tipoCargaArray = arrayListOf("Carga", "Precarga", "Retorno")
        densityValues = arrayListOf("Kg/m3", "Kg/l", "g/cm3")
        degrees = arrayListOf("Cº", "Fº")
        volumen = arrayListOf("LT", "USG")
        equiposConnect = arrayListOf("ERJ", "E9B", "E9A", "E90", "E70", "E190", "E170")
        binding = FragmentInformacionCombustibleBinding.inflate(layoutInflater)

        showData()
        setWatchers()

        binding.apply {
            camposTIET = listOf(
                tietTemperatura,
                tietDensidad,
                tietCombustibleAntesCargar,
                tietVolCargadoDispensador
            )
            ruledFuel = listOf(
                etIzquierdoPorReglas,
                etCentralPorReglas,
                tvderechoPorReglas
            )
            centralTankRow = arrayListOf(
                tvCentral,
                etCentralCombustiblerequerido,
                etCentralIndicado,
                etCentralPorReglas
            )
            spinUnidadesDensidad.apply {
                setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, densityValues))
                onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        tietDensidad.setText("0")
                        setOnClickListener{ hideKeyboard()}
                    }
            }
            spinGrados.apply {
                setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, degrees))
                onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        tietTemperatura.setText("0")
                        setOnClickListener{ hideKeyboard()}
                    }
            }
            spinTipoCarga.apply {
                setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, tipoCargaArray))

            }
            spinVolumen.apply {
                setAdapter(ArrayAdapter(requireContext(), R.layout.drop_down_item, volumen))
                onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    tietVolCargadoDispensador.setText("0")
                    setOnClickListener{ hideKeyboard()}
                }
            }
            tilGrados.setOnClickListener{ it.hideKeyboard()}
            tilVolCargadoDispensador.setOnClickListener{it.hideKeyboard()}
                setValidaciones()
                if (vuelo?.companiaEquipo == "5D") {
                    hideCentralTank()
                    cbLibras.apply {
                        isChecked = true
                        isEnabled = false
                    }
                    cbKilos.apply {
                        isChecked = false
                        isEnabled = false
                    }
                } else {
                    cbLibras.apply {
                        isChecked = false
                        isEnabled = false

                    }
                    cbKilos.apply {
                        isChecked = true
                        isEnabled = false
                    }
                }

                camposET = if (tvCentral.visibility == View.GONE)
                    listOf(
                        etIzquierdoCombustibleRequerido,
                        etIzquierdoIndicado,
                        tvDerechoCombustibleRequerido,
                        tvderechoIndicado
                    )
                else
                    listOf(
                        etIzquierdoCombustibleRequerido,
                        etIzquierdoIndicado,
                        etCentralCombustiblerequerido,
                        etCentralIndicado,
                        tvDerechoCombustibleRequerido,
                        tvderechoIndicado
                    )
            cbFallaIndicador.setOnClickListener(this@InformacionCombustible)
            }

        binding.btnGuardarLocal.setOnClickListener {
            setValuesToEntity()
            showToast("Guardado!")
            updateFormBD.invoke(rqOrdenCarga)
        }
        return binding.root
    }

    private fun showData() {
        var tmep = rqOrdenCarga.temperatura.substringBefore("C")
        if (!tmep.isNullOrEmpty()) {
            binding.tietTemperatura.setText(tmep)
        }
        if (!rqOrdenCarga.densidad.toString().isNullOrEmpty()) {
            binding.tietDensidad.setText(rqOrdenCarga.densidad.toString())
        }
        if (!rqOrdenCarga.combustibleAntesCarga.toString().isNullOrEmpty()) {
            binding.tietCombustibleAntesCargar.setText(rqOrdenCarga.combustibleAntesCarga.toString())
        }
        tmep = rqOrdenCarga.volumenCargadoDispensador.substringBefore("LT")

        if (!tmep.isNullOrEmpty()) {
            binding.tietVolCargadoDispensador.setText(tmep)
        }
        tmep = tmep.substringBefore("USG")

        if (!tmep.isNullOrEmpty()) {
            binding.tietVolCargadoDispensador.setText(tmep.trim())
        }
        if (!rqOrdenCarga.tipoCarga.isNullOrEmpty()) {
            binding.spinTipoCarga.setText(rqOrdenCarga.tipoCarga)

        }
        if (!rqOrdenCarga.unidadDensidad.isNullOrEmpty()) {
            binding.spinUnidadesDensidad.setSelection(densityValues.indexOf(rqOrdenCarga.unidadDensidad))
        }
        if (!rqOrdenCarga.tanqueIzquierdo.requerido.toString().isNullOrEmpty()) {
            binding.etIzquierdoCombustibleRequerido.setText(rqOrdenCarga.tanqueIzquierdo.requerido.toString())
        }
        if (!rqOrdenCarga.tanqueIzquierdo.indicado.toString().isNullOrEmpty()) {
            binding.etIzquierdoIndicado.setText(rqOrdenCarga.tanqueIzquierdo.indicado.toString())
        }
        if (!rqOrdenCarga.tanqueIzquierdo.reglas.toString().isNullOrEmpty()) {
            binding.etIzquierdoPorReglas.setText(rqOrdenCarga.tanqueIzquierdo.reglas.toString())
        }

        if (!rqOrdenCarga.tanqueCentral.requerido.toString().isNullOrEmpty()) {
            binding.etCentralCombustiblerequerido.setText(rqOrdenCarga.tanqueCentral.requerido.toString())
        }
        if (!rqOrdenCarga.tanqueCentral.indicado.toString().isNullOrEmpty()) {
            binding.etCentralIndicado.setText(rqOrdenCarga.tanqueCentral.indicado.toString())
        }
        if (!rqOrdenCarga.tanqueCentral.reglas.toString().isNullOrEmpty()) {
            binding.etCentralPorReglas.setText(rqOrdenCarga.tanqueCentral.reglas.toString())
        }

        if (!rqOrdenCarga.tanqueDerecho.requerido.toString().isNullOrEmpty()) {
            binding.tvDerechoCombustibleRequerido.setText(rqOrdenCarga.tanqueDerecho.requerido.toString())
        }
        if (!rqOrdenCarga.tanqueDerecho.indicado.toString().isNullOrEmpty()) {
            binding.tvderechoIndicado.setText(rqOrdenCarga.tanqueDerecho.indicado.toString())
        }
        if (!rqOrdenCarga.tanqueDerecho.reglas.toString().isNullOrEmpty()) {
            binding.tvderechoPorReglas.setText(rqOrdenCarga.tanqueDerecho.reglas.toString())
        }
        if(rqOrdenCarga.veces >= 5){
            binding.btnGuardarLocal.isVisible = false
            binding.adversaid.isVisible = false
        }
        binding.apply {
            var izqRequ = if (etIzquierdoCombustibleRequerido.text.toString() == "") 0.0 else etIzquierdoCombustibleRequerido.text.toString().toDouble()
            var cenRequ =  if(etCentralCombustiblerequerido.text.toString() == "") 0.0 else etCentralCombustiblerequerido.text.toString().toDouble()
            var derRequ =  if(tvDerechoCombustibleRequerido.text.toString() == "") 0.0 else tvDerechoCombustibleRequerido.text.toString().toDouble()

            tvTotalCombustibleRequerido.text = (izqRequ + cenRequ + derRequ).toString()
        }
        binding.apply {
            var izqRequ = if (etIzquierdoIndicado.text.toString() == "") 0.0 else etIzquierdoIndicado.text.toString().toDouble()
            var cenRequ =  if(etCentralIndicado.text.toString() == "") 0.0 else etCentralIndicado.text.toString().toDouble()
            var derRequ =  if(tvderechoIndicado.text.toString() == "") 0.0 else tvderechoIndicado.text.toString().toDouble()

            tvTotalIndicado.text = (izqRequ + cenRequ + derRequ).toString()
        }
        binding.apply {
            var izqRequ = if (etIzquierdoPorReglas.text.toString() == "") 0.0 else etIzquierdoPorReglas.text.toString().toDouble()
            var cenRequ =  if(etCentralPorReglas.text.toString() == "") 0.0 else etCentralPorReglas.text.toString().toDouble()
            var derRequ =  if(tvderechoPorReglas.text.toString() == "") 0.0 else tvderechoPorReglas.text.toString().toDouble()
            tvTotalPorReglas.text = (izqRequ + cenRequ + derRequ).toString()
        }

    }

    private fun setWatchers() {
        binding.apply {

            tietCombustibleAntesCargar.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val actual = if (s.toString() == "") 0.0 else s.toString().toDouble()
                    if (cbKilos.isChecked && actual > vuelo?.capacidadGasolina!!){
                        showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de KG por abastecer.")
                        tietCombustibleAntesCargar.setText("0")
                    } else if (cbLibras.isChecked && actual > vuelo?.capacidadGasolina!!){
                        showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de KG por abastecer.")
                        tietCombustibleAntesCargar.setText("0")
                    }
                    
                }

            })

            etIzquierdoCombustibleRequerido.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoCombustibleRequerido.text.toString() == "") 0.0 else etIzquierdoCombustibleRequerido.text.toString().toDouble()
                    var cenRequ =  if(etCentralCombustiblerequerido.text.toString() == "") 0.0 else etCentralCombustiblerequerido.text.toString().toDouble()
                    var derRequ =  if(tvDerechoCombustibleRequerido.text.toString() == "") 0.0 else tvDerechoCombustibleRequerido.text.toString().toDouble()

                    tvTotalCombustibleRequerido.text = (izqRequ + cenRequ + derRequ).toString()

                    if(izqRequ > vuelo?.tanqueIzquierdo!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque izquierdo.")
                        etIzquierdoCombustibleRequerido.setText("0")
                    }
                    
                }
            })

            etCentralCombustiblerequerido.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoCombustibleRequerido.text.toString() == "") 0.0 else etIzquierdoCombustibleRequerido.text.toString().toDouble()
                    var cenRequ =  if(etCentralCombustiblerequerido.text.toString() == "") 0.0 else etCentralCombustiblerequerido.text.toString().toDouble()
                    var derRequ =  if(tvDerechoCombustibleRequerido.text.toString() == "") 0.0 else tvDerechoCombustibleRequerido.text.toString().toDouble()

                    tvTotalCombustibleRequerido.text = (izqRequ + cenRequ + derRequ).toString()

                    if(cenRequ > vuelo?.tanqueCentral!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque central.")
                        etCentralCombustiblerequerido.setText("0")
                    }
                    
                }

            })

            tvDerechoCombustibleRequerido.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoCombustibleRequerido.text.toString() == "") 0.0 else etIzquierdoCombustibleRequerido.text.toString().toDouble()
                    var cenRequ =  if(etCentralCombustiblerequerido.text.toString() == "") 0.0 else etCentralCombustiblerequerido.text.toString().toDouble()
                    var derRequ =  if(tvDerechoCombustibleRequerido.text.toString() == "") 0.0 else tvDerechoCombustibleRequerido.text.toString().toDouble()

                    tvTotalCombustibleRequerido.text = (izqRequ + cenRequ + derRequ).toString()

                    if(derRequ > vuelo?.tanqueDerecho!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque derecho.")
                        tvDerechoCombustibleRequerido.setText("0")
                    }
                    
                }
            })

            etIzquierdoIndicado.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoIndicado.text.toString() == "") 0.0 else etIzquierdoIndicado.text.toString().toDouble()
                    var cenRequ =  if(etCentralIndicado.text.toString() == "") 0.0 else etCentralIndicado.text.toString().toDouble()
                    var derRequ =  if(tvderechoIndicado.text.toString() == "") 0.0 else tvderechoIndicado.text.toString().toDouble()

                    tvTotalIndicado.text = (izqRequ + cenRequ + derRequ).toString()

                    if(izqRequ > vuelo?.tanqueIzquierdo!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque izquierdo.")
                        etIzquierdoIndicado.setText("0")
                    }
                    
                }

            })

            etCentralIndicado.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoIndicado.text.toString() == "") 0.0 else etIzquierdoIndicado.text.toString().toDouble()
                    var cenRequ =  if(etCentralIndicado.text.toString() == "") 0.0 else etCentralIndicado.text.toString().toDouble()
                    var derRequ =  if(tvderechoIndicado.text.toString() == "") 0.0 else tvderechoIndicado.text.toString().toDouble()

                    tvTotalIndicado.text = (izqRequ + cenRequ + derRequ).toString()

                    if(cenRequ > vuelo?.tanqueCentral!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque central.")
                        etCentralIndicado.setText("0")
                    }
                    
                }

            })

            tvderechoIndicado.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoIndicado.text.toString() == "") 0.0 else etIzquierdoIndicado.text.toString().toDouble()
                    var cenRequ =  if(etCentralIndicado.text.toString() == "") 0.0 else etCentralIndicado.text.toString().toDouble()
                    var derRequ =  if(tvderechoIndicado.text.toString() == "") 0.0 else tvderechoIndicado.text.toString().toDouble()

                    tvTotalIndicado.text = (izqRequ + cenRequ + derRequ).toString()

                    if(derRequ > vuelo?.tanqueDerecho!!) {
                        showMessage("Verifica la cantidad máxima de combustible para el tanque derecho.")
                        tvderechoIndicado.setText("0")
                    }
                    
                }
            })

            etIzquierdoPorReglas.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoPorReglas.text.toString() == "") 0.0 else etIzquierdoPorReglas.text.toString().toDouble()
                    var cenRequ =  if(etCentralPorReglas.text.toString() == "") 0.0 else etCentralPorReglas.text.toString().toDouble()
                    var derRequ =  if(tvderechoPorReglas.text.toString() == "") 0.0 else tvderechoPorReglas.text.toString().toDouble()
                    tvTotalPorReglas.text = (izqRequ + cenRequ + derRequ).toString()
                    
                }

            })

            etCentralPorReglas.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoPorReglas.text.toString() == "") 0.0 else etIzquierdoPorReglas.text.toString().toDouble()
                    var cenRequ =  if(etCentralPorReglas.text.toString() == "") 0.0 else etCentralPorReglas.text.toString().toDouble()
                    var derRequ =  if(tvderechoPorReglas.text.toString() == "") 0.0 else tvderechoPorReglas.text.toString().toDouble()
                    tvTotalPorReglas.text = (izqRequ + cenRequ + derRequ).toString()
                    
                }

            })

            tvderechoPorReglas.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    var izqRequ = if (etIzquierdoPorReglas.text.toString() == "") 0.0 else etIzquierdoPorReglas.text.toString().toDouble()
                    var cenRequ =  if(etCentralPorReglas.text.toString() == "") 0.0 else etCentralPorReglas.text.toString().toDouble()
                    var derRequ =  if(tvderechoPorReglas.text.toString() == "") 0.0 else tvderechoPorReglas.text.toString().toDouble()
                    tvTotalPorReglas.text = (izqRequ + cenRequ + derRequ).toString()
                    
                }

            })

            tvTotalCombustibleRequerido.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    
                   /* binding.apply {
                        if(s.toString() != ""){
                            if(cbKilos.isChecked){
                                if((s.toString().toDouble()) > vuelo?.capacidadGasolina!!){
                                    showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de KG por abastecer.")
                                    cleanMatrixRequerido()
                                }
                            } else if(cbLibras.isChecked){
                                if((s.toString().toDouble()) > vuelo?.capacidadGasolina!!){
                                    showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de LB por abastecer.")
                                    cleanMatrixRequerido()
                                }
                            }
                        }
                    }*/
                }

            })

            tvTotalIndicado.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    
                   /* binding.apply {
                        if(s.toString() != ""){
                            if(cbKilos.isChecked){
                                if((s.toString().toDouble()) > vuelo?.capacidadGasolina!!){
                                    showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de KG por abastecer.")
                                    clearMatrixIndicado()
                                }
                            } else if(cbLibras.isChecked){
                                if((s.toString().toDouble()) > vuelo?.capacidadGasolina!!){
                                    showMessage("Verifica la distribución de combustible, has superado la cantidad máxima de LB por abastecer.")
                                    clearMatrixIndicado()
                                }
                            }
                        }
                    }*/
                }
            })
        }
    }

    private fun cleanMatrixRequerido() {
        binding.apply {
            etIzquierdoCombustibleRequerido.setText("0")
            etCentralCombustiblerequerido.setText("0")
            tvDerechoCombustibleRequerido.setText("0")
        }
    }

    private fun clearMatrixIndicado() {
        binding.apply {
            etIzquierdoIndicado.setText("0")
            etCentralIndicado.setText("0")
            tvderechoIndicado.setText("0")
        }
    }

    private fun setValidaciones() {
        binding.apply {
            tietTemperatura.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() != "" && s.toString() != "-" && spinGrados.text.toString() == "Cº"){
                        if (s.toString().toDouble() > 100 || s.toString().toDouble() < -100) {
                            tietTemperatura.setText(s.toString().subSequence(0, s.toString().length -1))
                            showMessage("Los grados celsius permitidos van desde -100 a 100")
                        }
                    } else if (s.toString() != "" && s.toString() != "-" && spinGrados.text.toString() == "Fº"){
                        if (s.toString().toDouble() > 212 || s.toString().toDouble() < -148) {
                            tietTemperatura.setText(s.toString().subSequence(0, s.toString().length -1))
                            showMessage("Los grados fahrenheit permitidos van desde -148 a 212")
                        }
                    }
                }
            })

            tietDensidad.filters = arrayOf(DecimalDigitsInputFilter(3, 4))
            tietTemperatura.filters = arrayOf(DecimalDigitsInputFilter(3, 4))
            tietCombustibleAntesCargar.filters = arrayOf(DecimalDigitsInputFilter(6,2))
            tietVolCargadoDispensador.filters = arrayOf(DecimalDigitsInputFilter(6, 2))
        }
    }

    fun validarCampos():Boolean{
        var flag = true
        binding.apply {
            camposTIET.forEach { if ("${it.text}" == ""){ flag = false; return@forEach} }
            camposET.forEach { if("${it.text}" == ""){ flag = false; return@forEach} }

            if(tietCombustibleAntesCargar.text.toString() == "")
                tilCombustibleAntesCargar.error = getString(R.string.error_campo_invalido)
            if (!cbLibras.isChecked && !cbKilos.isChecked)
                flag = false
        }
        if(flag)
            setValuesToEntity()
        return flag
    }

    override fun onPause() {
        super.onPause()
        setValuesToEntity()
    }
    fun  setValuesToEntity() {

        binding.apply {
            val helper = tvCentral.visibility == View.GONE

            rqOrdenCarga.apply {
                temperatura = "${tietTemperatura.text}${spinGrados.text}"
                densidad = "${tietDensidad.text}".toDouble()
                combustibleAntesCarga = "${tietCombustibleAntesCargar.text}".toDouble()
                distribucionCombustible = if (cbKilos.isChecked) "KG" else "LB"
                tanqueIzquierdo.apply {
                    requerido = etIzquierdoCombustibleRequerido.text.toString().toDouble()
                    indicado = etIzquierdoIndicado.text.toString().toDouble()
                    reglas = if (etIzquierdoPorReglas.text.toString() == "") 0.0 else etIzquierdoPorReglas.text.toString().toDouble()
                }
                tanqueCentral.apply {
                    requerido = if(helper) 0.0 else etCentralCombustiblerequerido.text.toString().toDouble()
                    indicado = if(helper) 0.0 else etCentralIndicado.text.toString().toDouble()
                    reglas = if(helper || etCentralPorReglas.text.toString() == "") 0.0 else etCentralPorReglas.text.toString().toDouble()
                }
                tanqueDerecho.apply {
                    requerido = tvDerechoCombustibleRequerido.text.toString().toDouble()
                    indicado = tvderechoIndicado.text.toString().toDouble()
                    reglas = if(tvderechoPorReglas.text.toString() == "") 0.0 else tvderechoPorReglas.text.toString().toDouble()
                }
                volumenCargadoDispensador = "${tietVolCargadoDispensador.text} ${spinVolumen.text}"
               tipoCarga = "${spinTipoCarga.text}"
                unidadDensidad = "${spinUnidadesDensidad.text}"
            }
        }

    }

    private fun showMessage(s: String) {
        val builder = AlertDialog.Builder(context, R.style.dialogNoCapitalLettersInButtons)
        builder.setMessage(s)
            .setPositiveButton(R.string.aceptar) { dialog, _ -> dialog.dismiss() }
        builder.create()
        builder.show()
    }

    private fun hideCentralTank() {
        centralTankRow.forEach { view -> view.visibility = View.GONE }
    }

    private fun unableRequiredFuel(isEnabled: Boolean){
        ruledFuel.forEach {
            it.isEnabled = !isEnabled
            it.background = ResourcesCompat.getDrawable(resources, R.color.color_gray_white_background_checklist_faded, null)
            it.setText("0")
        }
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        private var mPattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }

    }

    override fun onClick(v: View?) {
        binding.apply {
            /*when(v?.id) {
                cbFallaIndicador.id -> {
                    flagBox != flagBox
                    unableRequiredFuel(flagBox)
                }
            }*/
        }
    }

}