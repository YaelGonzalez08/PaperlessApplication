package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPrimerVueloDiaBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.adapters.ViewPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.*
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.CocinasFragment
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.tabs.llenarFromulario
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel.PrimerVueloDiaViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import ng.softcom.android.utils.ui.showSnackBar
import okhttp3.internal.wait

class PrimerVueloDiaFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentPrimerVueloDiaBinding
    lateinit var adapter: ViewPagerAdapter
    lateinit var model: PrimerVueloDiaViewModel
    lateinit var formsInDB: ArrayList<RequestFirstFlightForm>
    private var mActivity: MainActivity? = null
    val args:PrimerVueloDiaFragmentArgs by navArgs()
    private var percnotaSelected: Boolean = false
    private lateinit var form: RequestFirstFlightForm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupActionBar()
        setBackButtonSystem(requireActivity())
        binding = FragmentPrimerVueloDiaBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity
        model = PrimerVueloDiaViewModel()
        binding.btnUpdate.setOnClickListener {
            var d = Dialogo(requireContext())
            d.mostrarAviso("Atención!", "Se enviara la información actual (Si existe) antes de Actualizar")
            d.btnAceptar.setOnClickListener {
                d.Ocultar()
                getInfoDB()
            }
            d.btnCerrar.setOnClickListener {
                d.Ocultar()
                getInfoDB()
            }
        }
        getInfoDB()
