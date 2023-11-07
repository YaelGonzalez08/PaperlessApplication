package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs

import android.text.InputFilter
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.Pregunta
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.PreguntaCheckList

fun allBoxesSelected(boxesBien: Array<CheckBox>, boxesMal: Array<CheckBox>): Boolean {
    var flag = false

    for(i in boxesBien.indices) {
        if (boxesBien[i].isChecked || boxesMal[i].isChecked)
            flag = true
        else {
            flag = false
            break
        }
    }
    return flag
}

fun unableButtonEditText(b: Boolean, btn: TextView) {
    if (b)
        btn.apply {
            btn.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.button_consultar,
                null
            )
            btn.isEnabled = true
        }
    else
        btn.apply {
            btn.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.button_consultar_disabled,
                null
            )
            btn.isEnabled = false
        }
}

fun unableEmployeeName(s: Boolean, actv: AutoCompleteTextView) {
    if (s)
        actv.apply {
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(25))
            isEnabled = false
            setTextColor(resources.getColor(R.color.black))
        }
    else
        actv.apply {
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            isEnabled = true
            setText("")
        }
}

fun unableSignatureImageView(b: Boolean, ivFirma: ImageView) {
    ivFirma.apply {
         if(b){
             visibility = View.GONE
             background = ResourcesCompat.getDrawable(resources, R.drawable.border_for_image_view_signature_gray, null)
        } else {
             visibility = View.VISIBLE
             background = ResourcesCompat.getDrawable(resources, R.drawable.border_for_image_view_signature, null)
        }
        setImageBitmap(null)
    }
}

fun llenarFromulario(
    optBoxesBueno: Array<CheckBox>,
    preguntasToserver: MutableList<Pregunta>
) {
    for (i in optBoxesBueno.indices) {
        if (!optBoxesBueno[i].isChecked)
            preguntasToserver[i].condicion = 0
        else
            preguntasToserver[i].condicion = 1
    }
}

fun llenarFromularioSerchLis(
    optBoxesBueno: Array<CheckBox>,
    preguntasToserver: MutableList<PreguntaCheckList>
) {
    for (i in optBoxesBueno.indices) {
        if (!optBoxesBueno[i].isChecked)
            preguntasToserver[i].condicion = 0
        else
            preguntasToserver[i].condicion = 1
    }
}



