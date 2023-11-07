package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Fecha {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formattter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val formattterFechaonly = SimpleDateFormat("dd/MM/yyyy")

    fun stringToFecha(fechs: String): String{
        val formated = formattter.format(parser.parse(fechs))
        return formated
    }
    fun stringToFechaYearStared(fechs: String): String{
        val formattter = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val formated = formattter.format(parser.parse(fechs))
        return formated
    }
    fun stringToFechaOnly(fechs: String): String{
        val formated = formattterFechaonly.format(parser.parse(fechs))
        return formated
    }
    fun CompararFechas(dateLast: String): Int {
        var diferencia = Calendar.getInstance().timeInMillis - dateLast.toLong()
        Log.i("time dif","${Calendar.getInstance().timeInMillis} menos $dateLast")

        val secodsmilis = 1000
        val minutesMilis = secodsmilis * 60

        val elapserminutes = diferencia / minutesMilis
        diferencia %= minutesMilis

        Log.i("time dif","-> hace ${elapserminutes.toInt()} minutos")
        return elapserminutes.toInt()
    }
     fun calendarToString(cal: Calendar): String
    {
        return  parser.format(cal.time)
    }
}