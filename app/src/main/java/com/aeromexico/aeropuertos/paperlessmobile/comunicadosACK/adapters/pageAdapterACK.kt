package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import CuestionarioComunicado
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ResultPdfComunicado
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs.ConsentimientoACKFragment
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs.ContenidoACKFragment
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs.EncuestaACKFragment
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs.RetroAlimentacionFragment
import java.util.ArrayList

class pageAdapterACK(
    fm: FragmentManager,
    var result: ResultPdfComunicado,
    var newlist: (item: ArrayList<CuestionarioComunicado>) -> Unit,
    var request: (item: RequestEnvioCuesitonario) -> Unit,
    var respuestas: (item: ArrayList<Respuestas>) -> Unit,
    var goToEncuesta: ()-> Unit
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 4
    }

    var retroAlimentacionFragment =
        RetroAlimentacionFragment(result.detalleComunicado.cuestionarioComunicado, ::getFolio)
    var retro = ArrayList<CuestionarioComunicado>()
    var f =0

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ContenidoACKFragment(result.detalleComunicado,goToEncuesta)

            1 -> EncuestaACKFragment(result.detalleComunicado.cuestionarioComunicado, newlist,respuestas)

            2 -> ConsentimientoACKFragment(result.detalleComunicado,request)

            3 -> retroAlimentacionFragment

            else -> {
                ContenidoACKFragment(result.detalleComunicado,goToEncuesta)
            }

        }
    }

    fun goToConcent(arrayList: ArrayList<CuestionarioComunicado>) {
        retro = arrayList
    }

    fun goToSuccess(folio: Int) {
        f = folio
        retroAlimentacionFragment = RetroAlimentacionFragment(retro,::getFolio)
    }
    fun getFolio():Int{
        return f
    }

}