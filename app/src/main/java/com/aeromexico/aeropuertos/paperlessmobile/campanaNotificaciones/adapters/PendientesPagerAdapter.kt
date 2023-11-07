package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs.CargaCombustiblePendienteTab
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs.CheckListEquipoPendientesTab
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs.DeshieloPendientesTab
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs.PrimerVueloDiaPendienteTab
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Pendientes.PendientesInspecAeronave
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.PendientesMensajesOperacionalesFragment
import com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes.PendientesNotocFragment

class PendientesPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 7 //-->> RECUERDA cambiar este valor si agregas un nuevo fragmento al viewpager si no no se muestra
    }

    override fun getItem(position: Int): Fragment {
        Log.i("getItem ->", "$position")
        return when (position) {
            0 -> {
                PendientesMensajesOperacionalesFragment()
            }
            1 -> {
                PendientesInspecAeronave()
            }

            3 -> {
                CheckListEquipoPendientesTab()
            }
            4 -> {
                DeshieloPendientesTab()
            }
            5 -> {
                CargaCombustiblePendienteTab()
            }

            2 -> {
                PrimerVueloDiaPendienteTab()
            }
            6 -> {
                PendientesNotocFragment()
            }
            else -> {
                PendientesMensajesOperacionalesFragment()
            }
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
         return when (position) {
            0 -> {
                (R.string.mensajes_operacionales).toString()
            }
            1 -> {
                R.string.inspeccion_de_aeronave.toString()
            }
            2 -> {
                R.string.primer_vuelo.toString()
            }
            3 -> {
                R.string.inspeccion_de_equipo.toString()
            }
            4 -> {
                "Deshielo"
            }
            5 -> {
                "Carga Combustible"
            }
             6 -> {
                 "Notoc"
             }
            else -> {
                super.getPageTitle(position)
            }
        }
    }
}