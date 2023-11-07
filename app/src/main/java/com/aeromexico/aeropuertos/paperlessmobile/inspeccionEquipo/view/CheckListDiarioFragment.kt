package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentCheckListDiarioBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.adapters.EquipoDiarioPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.PreguntasCheckListToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.repository.CheckListRepository
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.tabs.CheckListConsentimientoTab
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.viewModels.CheckListDiarioViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Equipo
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Preguntas
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResultEquipo
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.snackbar.Snackbar
import java.util.*

class CheckListDiarioFragment : Fragment() {
    lateinit var binding: FragmentCheckListDiarioBinding
    lateinit var dialogo: Dialogo
    lateinit var model: CheckListDiarioViewModel
    private lateinit var adapterPagerTabs: EquipoDiarioPagerAdapter
    private var mActivity: MainActivity? = null
    private val args: CheckListDiarioFragmentArgs by navArgs()
    var operador: UsuarioCore? = null
    var supervisor: UsuarioCore? = null
    var resultEquipo: ResultEquipo? = null
    var page: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckListDiarioBinding.inflate(inflater, container, false)
        setupActionBar()
        dialogo = Dialogo(requireContext())
        model = CheckListDiarioViewModel(CheckListRepository(WebServiceApi().getCheckListApi()))

        setBackButtonSystem(requireActivity())
        // rescatamos argumentos
        args.also {

            operador = it.operador
            supervisor = it.supervisor
            resultEquipo = it.resultEquipo

        }

