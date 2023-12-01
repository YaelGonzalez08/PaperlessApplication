package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.R
import java.util.*

class DatePickerFragment(val listener:(day:Int, month:Int, year:Int) -> Unit): DialogFragment(),
        DatePickerDialog.OnDateSetListener {
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        listener(p3,p2,p1)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c =Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        c.add(Calendar.DAY_OF_MONTH,-1)
        val picker = DatePickerDialog(activity as Context, R.style.datePickerDialoghelper,this,year,month,day)
        picker.datePicker.minDate = c.timeInMillis
        val max = Calendar.getInstance()
        max.add(Calendar.DAY_OF_MONTH,1)
        picker.datePicker.maxDate = max.timeInMillis
        return picker
    }
}