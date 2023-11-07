package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.databinding.AveriaCaptureDialogBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia

class AveriaCaptureDialogDialog(
    var averia: Averia,
    var type: Int,
    val averiaListener: (averia: Averia) -> Unit
) :
    DialogFragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    lateinit var binding: AveriaCaptureDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AveriaCaptureDialogBinding.inflate(layoutInflater)
        this@AveriaCaptureDialogDialog.isCancelable = false
        if (type == TIPO_DESCRIPCION){
            showDescripcionmode()
        }
        if (type == TIPO_NUMERICO){
            showNumberMode()
        }
        if(type == TIPO_OTROS){
            showOtrosMode()
        }

        binding.apply {
            textView29.setText(averia.TipoAveria)
            btnAceptar.setOnClickListener {
                averiaListener.invoke(averia)
                this@AveriaCaptureDialogDialog.dismiss()
            }
            closeDialog.setOnClickListener {
                this@AveriaCaptureDialogDialog.dismiss()
            }
            if (!averia.descripcionAveria.isNullOrEmpty()) {
                editDetalle.setText(averia.descripcionAveria)
            }
        }
        return binding.root

    }

    private fun showOtrosMode() {
        binding.apply {
            contDetalle.visibility = View.VISIBLE
            contsArea.visibility = View.VISIBLE
            editDetalle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                    averia.descripcionAveria = (if (editArea.text.toString()
                            .isNullOrEmpty()
                    ) "" else editArea.text.toString()) + ":" + editDetalle.text.toString()
                //    updateSilence.invoke(item, position)
                    btnAceptar.visibility = View.VISIBLE

                }

            })

            editArea.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                    averia.descripcionAveria = (if (editArea.text.toString()
                            .isNullOrEmpty()
                    ) "" else editArea.text.toString()) + ":" + editDetalle.text.toString()
                  //  updateSilence.invoke(item, position)
                    btnAceptar.visibility = View.VISIBLE

                }

            })
        }
    }

    private fun showNumberMode() {
        binding.apply {
            contsOnlyNumber.visibility = View.VISIBLE
            editNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                    if (!editNumber.text.toString().isNullOrEmpty()) {
                        averia.respuestaAux = editNumber.text.toString()
                     //   updateSilence.invoke(item, position)
                        btnAceptar.visibility = View.VISIBLE
                    }
                }

            })
        }
    }

    private fun showDescripcionmode() {
        binding.apply {

            contDetalle.visibility = View.VISIBLE
            editDetalle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (!editDetalle.text.toString().isNullOrEmpty()) {
                        btnAceptar.visibility = View.VISIBLE
                        averia.descripcionAveria = editDetalle.text.toString()
                    }
                }

            })


        }
    }

    companion object{
        const val TIPO_DESCRIPCION = 0
        const val TIPO_NUMERICO = 1
        const val TIPO_OTROS = 2
    }


}