        adapterPagerTabs = EquipoDiarioPagerAdapter(
            childFragmentManager,
            cargarDatos(),
            operador!!,
            supervisor!!,
            resultEquipo?.equipo!!
        )
        binding.apply {
            tabsContent.adapter = adapterPagerTabs
            //setup botones superiores de seleccionar el pager
            buttonRevision.textInCircle.text = "1"
            buttonConsentimiento.textInCircle.text = "2"
            buttonRevision.textNameButton.text  = getString(R.string.revision_equipo)
            buttonConsentimiento.textNameButton.text = getString(R.string.consentimiento)
            buttonRevision.root.setOnClickListener {
                setGoToTab(0)
            }
            btnRegresar.setOnClickListener {
                tabsContent.apply {
                    if (currentItem == 1)
                        currentItem = 0
                    else
                        findNavController().navigate(R.id.action_checkListDiarioFragment_to_escaneoCredencialesFragment)
                }
            }
            buttonConsentimiento.root.setOnClickListener {
                setGoToTab(1)
            }
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
                    setGoToTab(position)
                }
            })

            btnContinuar.setOnClickListener {
                if (page == 0) {
                    setGoToTab(1)
                } else if (page == 1) {
                    EnviarCheckToServer(
                        Fecha().calendarToString(Calendar.getInstance()),
                        adapterPagerTabs.equipoCheck.lista,
                        supervisor,
                        resultEquipo?.equipo!!,
                        operador
                    )

                } else if (page == 2) {
                    findNavController().popBackStack()
                    activity?.onBackPressed()
                }
            }
        }
        return binding.root
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.tabsContent.apply {
                    if (currentItem == 1)
                        currentItem = 0
                    else
                        findNavController().navigate(R.id.action_checkListDiarioFragment_to_escaneoCredencialesFragment)
                }
            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
    }

    private fun EnviarCheckToServer(
        fecha: String,
        lista: List<Preguntas>,
        supervisor: UsuarioCore?,
        equipo: Equipo?,
        operador: UsuarioCore?
    ) {
        dialogo.mostrarCargando(null)

        var lis: ArrayList<PreguntasCheckListToServer> = arrayListOf()

        lista.forEach {
            lis.add(
                PreguntasCheckListToServer(
                    id = it.idPregunta,
                    estate = it.estate!!,
                    observation = it.observation,
                    photo = it.photo
                )
            )
        }

        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            var datosCheck = CheckToServer(
                lista = lis,
                fecha = fecha,
                equipo = equipo,
                operador = operador,
                supervisor = supervisor,
                creadoPor = userdata.userGuid

            )

            model.SentCheckList(
                datosCheck
            ).observe(viewLifecycleOwner, { responseCheck ->
                dialogo.Ocultar()
                if (responseCheck != null) {
                    responseCheck.result.let {
                        binding.apply {
                            exitosoMessage.folio.text = getString(R.string.folio) + " ${it?.folio}"
                        }
                        setGoToTab(2)
                    }

                } else {

                    dialogo.mostrarError(getString(R.string.error_conexion), getString(R.string.error_checlist_to_bd))
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                        guardarCheckBD(
                            lista = lis,
                            fecha = fecha,
                            equipo = equipo,
                            operador = operador,
                            supervisor = supervisor,
                            creadoPor = userdata.userGuid
                        )
                    }

                }
            })
        }

    }

    private fun guardarCheckBD(
        fecha: String,
        lista: List<PreguntasCheckListToServer>,
        supervisor: UsuarioCore?,
        equipo: Equipo?,
        operador: UsuarioCore?,
        creadoPor: String
    ) {
        dialogo.mostrarCargando(null)

        var datosCheck = CheckToServer(
            fecha = fecha,
            equipo = equipo,
            operador = operador,
            supervisor = supervisor,
            creadoPor = creadoPor,
            lista = emptyList()
        )

        model.addCheckBD(datosCheck, lista)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                dialogo.Ocultar()
                Snackbar.make(binding.root, "code: $it", Snackbar.LENGTH_SHORT).show();
                findNavController().popBackStack()
                activity?.onBackPressed()
            })

    }

    private fun cargarDatos(): List<Preguntas> {

        resultEquipo?.equipo!!?.let {
            binding.apply {
                infoEquipo.txtLocationEstacion.text = it.idEstacion
                infoEquipo.txtNoEconomico.text = it.numeroActivo
                it.idCombustible.also { s ->
                    if (s.isNullOrEmpty()) {
                        infoEquipo.txtTipo.text = getString(R.string.no_motorizado)
                    } else {
                        infoEquipo.txtTipo.text = "Motorizado"
                    }
                }
            }
        }
        return resultEquipo?.preguntas!!
    }


    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.tittle_checklist_diario)

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.tabsContent.currentItem == 1)
                    binding.tabsContent.currentItem = 0
                else
                    findNavController().navigate(R.id.action_checkListDiarioFragment_to_escaneoCredencialesFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setGoToTab(i: Int) {
        page = i
        if (!adapterPagerTabs.equipoCheck.validateCheckList()) {
            page = 0
        }
        val params: ViewGroup.LayoutParams = binding.tabsContent.layoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT

        when (page) {
            0 -> {
                binding.apply {

                    params.height = 1500
                    tabsContent.layoutParams = params
                    btnContinuar.text = getString(R.string.siguiente)
                    btnContinuar.background = resources.getDrawable(R.drawable.background_azul_cuadrado_esquinasredondas)
                    buttonRevision.textInCircle.visibility = View.VISIBLE
                    buttonRevision.iconPalomita.visibility = View.GONE
                    buttonConsentimiento.textInCircle.visibility = View.VISIBLE
                    buttonConsentimiento.iconPalomita.visibility = View.GONE
                    tabsContent.currentItem = 0
                    tabsContent.visibility = View.VISIBLE
                    exitosoMessage.root.visibility = View.GONE
                }
            }
            1 -> {

                binding.apply {
                    params.height = 700
                    btnContinuar.text = getString(R.string.text_finalizar_btn)
                    btnContinuar.background = resources.getDrawable(R.drawable.background_rojo_cuadrado_esquinasredondas)
                    tabsContent.layoutParams = params
                    buttonRevision.textInCircle.visibility = View.GONE
                    buttonRevision.iconPalomita.visibility = View.VISIBLE
                    buttonConsentimiento.textInCircle.visibility = View.VISIBLE
                    buttonConsentimiento.iconPalomita.visibility = View.GONE
                    tabsContent.currentItem = 1
                    tabsContent.visibility = View.VISIBLE
                    exitosoMessage.root.visibility = View.GONE
                }
            }
            2 -> {
                binding.apply {
                    btnRegresar.visibility = View.GONE
                    buttonConsentimiento.textInCircle.visibility = View.GONE
                    buttonConsentimiento.iconPalomita.visibility = View.VISIBLE
                    btnContinuar.background = resources.getDrawable(R.drawable.green_background_rounded)
                    tabsContent.visibility = View.GONE
                    exitosoMessage.root.visibility = View.VISIBLE
                    buttonRevision.root.isEnabled = false
                    buttonConsentimiento.root.isEnabled = false
                    btnContinuar.text = "Aceptar"
                }
            }
        }
    }
}