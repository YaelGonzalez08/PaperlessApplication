package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDialogSignatureBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.unableButtonEditText
import com.williamww.silkysignature.views.SignaturePad

class DialogFragmentSignature( val listener: (bitmap:Bitmap) -> Unit) : DialogFragment(), View.OnClickListener, SignaturePad.OnSignedListener {

    lateinit var binding: FragmentDialogSignatureBinding
    var signatured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogSignatureBinding.inflate(inflater, container, false)

        binding.apply {
            btnAceptar.setOnClickListener(this@DialogFragmentSignature)
            btnRedo.setOnClickListener(this@DialogFragmentSignature)
            spFirma.setOnSignedListener(this@DialogFragmentSignature)
            unableButtonEditText(false, btnAceptar)
            unableButtonEditText(false, btnRedo)
            closeDialog.setOnClickListener(this@DialogFragmentSignature)
            spFirma.setOnSignedListener(this@DialogFragmentSignature)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {
                btnRedo.id -> spFirma.clear()

                closeDialog.id -> this@DialogFragmentSignature.dismiss()

                btnAceptar.id -> {
                    if (this@DialogFragmentSignature.signatured) {
                        listener(spFirma.signatureBitmap)
                        this@DialogFragmentSignature.dismiss()
                    } else {
                        Toast.makeText(context, "Firma en el area blanca", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStartSigning() {

    }

    override fun onSigned() {
        this@DialogFragmentSignature.signatured = true
        binding.apply {
            unableButtonEditText(true, btnAceptar)
            unableButtonEditText(true, btnRedo)
        }
    }

    override fun onClear() {
        this@DialogFragmentSignature.signatured = false
        binding.apply {
            unableButtonEditText(false, btnAceptar)
            unableButtonEditText(false, btnRedo)
        }
    }
}