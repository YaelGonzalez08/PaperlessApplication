package com.aeromexico.aeropuertos.paperlessmobile.desHielo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs.ConcentimientoFragment
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs.CondicionesMeteorologicasFragment
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.tabs.InformacionServicioFragment
import com.github.ksoichiro.android.observablescrollview.ScrollState

class DeshieloPageAdapter(
    fm: FragmentManager,
    scrollState: (ScrollState?) -> Unit
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var informacionServicioFragment = InformacionServicioFragment(scrollState)
    var concentimientoFragment = ConcentimientoFragment(scrollState)
    var condicionesMeteorologicasFragment = CondicionesMeteorologicasFragment(scrollState)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> informacionServicioFragment

            1 -> condicionesMeteorologicasFragment

            2 -> concentimientoFragment

            else -> {
                informacionServicioFragment
            }

        }
    }

    override fun getCount(): Int {
        return 3
    }

}