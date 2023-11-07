package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.view.AveriasInspeccionAeronaveFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.view.InpeccionConcentimientoFragment
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos

class AdapterPageInspeccionAeronaves(
    var fm: FragmentManager,
    var vuelo: Vuelos?,
    var inspeccionAeronave: InspeccionAeronave,
    var toConcentiemto: (inspeccion: InspeccionAeronave) -> Unit,
    var getInspeccionLocal: () -> InspeccionAeronave?
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var inspeccion = AveriasInspeccionAeronaveFragment(vuelo,inspeccionAeronave,toConcentiemto)
    var concentimiento = InpeccionConcentimientoFragment(getInspeccionLocal)
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> inspeccion
            1 -> concentimiento
            else -> {
                inspeccion
            }

        }
    }

    override fun getCount(): Int {
        return 2
    }

}