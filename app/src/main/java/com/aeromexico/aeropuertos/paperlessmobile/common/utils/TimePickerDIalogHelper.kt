package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import java.util.*

class TimePickerDIalogHelper(val listener: (String) -> Unit) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val min = c.get(Calendar.MINUTE)
        val dialog = context.let {
            TimePickerDialog(
                it,
                R.style.datePickerDialoghelper,
                this,
                hour,
                min,
                true
            )
        }
        return dialog
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        var stri = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
        var stri2 = if (minute < 10) "0$minute" else "$minute"
        listener("$stri:$stri2")
    }
}