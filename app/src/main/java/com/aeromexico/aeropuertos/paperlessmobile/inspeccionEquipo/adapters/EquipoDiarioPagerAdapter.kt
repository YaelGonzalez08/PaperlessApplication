package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.tabs.CheckListConsentimientoTab
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.tabs.EquipoCheckListTab
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Equipo
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Preguntas
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore

class EquipoDiarioPagerAdapter(
    fm: FragmentManager,
    var lista: List<Preguntas>,
    var operador: UsuarioCore,
    var supervisor: UsuarioCore,
    var equipo: Equipo
):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
     var equipoCheck: EquipoCheckListTab = EquipoCheckListTab(lista)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                equipoCheck
            }

            else -> {
                CheckListConsentimientoTab(operador,supervisor)
            }

        }
    }

    override fun getCount(): Int {
        return 2
    }

}