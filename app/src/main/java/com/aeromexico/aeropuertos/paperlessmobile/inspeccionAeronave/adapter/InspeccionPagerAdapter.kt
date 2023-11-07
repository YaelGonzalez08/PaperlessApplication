package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.ConsentimientoFormFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.InspeccionFormFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Finalizacion.FinalizacionInspeccionAeronaveFragment


class InspeccionPagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                 InspeccionFormFragment();
            }
            1 -> {
                ConsentimientoFormFragment();
            }
             2->{
                 FinalizacionInspeccionAeronaveFragment();

             }
            else -> {
                FinalizacionInspeccionAeronaveFragment();
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> {
                "Inspección de aeronave"
            }
            1 -> {
                "Consentimiento"
            }
            else -> {
                ""
            }
        }
    }


}