//        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, ::getBindingPrimerVuelo, ::getRequestBody)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_title_first_flight)

        formsInDB = arrayListOf()

        return binding.root
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.buscar_un_vuelo)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(activity, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setTabLayOutIcons() {
        binding.apply {
            tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_number_one)
            tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_numero__two)
            tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_number_tree)
            tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_number_four)
        }
    }

    private fun setHeaders() {
        args.let {
            binding.apply {
                tvDateFlightValue.text = it.vuelo.fechaVueloLocal
                tvFlightNumberValue.text = it.vuelo.numVuelo.toString()
                tvEnrollmentValue.text = it.vuelo.matricula
                tvRouteValue.text = "${it.vuelo.origen}-${it.vuelo.destino}"
            }
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {
                R.id.percnota -> {
                    if (percnotaSelected) {
                        percnota.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_moon, null)
                        percnotaSelected = false
                    } else {
                        percnota.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_moon_blue, null)
                        percnotaSelected = true
                    }
                }
                R.id.pdf -> findNavController().navigate(R.id.action_primerVueloDiaFragment_to_pdfInstructionsFragment)

            }
        }
    }

    private fun validateForm() {
        val formsInDB2: ArrayList<RequestFirstFlightForm> = arrayListOf()
        var idTodelete = 0

        model.getAllForms().observeOnce{ flight ->
            flight.forEach{
                formsInDB2.add(Gson().fromJson(it.request, RequestFirstFlightForm::class.java))

                if(Gson().fromJson(it.request, RequestFirstFlightForm::class.java).flightReferenceNumber == args.vuelo.flightReferenceNumber) {
                    idTodelete = it.id
                }
            }
            var re = RequestFirstFlightForm(args.vuelo.flightReferenceNumber)

            formsInDB2.forEach{
                if (it.flightReferenceNumber == args.vuelo.flightReferenceNumber){
                    re = it
                    return@forEach
                }
            }

            re.apply {
                Log.i("Enviando preguntas", this.preguntas.toString())
                if (this.piloto.nombre != "" || this.sobrecargo.nombre != "" || this.oficial.nombre != "" || this.cocinas.nombre != "") {
                    this.pernocta = percnotaSelected
                    var diag = Dialogo(requireContext())
                    diag.btnCerrar.setOnClickListener{startActivity(Intent(activity, MainActivity::class.java))}
                    diag.mostrarCargando("Enviando formulario")

                    model.insertPrimerVuelo(this).observeOnce{response ->
                        diag.apply {
                            btnCerrar.visibility = View.GONE
                            btnAceptar.visibility = View.VISIBLE
                            btnAceptar.setOnClickListener(this@PrimerVueloDiaFragment)
                        }
                        diag.Ocultar()
                        if(response != null) {

                            if(response.error)
                                diag.mostrarError("Error al enviar formulario", "${response.errorMessage}")

                            model.deletePtimerVueloCheckById(idTodelete)
                            val action = PrimerVueloDiaFragmentDirections.actionPrimerVueloDiaFragmentToSucces(response)
                            findNavController().navigate(action)

                        } else {
                            diag.mostrarError("Sin conexión", "Verifica tu enlace a internet, el formulario se guardara de manera local.")
                        }
                    }
                } else {
                    val diag2 = Dialogo(requireContext())
                    diag2.apply {
                        diag2.btnCerrar.setOnClickListener{diag2.Ocultar()}
                        mostrarError(
                            "Formulario incompleto",
                            "Verifica que cada formulario este debidamente completo y guardado antes de enviar."
                        )
                    }
                }
            }
        }
    }

    private fun validateFlightNumber():RequestFirstFlightForm{

        model.getAllForms().observe(viewLifecycleOwner, { flight ->
            flight.forEach{
                formsInDB.add(Gson().fromJson(it.request, RequestFirstFlightForm::class.java))
            }
        })
        var re = RequestFirstFlightForm(args.vuelo.flightReferenceNumber)
        Log.i("form preguntas",form.preguntas.toString())
        formsInDB.forEach{
            if (it.flightReferenceNumber == args.vuelo.flightReferenceNumber){
                re = it
                Log.i("Preguntas en base local",re.preguntas.toString())

                return@forEach
            }
        }
        if(form.piloto.numEmpleado !="" || form.cocinas.numEmpleado !="" || form.sobrecargo.numEmpleado !="" || form.oficial.numEmpleado !="") {
            re = form
            if(form.piloto.numEmpleado != "" ){
                re.piloto = form.piloto
                for (pregunta in re.preguntas) {
                    if (pregunta.idpregunta <= 5)
                        pregunta.condicion =
                            form.preguntas.find { pregu -> pregunta.idpregunta == pregu.idpregunta }!!.condicion
                }
            }


            if (form.cocinas.nombre.isNotEmpty()) {
                re.cocinas = form.cocinas
                for (pregunta in re.preguntas) {
                    if (pregunta.idpregunta in 5..8)
                        pregunta.condicion =
                            form.preguntas.find { pregu -> pregunta.idpregunta == pregu.idpregunta }!!.condicion
                }
            }

            if (form.sobrecargo.nombre.isNotEmpty()) {
                re.sobrecargo = form.sobrecargo
                for (pregunta in re.preguntas) {
                    if (pregunta.idpregunta in 9..25)
                        pregunta.condicion =
                            form.preguntas.find { pregu -> pregunta.idpregunta == pregu.idpregunta }!!.condicion
                }
            }

            if (form.oficial.nombre.isNotEmpty()) {
                re.sobrecargo = form.sobrecargo
                for (pregunta in re.preguntas) {
                    if (pregunta.idpregunta >= 26 && pregunta.idpregunta < 29)
                        pregunta.condicion =
                            form.preguntas.find { pregu -> pregunta.idpregunta == pregu.idpregunta }!!.condicion
                }
            }

        }
        return re
    }

    private fun getBindingPrimerVuelo(): FragmentPrimerVueloDiaBinding {
        return binding
    }

    private fun getRequestBody(): RequestFirstFlightForm {
        return validateFlightNumber()
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
    }

    private fun getInfoDB() {
        var dialogo = Dialogo(requireContext())
        model.responseState.observe(viewLifecycleOwner,{
            if (it.state == RequestState.REQ_IN_PROGRESS){
                dialogo.mostrarCargando(getString(R.string.cargando))
                /*Snackbar.make(mBinding.root, "State en progreso ${it.state}", Snackbar.LENGTH_SHORT)
                        .show()*/
            }
            else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK){
                dialogo.Ocultar()
                if(it.state == RequestState.REQ_BAD) {
                    dialogo.mostrarAviso("No se pudo obtener información para este vuelo",
                        getString(R.string.verifique_su_conexion_e_intente_de_nuevo))
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                    incializaAdapter()
                }
                Snackbar.make(binding.root, "State ${it.state}", Snackbar.LENGTH_SHORT)
                    .show()

            }

        })
        form = RequestFirstFlightForm(args.vuelo.flightReferenceNumber)
        model.getInfoDB(args.vuelo.flightReferenceNumber).observe(viewLifecycleOwner,{

            if(it != null) {
                if(it.result != null) {
                    form.preguntas = it.result.item3.preguntas
                    form.piloto = it.result.item3.piloto
                    form.cocinas = it.result.item3.cocinas
                    form.sobrecargo = it.result.item3.sobrecargo
                    form.oficial = it.result.item3.oficial
                }
            }
            incializaAdapter()
        })

    }

    private fun incializaAdapter() {
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, ::getBindingPrimerVuelo, ::getRequestBody,::validateForm)
        binding.apply {
            vpFormFirstFlight.adapter = adapter

            TabLayoutMediator(tabLayout, vpFormFirstFlight) { tab, position ->
                when (position) {
                    0 -> tab.text = "CockPit"
                    1 -> tab.text = "Cocina"
                    2 -> tab.text = "Interno"
                    3 -> tab.text = "Externo"
                }
            }.attach()

            setHeaders()
            setTabLayOutIcons()
            percnota.setOnClickListener(this@PrimerVueloDiaFragment)
            pdf.setOnClickListener(this@PrimerVueloDiaFragment)
            vpFormFirstFlight.registerOnPageChangeCallback( object: ViewPager2.OnPageChangeCallback() {})
        }
    }
}