package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.tabs.ClientesCtrlAbordajeFragment
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.tabs.OficialCtrlAbordajeFragment
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.tabs.PreliminarCtrlAbordajeFragment


class ControlAbordajePageAdapter(
    var fm: FragmentManager,var fligthReference:String,var continuebtn:()-> Unit
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var clientesCtrlAbordaje = ClientesCtrlAbordajeFragment(fligthReference)
    var preliminar = PreliminarCtrlAbordajeFragment(fligthReference)
    var oficialCtrlAbordajeFragment = OficialCtrlAbordajeFragment(fligthReference,continuebtn)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> preliminar

            1 -> clientesCtrlAbordaje
            2-> oficialCtrlAbordajeFragment

            else -> {
                oficialCtrlAbordajeFragment
            }

        }
    }

    override fun getCount(): Int {
        return 3
    }

}