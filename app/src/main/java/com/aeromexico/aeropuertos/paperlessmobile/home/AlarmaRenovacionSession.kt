package com.aeromexico.aeropuertos.paperlessmobile.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import java.util.*


class AlarmaRenovacionSession : BroadcastReceiver() {
    private var mActivity: MainActivity? = null
    private  var contexto = PaperlessApplication.contextAppSingleton
    companion object{
        var instnce : AlarmaRenovacionSession?=null

        fun getInstance(): AlarmaRenovacionSession? {
            if(instnce == null){
                instnce = AlarmaRenovacionSession()
            }
            return instnce
        }
    }
    init {
        instnce = this
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val c: Calendar = Calendar.getInstance()
        MainActivity.getInstance()?.mostrarDialogoSessionExpiracion()
        Log.i("AlarmaRenovacionSession", "Show Alarma  at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}")
    }

    fun close(){
        MainActivity.getInstance()?.logOut()
    }

    fun startAlarm(c: Calendar, param: Context) {
        contexto = param
        val alarmManager = contexto.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(contexto, AlarmaRenovacionSession::class.java)
        val pendingIntent = PendingIntent.getBroadcast(contexto, 1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }

        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        Log.i("AlarmaRenovacionSession", "startAlarm to  at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}")
    }
    fun cancelAlarm() {
        val alarmManager =contexto.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(contexto, AlarmaRenovacionSession::class.java)
        val pendingIntent = PendingIntent.getBroadcast(contexto, 1, intent, 0)
        alarmManager!!.cancel(pendingIntent)
        Log.i("AlarmaRenovacionSession","cancelAlarm")
    }

}