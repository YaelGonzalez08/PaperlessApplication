package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.R
import java.util.*

class DatePickerDialogHelper(val oneLastYear:Boolean, val threDaysAvailable:Boolean, val listener: (day:Int, month:Int, year:Int) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val picker = context?.let {
            DatePickerDialog(
                it,
                R.style.datePickerDialoghelper,
                this,
                year,
                month,
                day
            )
        }

        if (threDaysAvailable) {
            picker?.let {
                it.datePicker.maxDate = c.timeInMillis + 1000 * 60 * 60 * 24
                it.datePicker.minDate = c.timeInMillis - 1000 * 60 * 60 * 24
            }
        }

        if (oneLastYear) {
            picker?.let {
                it.datePicker.minDate = c.timeInMillis - 31556952000
            }
        }

        return picker as Dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month + 1, year)
    }

}