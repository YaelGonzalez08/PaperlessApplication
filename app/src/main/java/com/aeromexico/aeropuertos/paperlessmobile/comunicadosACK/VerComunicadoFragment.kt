package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK

import CuestionarioComunicado
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters.pageAdapterACK
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository.ComunicadosRepository
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentVerComunicadoBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

class VerComunicadoFragment : Fragment() {

    private var asSend: Boolean = false
    private var encuestaCompelta: Boolean = false
    private var isCompleted: Boolean = false
    private lateinit var respuestas: ArrayList<Respuestas>
    lateinit var binding: FragmentVerComunicadoBinding
    lateinit var pageadapter: pageAdapterACK
    private val args: VerComunicadoFragmentArgs by navArgs()
    private var mActivity: MainActivity? = null
    lateinit var model: comunicadosViewModel
    lateinit var dialogo: Dialogo
    var idCominicado: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerComunicadoBinding.inflate(layoutInflater)

        dialogo = Dialogo(requireContext())
        model = comunicadosViewModel(ComunicadosRepository(WebServiceApi().getComuncadisApi()),
            CoreRepository(WebServiceApi().getCoreApi())
        )
        observers()
        setupActionBar()
        binding.apply{
            btnContenido.textInCircle.text = "1"
            btnContenido.textNameButton.text = "Contenido"

            btnCondbtnEncuesta.textInCircle.text = "2"
            btnCondbtnEncuesta.textNameButton.text = "Encuesta"

            btnConcentimiento.textInCircle.text = "3"
            btnConcentimiento.textNameButton.text = "Concentimiento"

            tabsContent.setOnTouchListener(View.OnTouchListener { v, event ->
                tabsContent.parent.requestDisallowInterceptTouchEvent(false)
                false
            })

            tabsContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    binding.root.hideKeyboard()
                    setGoToTab(position)
                }
            })
        }

        return binding.root
    }

    private fun observers() {
        dialogo.mostrarCargando("Cargando")

        model.getComunicadoById(args.comunicadoId).observe(viewLifecycleOwner, Observer {
            dialogo.Ocultar()

            if (it != null) {

                pageadapter = it?.result?.let { it1 ->
                    idCominicado = args.comunicadoId
                    pageAdapterACK(
                        childFragmentManager,
                        it1, ::goToConcentimiento, ::sendCuestionario, ::RespuestasCuestionario,::showEncuesta
                    )
                }!!
                binding.apply {
                    tabsContent.adapter = pageadapter


                }
            } else {
                dialogo.mostrarError("Comunicado No disponible", it?.message)
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()
                    activity?.onBackPressed()
                }
            }


        })
    }

    private fun showEncuesta() {
        setGoToTab(1)
    }

    private fun setGoToTab( page: Int) {
        var pagina = page

        if(isCompleted)
            pagina = 3

        if(page == 2 && !encuestaCompelta)
            pagina = 1

        if (page == 3 && !asSend)
            pagina = 2

        when (pagina) {

            0 -> {
                //ppdf
                binding.apply {

                    tabsContent.currentItem = 0
                    tabsContent.visibility = View.VISIBLE

                    btnContenido.textInCircle.visibility = View.VISIBLE
                    btnContenido.iconPalomita.visibility = View.GONE

                    btnCondbtnEncuesta.textInCircle.visibility = View.VISIBLE
                    btnCondbtnEncuesta.iconPalomita.visibility = View.GONE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                }
            }
            1 -> {
                //encuesta

                binding.apply {

                    tabsContent.currentItem = 1
                    tabsContent.visibility = View.VISIBLE

                    btnContenido.textInCircle.visibility = View.GONE
                    btnContenido.iconPalomita.visibility = View.VISIBLE

                    btnCondbtnEncuesta.textInCircle.visibility = View.VISIBLE
                    btnCondbtnEncuesta.iconPalomita.visibility = View.GONE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE
                }
            }
            2 -> {
                //concentimiento
                binding.apply {

                    tabsContent.currentItem = 2
                    tabsContent.visibility = View.VISIBLE

                    btnContenido.textInCircle.visibility = View.GONE
                    btnContenido.iconPalomita.visibility = View.VISIBLE

                    btnCondbtnEncuesta.textInCircle.visibility = View.GONE
                    btnCondbtnEncuesta.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.VISIBLE
                    btnConcentimiento.iconPalomita.visibility = View.GONE

                }
            }
            3 -> {
                // success
                binding.apply {
                    tabsContent.currentItem = 3
                    tabsContent.visibility = View.VISIBLE

                    btnContenido.textInCircle.visibility = View.GONE
                    btnContenido.iconPalomita.visibility = View.VISIBLE

                    btnCondbtnEncuesta.textInCircle.visibility = View.GONE
                    btnCondbtnEncuesta.iconPalomita.visibility = View.VISIBLE

                    btnConcentimiento.textInCircle.visibility = View.GONE
                    btnConcentimiento.iconPalomita.visibility = View.VISIBLE
                    tabs.isEnabled = false
                    isCompleted = true
                }
            }
        }
    }

    private fun RespuestasCuestionario(respuestasCuestionario: ArrayList<Respuestas>) {
        respuestas = respuestasCuestionario
    }

    private fun sendCuestionario(requestEnvioCuesitonario: RequestEnvioCuesitonario) {
        requestEnvioCuesitonario.idComunicado = idCominicado
        requestEnvioCuesitonario.respuestas = respuestas

        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            requestEnvioCuesitonario.creadoPor = userdata.userGuid
        }
        enviartoServer(requestEnvioCuesitonario)

    }

    private fun enviartoServer(requestEnvioCuesitonario: RequestEnvioCuesitonario) {

        model.sendComunicadoResuelto(requestEnvioCuesitonario).observe(viewLifecycleOwner, Observer {
            if(it != null){
                it?.result?.comunicadoContestado?.idComunicadoFinalizado?.let { it1 ->
                    pageadapter.goToSuccess(it1)
                    asSend = true
                    setGoToTab(3)
                }

            }
            it?.errorMessage?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToConcentimiento(arrayList: ArrayList<CuestionarioComunicado>) {
        pageadapter.goToConcent(arrayList)
        encuestaCompelta = true
        setGoToTab(2)
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Comunicado"
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}