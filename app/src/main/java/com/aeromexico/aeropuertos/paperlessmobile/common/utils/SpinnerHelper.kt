package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.IATA

class SpinnerHelper(private val spin: AutoCompleteTextView) : AdapterView.OnItemSelectedListener {

    fun getAdapterIATAS (context: Context, values: MutableList<IATA>) : ArrayAdapter<IATA> {
        spin.onItemSelectedListener = this
        return ArrayAdapter(context, R.layout.drop_down_item, values)
    }

    fun getAdapterString (context: Context, values: MutableList<String>) : ArrayAdapter<String> {
        spin.onItemSelectedListener = this
        return ArrayAdapter(context, R.layout.drop_down_item, values)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.e("onItemSelected", "${view?.id}, item: ${parent?.adapter?.getItem(position)}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}