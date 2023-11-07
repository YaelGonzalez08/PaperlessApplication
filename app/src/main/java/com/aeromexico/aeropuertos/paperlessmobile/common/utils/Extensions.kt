package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.google.android.material.textfield.TextInputEditText

fun changeValues(cbTouched: CheckBox, cbNotTouched: CheckBox) {
    if(!cbTouched.isChecked) {
        cbTouched.isChecked = false
        cbNotTouched.isChecked = true
    }
    else {
        cbTouched.isChecked = true
        cbNotTouched.isChecked = false
    }
}

fun showDialogBoxMalSelected(context:Context){
    val builder = AlertDialog.Builder(context, R.style.dialogNoCapitalLettersInButtons)
    builder.setMessage(R.string.dialog_mal_selected)
        .setPositiveButton(R.string.aceptar) { dialog, _ -> dialog.dismiss() }
    builder.create()
    builder.show()
}

fun changeFormatDate(day: Int, month: Int, year: Int): String {
    var txt = if (month <= 10) "0$month-" else "$month-"
    txt += if (day <= 9)  "0$day-$year" else "$day-$year"
    return txt
}

fun setCheckBoxesLogic(
    optionsBoxesMalo: Array<CheckBox>,
    optionsBoxesBueno: Array<CheckBox>,
    v: View?,
    requireContext: Context
) {
    for(selected in optionsBoxesBueno.indices) {
             if (v?.id == optionsBoxesBueno[selected].id) {
            changeValues(optionsBoxesBueno[selected], optionsBoxesMalo[selected])
            if (optionsBoxesMalo[selected].isChecked)
                showDialogBoxMalSelected(requireContext)
        }
    }
    for (selected in optionsBoxesMalo.indices) {
        if (v?.id == optionsBoxesMalo[selected].id) {
            changeValues(optionsBoxesMalo[selected], optionsBoxesBueno[selected])
            if (optionsBoxesMalo[selected].isChecked)
                showDialogBoxMalSelected(requireContext)
        }
    }
}

fun View.hideKeyboard() {
    val imm = PaperlessApplication.contextAppSingleton.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

// usar cuando quieres controlar que tu observador solo se ejecute una vez
fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


 fun showSelectHour(view: TextInputEditText, parentFragmentManager: FragmentManager) {
    val timePicker = TimePickerDIalogHelper { onTimeSelected(it, view)}
    timePicker.show(parentFragmentManager, "time")
}

 fun onTimeSelected(str: String, view: TextInputEditText) {
    view.setText(str)
}