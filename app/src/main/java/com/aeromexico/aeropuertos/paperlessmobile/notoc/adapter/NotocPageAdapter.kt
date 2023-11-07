package com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs.*

class NotocPageAdapter   (  fm: FragmentManager
) :
FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InfoMercanciaMainFragment()

            1 -> InfoOtraCargaMainFragment()

            2 -> notocConcentimientoFragment()

            else -> {
                InfoMercanciaMainFragment()
            }

        }
    }

    override fun getCount(): Int {
        return 3
    }

}