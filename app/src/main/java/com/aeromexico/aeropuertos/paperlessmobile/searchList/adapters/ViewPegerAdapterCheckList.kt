package com.aeromexico.aeropuertos.paperlessmobile.searchList.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aeromexico.aeropuertos.paperlessmobile.searchList.view.tabs.*

class ViewPegerAdapterCheckList(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val guid: String?,
    val lpd: Boolean
): FragmentStateAdapter(fragmentManager, lifecycle) {

    var inspeccionOOFragment: InspeccionOficialTabFragment = InspeccionOficialTabFragment(guid, lpd)
    var inspeccionPABFragment: InspeccionPabTabFragment = InspeccionPabTabFragment(guid, lpd)
    var inspeccionSOBFragment: InspeccionSobTabFragment = InspeccionSobTabFragment(guid, lpd)
    var inspeccionSegFragment: InspeccionSeguridadTabFragment = InspeccionSeguridadTabFragment(guid, lpd)
    var inspeccionCapitanFragment: InspeccionCapitantabFragment = InspeccionCapitantabFragment(guid, lpd)

    override fun getItemCount() = if(lpd) 5 else 4

    override fun createFragment(position: Int): Fragment {
        if (lpd) {
            return when (position) {
                0 -> inspeccionOOFragment

                1 -> inspeccionPABFragment

                2 -> inspeccionSOBFragment

                3 -> inspeccionSegFragment

                4 -> inspeccionCapitanFragment

                else -> Fragment()
            }
        } else {
            return when (position) {
                0 -> inspeccionOOFragment

                1 -> inspeccionSOBFragment

                2 -> inspeccionSegFragment

                3 -> inspeccionCapitanFragment

                else -> Fragment()
            }
        }
    }
}