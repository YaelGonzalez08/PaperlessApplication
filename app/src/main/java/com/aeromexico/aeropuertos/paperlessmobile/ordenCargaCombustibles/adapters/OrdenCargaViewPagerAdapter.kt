package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.views.tabs.CombustibleConsentimiento
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.views.tabs.InformacionCombustible
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos

class OrdenCargaViewPagerAdapter(
    fragmentManager: FragmentManager,
    vuelo: Vuelos?,
    rqOrdenCarga: RequestOrdenCarga,
    updateFormBD: (rq: RequestOrdenCarga) -> Unit,
    var enviarForm:() -> Unit
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var informacionCombustible = InformacionCombustible(vuelo, rqOrdenCarga, updateFormBD)
    var combustibleConsentimiento = CombustibleConsentimiento(vuelo,enviarForm,rqOrdenCarga)


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> informacionCombustible

            1 -> combustibleConsentimiento

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

}