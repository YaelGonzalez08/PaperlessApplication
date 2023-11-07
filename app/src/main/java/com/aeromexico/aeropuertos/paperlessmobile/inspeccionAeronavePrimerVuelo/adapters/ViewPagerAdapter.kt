package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.CocinasFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.InspeccionOficialesFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.InspeccionPilotoFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.InspeccionSobrecargosFragment
import kotlin.reflect.KFunction0

class ViewPagerAdapter(
    fragementManager: FragmentManager,
    lifeCycle: Lifecycle,
    private var getBindingprimerVuelo: KFunction0<FragmentPrimerVueloDiaBinding>,
    private var getRequestBody: () -> RequestFirstFlightForm,
   var enviarForm:() -> Unit
) :
    FragmentStateAdapter(fragementManager, lifeCycle) {

    var inspeccionPilotoFragment: InspeccionPilotoFragment = InspeccionPilotoFragment(getBindingprimerVuelo, getRequestBody,enviarForm)
    var inspeccionSobrecargosFragment: InspeccionSobrecargosFragment = InspeccionSobrecargosFragment(getBindingprimerVuelo, getRequestBody,enviarForm)
    var inspeccionOficialesFragment: InspeccionOficialesFragment = InspeccionOficialesFragment(getBindingprimerVuelo, getRequestBody,enviarForm)
    var cocinasFragment: CocinasFragment = CocinasFragment(getBindingprimerVuelo, getRequestBody,enviarForm)

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> inspeccionPilotoFragment

            1 -> cocinasFragment

            2 -> inspeccionSobrecargosFragment

            3 -> inspeccionOficialesFragment

            else -> Fragment()
        }
    }
}