package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInspeccionFormBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DetalleAveria.DetalleAveria
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DetalleAveria.DetalleAveriaFotosMultiples
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager.viewModel.PagerViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel


class InspeccionFormFragment : Fragment() {

    //ViewModels
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()
    private val inspeccionAeronaveViewModel: InspeccionAeronaveViewModel by activityViewModels()
    private val pagerViewModel: PagerViewModel by activityViewModels()

    private lateinit var mBinding: FragmentInspeccionFormBinding

    //Fragments
    val fragmentRadomo = DetalleAveria()

    val fragmentCargaDelantero = DetalleAveria()
    val fragmentCargaTrasero = DetalleAveria()
    val fragmentCargaBulk = DetalleAveria()

    val fragmentCompIntDelantero = DetalleAveria()
    val fragmentCompIntTrasero = DetalleAveria()
    val fragmentCompIntBulk = DetalleAveria()

    val fragmentSemialaIzq = DetalleAveria()
    val fragmentSemialaDer = DetalleAveria()

    val fragmentAguaPotable = DetalleAveria()
    val fragmentAguaNegra = DetalleAveria()
    val fragmentPuertaPrincipal = DetalleAveria()
    val fragmentServicioTrasera = DetalleAveria()

    val fragmentSepDosIn = DetalleAveria()
    val fragmentColocacionRedes = DetalleAveria()

    val fragmentOtro = DetalleAveriaFotosMultiples()

    //Text watcher para campo de nombre 'otro'
    protected var nombreOtroTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            inspeccionFormViewModel.setOtrosNombre(mBinding.etNombreOtro.text.toString())
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.mBinding = FragmentInspeccionFormBinding.inflate(inflater, container, false)

        this.setFragments()
        this.setCheckboxListeners()
        this.setListenersStepper()

        return this.mBinding.root
    }

    //Listeners de stepper
    fun setListenersStepper(){
        this.mBinding.btnSiguiente.setOnClickListener{

            var checkBoxesCorrectos = validateCheckboxes()
            var detallesCorrectos = validaDetallesFaltantes()
            var fotosMandatoriasCorrectas = validaFotosMandatorias()
            var llegadaSalidaCorrecto = validateLLagadaSalida()

            mBinding.tvErrorChecksFaltantes.visibility = if(checkBoxesCorrectos) View.GONE else View.VISIBLE
            mBinding.tvErrorInspeccion.visibility = if(detallesCorrectos) View.GONE else View.VISIBLE
            mBinding.tvErrorCompCarga.visibility = if(fotosMandatoriasCorrectas) View.GONE else View.VISIBLE
            mBinding.tvErrorLlegadaSalida.visibility = if(llegadaSalidaCorrecto) View.GONE else View.VISIBLE

            if(checkBoxesCorrectos && detallesCorrectos && fotosMandatoriasCorrectas && llegadaSalidaCorrecto){
                pagerViewModel.siguienteStep()
            }

        }
    }


    //Fragments de detalle del daño
    private fun setFragments(){
        val manager: FragmentManager = parentFragmentManager

        val transactionRadomo: FragmentTransaction = manager.beginTransaction()
        transactionRadomo.replace(mBinding.frameRadomo.id, fragmentRadomo).commit()
        this.fragmentRadomo.averia_seleccionada = Constants.TiposAveria.radomo.name

        val transactionCargaDelantero: FragmentTransaction = manager.beginTransaction()
        transactionCargaDelantero.replace(mBinding.frameCompCargaDelantero.id, fragmentCargaDelantero).commit()
        this.fragmentCargaDelantero.averia_seleccionada = Constants.TiposAveria.comp_carga_delantero.name

        val transactionCargaTrasero: FragmentTransaction = manager.beginTransaction()
        transactionCargaTrasero.replace(mBinding.frameCompCargaTrasero.id, fragmentCargaTrasero).commit()
        this.fragmentCargaTrasero.averia_seleccionada = Constants.TiposAveria.comp_carga_trasero.name

        val transactionCargaBulk: FragmentTransaction = manager.beginTransaction()
        transactionCargaBulk.replace(mBinding.frameCompCargaBulk.id, fragmentCargaBulk).commit()
        this.fragmentCargaBulk.averia_seleccionada = Constants.TiposAveria.comp_carga_bulk.name


        val transactionCompIntdelantero: FragmentTransaction = manager.beginTransaction()
        transactionCompIntdelantero.replace(mBinding.frameCompIntDelantero.id, fragmentCompIntDelantero).commit()
        this.fragmentCompIntDelantero.averia_seleccionada = Constants.TiposAveria.comp_int_delantero.name

        val transactionCompIntTrasero: FragmentTransaction = manager.beginTransaction()
        transactionCompIntTrasero.replace(mBinding.frameCompIntTrasero.id, fragmentCompIntTrasero).commit()
        this.fragmentCompIntTrasero.averia_seleccionada = Constants.TiposAveria.comp_int_trasero.name

        val transactionCompIntBulk: FragmentTransaction = manager.beginTransaction()
        transactionCompIntBulk.replace(mBinding.frameCompIntBulk.id, fragmentCompIntBulk).commit()
        this.fragmentCompIntBulk.averia_seleccionada = Constants.TiposAveria.comp_int_bulk.name


        val transactionSemialaIzq: FragmentTransaction = manager.beginTransaction()
        transactionSemialaIzq.replace(mBinding.frameSemialaIzq.id, fragmentSemialaIzq).commit()
        this.fragmentSemialaIzq.averia_seleccionada = Constants.TiposAveria.semiala_izq.name

        val transactionSemialaDer: FragmentTransaction = manager.beginTransaction()
        transactionSemialaDer.replace(mBinding.frameSemialaDer.id, fragmentSemialaDer).commit()
        this.fragmentSemialaDer.averia_seleccionada = Constants.TiposAveria.semiala_der.name


        val transactionAguaPotable: FragmentTransaction = manager.beginTransaction()
        transactionAguaPotable.replace(mBinding.frameAguaPotable.id, fragmentAguaPotable).commit()
        this.fragmentAguaPotable.averia_seleccionada = Constants.TiposAveria.agua_potable.name

        val transactionAguaNegra: FragmentTransaction = manager.beginTransaction()
        transactionAguaNegra.replace(mBinding.frameAguaNegra.id, fragmentAguaNegra).commit()
        this.fragmentAguaNegra.averia_seleccionada = Constants.TiposAveria.agua_negra.name

        val transactionPuertaPrincipal: FragmentTransaction = manager.beginTransaction()
        transactionPuertaPrincipal.replace(mBinding.framePuertaPrincipal.id, fragmentPuertaPrincipal).commit()
        this.fragmentPuertaPrincipal.averia_seleccionada = Constants.TiposAveria.puerta_principal.name


        val transactionServicioTrasera: FragmentTransaction = manager.beginTransaction()
        transactionServicioTrasera.replace(mBinding.frameServicioTrasera.id, fragmentServicioTrasera).commit()
        this.fragmentServicioTrasera.averia_seleccionada = Constants.TiposAveria.puerta_trasera.name


        val transactionSepDosIn: FragmentTransaction = manager.beginTransaction()
        transactionSepDosIn.replace(mBinding.frameSepDosIn.id, fragmentSepDosIn).commit()
        this.fragmentSepDosIn.averia_seleccionada = Constants.TiposAveria.sep_dos_pulg.name

        val transactionColocacionRedes: FragmentTransaction = manager.beginTransaction()
        transactionColocacionRedes.replace(mBinding.frameColocacionRedes.id, fragmentColocacionRedes).commit()
        this.fragmentColocacionRedes.averia_seleccionada = Constants.TiposAveria.colocacion_redes.name

        val transactionOtros: FragmentTransaction = manager.beginTransaction()
        transactionOtros.replace(mBinding.frameOtro.id, fragmentOtro).commit()
        this.fragmentOtro.averia_seleccionada = Constants.TiposAveria.otro.name

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Es WB o NB
        inspeccionAeronaveViewModel.getEsWideBody().observe(viewLifecycleOwner,{
            if(it){
                mBinding.tvCompCargaBulk.visibility = View.VISIBLE
                mBinding.cbCompCargaBulk.visibility = View.VISIBLE
                mBinding.detalleCompCargaBulk.visibility = View.VISIBLE
                mBinding.tvTieneAveriaCompCargaBulk.visibility = View.VISIBLE
                mBinding.cbNoAveriaCompCargaBulk.visibility = View.VISIBLE
                mBinding.tvSiCompCargaBulk.visibility = View.VISIBLE
                mBinding.tvNoCompCargaBulk.visibility = View.VISIBLE

                mBinding.tvCompIntBulk.visibility = View.VISIBLE
                mBinding.cbCompIntBulk.visibility = View.VISIBLE
                mBinding.detalleCompIntBulk.visibility = View.VISIBLE
                mBinding.tvTieneAveriaCompIntBulk.visibility = View.VISIBLE
                mBinding.cbNoAveriaCompIntBulk.visibility = View.VISIBLE
                mBinding.tvSiCompIntBulk.visibility = View.VISIBLE
                mBinding.tvNoCompIntBulk.visibility = View.VISIBLE
            }else{
                mBinding.tvCompCargaBulk.visibility = View.GONE
                mBinding.cbCompCargaBulk.visibility = View.GONE
                mBinding.detalleCompCargaBulk.visibility = View.GONE
                mBinding.tvTieneAveriaCompCargaBulk.visibility = View.GONE
                mBinding.cbNoAveriaCompCargaBulk.visibility = View.GONE
                mBinding.tvSiCompCargaBulk.visibility = View.GONE
                mBinding.tvNoCompCargaBulk.visibility = View.GONE

                mBinding.tvCompIntBulk.visibility = View.GONE
                mBinding.cbCompIntBulk.visibility = View.GONE
                mBinding.detalleCompIntBulk.visibility = View.GONE
                mBinding.tvTieneAveriaCompIntBulk.visibility = View.GONE
                mBinding.cbNoAveriaCompIntBulk.visibility = View.GONE
                mBinding.tvSiCompIntBulk.visibility = View.GONE
                mBinding.tvNoCompIntBulk.visibility = View.GONE
            }
        })

        //Mostrar / ocultar detalles de avería
        inspeccionFormViewModel.getOtrosTieneAveria().observe(viewLifecycleOwner, {
            if(it){
                this.mBinding.tilNombreOtro.visibility = View.VISIBLE
                mBinding.etNombreOtro.addTextChangedListener(nombreOtroTextWatcher)

            }else{
                this.mBinding.tilNombreOtro.visibility = View.GONE
                mBinding.etNombreOtro.removeTextChangedListener(nombreOtroTextWatcher)
            }
        })


        super.onViewCreated(view, savedInstanceState)
    }


    //Listeners para checkboxes (mostrar ocultar fragment de detalle)
    private fun setCheckboxListeners(){
        //Pernocta
        mBinding.switchPernocta.setOnClickListener{
            inspeccionFormViewModel.setEsPernocta(mBinding.switchPernocta.isChecked)
        }

        //Llegada o salida
        mBinding.cbLlegada.setOnClickListener{
            this.inspeccionFormViewModel.setEsLlegada(mBinding.cbLlegada.isChecked)
            if(mBinding.cbLlegada.isChecked){
                mBinding.cbSalida.isChecked = false

                mBinding.tvNotaLLegadaSalida.visibility = View.VISIBLE
                mBinding.tvNotaLLegadaSalida.text = getString(R.string.warning_llegada)
            }else{
                mBinding.tvNotaLLegadaSalida.visibility = View.GONE
            }
        }
        mBinding.cbSalida.setOnClickListener{
            if(mBinding.cbSalida.isChecked){
                mBinding.cbLlegada.isChecked = false
                this.inspeccionFormViewModel.setEsLlegada(false)
                mBinding.tvNotaLLegadaSalida.visibility = View.VISIBLE
                mBinding.tvNotaLLegadaSalida.text = getString(R.string.warning_salida)


            }else{
                mBinding.tvNotaLLegadaSalida.visibility = View.GONE

            }
        }

        //Averías
        mBinding.cbRadomo.setOnClickListener{
            this.inspeccionFormViewModel.setRadomoTieneAveria(mBinding.cbRadomo.isChecked)
           if(mBinding.cbRadomo.isChecked){
               mBinding.cbNoAveriaRadomo.isChecked = false
           }
        }
        mBinding.cbNoAveriaRadomo.setOnClickListener{
            if(mBinding.cbNoAveriaRadomo.isChecked){
                mBinding.cbRadomo.isChecked = false
                this.inspeccionFormViewModel.setRadomoTieneAveria(false)
            }
        }

        mBinding.cbCompCargaDelantero.setOnClickListener{
            this.inspeccionFormViewModel.setCompCargaDelanteroTieneAveria(mBinding.cbCompCargaDelantero.isChecked)
            if(mBinding.cbCompCargaDelantero.isChecked){
                mBinding.cbNoAveriaCompCargaDelantero.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompCargaDelantero.setOnClickListener{
            if(mBinding.cbNoAveriaCompCargaDelantero.isChecked){
                mBinding.cbCompCargaDelantero.isChecked = false
                this.inspeccionFormViewModel.setCompCargaDelanteroTieneAveria(false)
            }
        }
        mBinding.cbCompCargaTrasero.setOnClickListener{
            this.inspeccionFormViewModel.setCompCargaTraseroTieneAveria(mBinding.cbCompCargaTrasero.isChecked)
            if(mBinding.cbCompCargaTrasero.isChecked){
                mBinding.cbNoAveriaCompCargaTrasero.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompCargaTrasero.setOnClickListener{
            if(mBinding.cbNoAveriaCompCargaTrasero.isChecked){
                mBinding.cbCompCargaTrasero.isChecked = false
                this.inspeccionFormViewModel.setCompCargaTraseroTieneAveria(false)
            }
        }
        mBinding.cbCompCargaBulk.setOnClickListener{
            this.inspeccionFormViewModel.setCompCargaBulkTieneAveria(mBinding.cbCompCargaBulk.isChecked)
            if(mBinding.cbCompCargaBulk.isChecked){
                mBinding.cbNoAveriaCompCargaBulk.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompCargaBulk.setOnClickListener{
            if(mBinding.cbNoAveriaCompCargaBulk.isChecked){
                mBinding.cbCompCargaBulk.isChecked = false
                this.inspeccionFormViewModel.setCompCargaBulkTieneAveria(false)
            }
        }

        mBinding.cbCompIntDelantero.setOnClickListener{
            this.inspeccionFormViewModel.setCompIntDelanteroTieneAveria(mBinding.cbCompIntDelantero.isChecked)
            if(mBinding.cbCompIntDelantero.isChecked){
                mBinding.cbNoAveriaCompIntDelantero.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompIntDelantero.setOnClickListener{
            if(mBinding.cbNoAveriaCompIntDelantero.isChecked){
                mBinding.cbCompIntDelantero.isChecked = false
                this.inspeccionFormViewModel.setCompIntDelanteroTieneAveria(false)
            }
        }
        mBinding.cbCompIntTrasero.setOnClickListener{
            this.inspeccionFormViewModel.setCompIntTraseroTieneAveria(mBinding.cbCompIntTrasero.isChecked)
            if(mBinding.cbCompIntTrasero.isChecked){
                mBinding.cbNoAveriaCompIntTrasero.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompIntTrasero.setOnClickListener{
            if(mBinding.cbNoAveriaCompIntTrasero.isChecked){
                mBinding.cbCompIntTrasero.isChecked = false
                this.inspeccionFormViewModel.setCompIntTraseroTieneAveria(false)
            }
        }
        mBinding.cbCompIntBulk.setOnClickListener{
            this.inspeccionFormViewModel.setCompIntBulkTieneAveria(mBinding.cbCompIntBulk.isChecked)
            if(mBinding.cbCompIntBulk.isChecked){
                mBinding.cbNoAveriaCompIntBulk.isChecked = false
            }
        }
        mBinding.cbNoAveriaCompIntBulk.setOnClickListener{
            if(mBinding.cbNoAveriaCompIntBulk.isChecked){
                mBinding.cbCompIntBulk.isChecked = false
                this.inspeccionFormViewModel.setCompIntBulkTieneAveria(false)
            }
        }

        mBinding.cbSemialaIzq.setOnClickListener{
            this.inspeccionFormViewModel.setSemialaIzqTieneAveria(mBinding.cbSemialaIzq.isChecked)
            if(mBinding.cbSemialaIzq.isChecked){
                mBinding.cbNoAveriaSemialaIzq.isChecked = false
            }
        }
        mBinding.cbNoAveriaSemialaIzq.setOnClickListener{
            if(mBinding.cbNoAveriaSemialaIzq.isChecked){
                mBinding.cbSemialaIzq.isChecked = false
                this.inspeccionFormViewModel.setSemialaIzqTieneAveria(false)
            }
        }
        mBinding.cbSemialaDer.setOnClickListener{
            this.inspeccionFormViewModel.setSemialaDerTieneAveria(mBinding.cbSemialaDer.isChecked)
            if(mBinding.cbSemialaDer.isChecked){
                mBinding.cbNoAveriaSemialaDer.isChecked = false
            }
        }
        mBinding.cbNoAveriaSemialaDer.setOnClickListener{
            if(mBinding.cbNoAveriaSemialaDer.isChecked){
                mBinding.cbSemialaDer.isChecked = false
                this.inspeccionFormViewModel.setSemialaDerTieneAveria(false)
            }
        }

        mBinding.cbAguaPotable.setOnClickListener{
            this.inspeccionFormViewModel.setAguaPotableTieneAveria(mBinding.cbAguaPotable.isChecked)
            if(mBinding.cbAguaPotable.isChecked){
                mBinding.cbNoAveriaAguaPotable.isChecked = false
            }
        }
        mBinding.cbNoAveriaAguaPotable.setOnClickListener{
            if(mBinding.cbNoAveriaAguaPotable.isChecked){
                mBinding.cbAguaPotable.isChecked = false
                this.inspeccionFormViewModel.setAguaPotableTieneAveria(false)
            }
        }
        mBinding.cbAguaNegra.setOnClickListener{
            this.inspeccionFormViewModel.setAguaNegraTieneAveria(mBinding.cbAguaNegra.isChecked)
            if(mBinding.cbAguaNegra.isChecked){
                mBinding.cbNoAveriaAguaNegra.isChecked = false
            }
        }
        mBinding.cbNoAveriaAguaNegra.setOnClickListener{
            if(mBinding.cbNoAveriaAguaNegra.isChecked){
                mBinding.cbAguaNegra.isChecked = false
                this.inspeccionFormViewModel.setAguaNegraTieneAveria(false)
            }
        }
        mBinding.cbPuertaPrincipal.setOnClickListener{
            this.inspeccionFormViewModel.setPuertaPrincipalTieneAveria(mBinding.cbPuertaPrincipal.isChecked)
            if(mBinding.cbPuertaPrincipal.isChecked){
                mBinding.cbNoAveriaPuertaPrincipal.isChecked = false
            }
        }
        mBinding.cbNoAveriaPuertaPrincipal.setOnClickListener{
            if(mBinding.cbNoAveriaPuertaPrincipal.isChecked){
                mBinding.cbPuertaPrincipal.isChecked = false
                this.inspeccionFormViewModel.setPuertaPrincipalTieneAveria(false)
            }
        }
        mBinding.cbServicioTrasera.setOnClickListener{
            this.inspeccionFormViewModel.setServicioTraseraTieneAveria(mBinding.cbServicioTrasera.isChecked)
            if(mBinding.cbServicioTrasera.isChecked){
                mBinding.cbNoAveriaServicioTrasera.isChecked = false
            }
        }
        mBinding.cbNoAveriaServicioTrasera.setOnClickListener{
            if(mBinding.cbNoAveriaServicioTrasera.isChecked){
                mBinding.cbServicioTrasera.isChecked = false
                this.inspeccionFormViewModel.setServicioTraseraTieneAveria(false)
            }
        }

        mBinding.cbSepDosIn.setOnClickListener{
            this.inspeccionFormViewModel.setSepDosInTieneAveria(mBinding.cbSepDosIn.isChecked)
            if(mBinding.cbSepDosIn.isChecked){
                mBinding.cbNoAveriaSepDosIn.isChecked = false
            }
        }
        mBinding.cbNoAveriaSepDosIn.setOnClickListener{
            if(mBinding.cbNoAveriaSepDosIn.isChecked){
                mBinding.cbSepDosIn.isChecked = false
                this.inspeccionFormViewModel.setSepDosInTieneAveria(false)
            }
        }
        mBinding.cbColocacionRedes.setOnClickListener{
            this.inspeccionFormViewModel.setColocacionRedesTieneAveria(mBinding.cbColocacionRedes.isChecked)
            if(mBinding.cbColocacionRedes.isChecked){
                mBinding.cbNoAveriaColocacionRedes.isChecked = false
            }
        }
        mBinding.cbNoAveriaColocacionRedes.setOnClickListener{
            if(mBinding.cbNoAveriaColocacionRedes.isChecked){
                mBinding.cbColocacionRedes.isChecked = false
                this.inspeccionFormViewModel.setColocacionRedesTieneAveria(false)
            }
        }
        mBinding.cbOtros.setOnClickListener{
            this.inspeccionFormViewModel.setOtrosTieneAveria(mBinding.cbOtros.isChecked)
            if(mBinding.cbOtros.isChecked){
                mBinding.cbNoAveriaOtros.isChecked = false
            }
        }
        mBinding.cbNoAveriaOtros.setOnClickListener{
            if(mBinding.cbNoAveriaOtros.isChecked){
                mBinding.cbOtros.isChecked = false
                this.inspeccionFormViewModel.setOtrosTieneAveria(false)
            }
        }

    }

    //Validaciones del formulario
    private fun validaDetallesFaltantes(): Boolean{

        var correcto = false

        if(this.inspeccionFormViewModel.radomo_tieneAveria.value!!){
            if(inspeccionFormViewModel.radomo_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.radomo_descripcionAveria.value.isNullOrBlank() ){
             return correcto
            }
        }
        if(this.inspeccionFormViewModel.compCargaDelantero_tieneAveria.value!!){
            if(inspeccionFormViewModel.compCargaDelantero_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compCargaDelantero_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }

        if(this.inspeccionFormViewModel.compCargaTrasero_tieneAveria.value!!){
            if(inspeccionFormViewModel.compCargaTrasero_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compCargaTrasero_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.compCargaBulk_tieneAveria.value!!){
            if(inspeccionFormViewModel.compCargaBulk_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compCargaBulk_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }


        if(this.inspeccionFormViewModel.compIntDelantero_tieneAveria.value!!){
            if(inspeccionFormViewModel.compIntDelantero_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compIntDelantero_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.compIntTrasero_tieneAveria.value!!){
            if(inspeccionFormViewModel.compIntTrasero_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compIntTrasero_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.compIntBulk_tieneAveria.value!!){
            if(inspeccionFormViewModel.compIntBulk_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.compIntBulk_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }


        if(this.inspeccionFormViewModel.semialaIzq_tieneAveria.value!!){
            if(inspeccionFormViewModel.semialaIzq_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.semialaIzq_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.semialaDer_tieneAveria.value!!){
            if(inspeccionFormViewModel.semialaDer_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.semialaDer_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }


        if(this.inspeccionFormViewModel.aguaPotable_tieneAveria.value!!){
            if(inspeccionFormViewModel.aguaPotable_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.aguaPotable_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.aguaNegra_tieneAveria.value!!){
            if(inspeccionFormViewModel.aguaNegra_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.aguaNegra_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }

        if(this.inspeccionFormViewModel.puertaPrincipal_tieneAveria.value!!){
            if(inspeccionFormViewModel.puertaPrincipal_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.puertaPrincipal_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }
        if(this.inspeccionFormViewModel.servicioTrasera_tieneAveria.value!!){
            if(inspeccionFormViewModel.servicioTrasera_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.servicioTrasera_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }

        if(this.inspeccionFormViewModel.sepDosIn_tieneAveria.value!!){
            if(inspeccionFormViewModel.sepDosIn_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.sepDosIn_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }

        if(this.inspeccionFormViewModel.colocacionRedes_tieneAveria.value!!){
            if(inspeccionFormViewModel.colocacionRedes_ImgB64.value.isNullOrBlank() || inspeccionFormViewModel.colocacionRedes_descripcionAveria.value.isNullOrBlank() ){
                return correcto
            }
        }

        if(this.inspeccionFormViewModel.otros_tieneAveria.value!!){
            if(inspeccionFormViewModel.otros_imagenes.value!!.isEmpty() ){
                return correcto
            }
        }

        correcto = true
        return correcto
    }

    private fun validaFotosMandatorias(): Boolean{

        var correcto = false

        if(!inspeccionFormViewModel.radomo_tieneAveria.value!!
            && inspeccionFormViewModel.getRadomoMandatorio().value!!
            && inspeccionFormViewModel.radomo_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.compCargaDelantero_tieneAveria.value!!
            && inspeccionFormViewModel.getCompCargaDelanteroMandatorio().value!!
            && inspeccionFormViewModel.compCargaDelantero_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.compCargaTrasero_tieneAveria.value!!
            && inspeccionFormViewModel.getCompCargaTraseroMandatorio().value!!
            && inspeccionFormViewModel.compCargaTrasero_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(inspeccionAeronaveViewModel.getEsWideBody().value!!){
            if(!inspeccionFormViewModel.compCargaBulk_tieneAveria.value!!
                && inspeccionFormViewModel.getCompCargaBulkMandatorio().value!!
                && inspeccionFormViewModel.compCargaBulk_ImgB64.value.isNullOrBlank()){
                return correcto
            }
        }


        if(!inspeccionFormViewModel.compIntDelantero_tieneAveria.value!!
            && inspeccionFormViewModel.getCompIntDelanteroMandatorio().value!!
            && inspeccionFormViewModel.compIntDelantero_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.compIntTrasero_tieneAveria.value!!
            && inspeccionFormViewModel.getCompIntTraseroMandatorio().value!!
            && inspeccionFormViewModel.compIntTrasero_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(inspeccionAeronaveViewModel.getEsWideBody().value!!){
            if(!inspeccionFormViewModel.compIntBulk_tieneAveria.value!!
                && inspeccionFormViewModel.getCompIntBulkMandatorio().value!!
                && inspeccionFormViewModel.compIntBulk_ImgB64.value.isNullOrBlank()){
                return correcto
            }
        }


        if(!inspeccionFormViewModel.semialaIzq_tieneAveria.value!!
            && inspeccionFormViewModel.getSemialaIzqMandatorio().value!!
            && inspeccionFormViewModel.semialaIzq_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.semialaDer_tieneAveria.value!!
            && inspeccionFormViewModel.getSemialaDerMandatorio().value!!
            && inspeccionFormViewModel.semialaDer_ImgB64.value.isNullOrBlank()){
            return correcto
        }


        if(!inspeccionFormViewModel.aguaPotable_tieneAveria.value!!
            && inspeccionFormViewModel.getAguaPotableMandatorio().value!!
            && inspeccionFormViewModel.aguaPotable_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.aguaNegra_tieneAveria.value!!
            && inspeccionFormViewModel.getAguaNegraMandatorio().value!!
            && inspeccionFormViewModel.aguaNegra_ImgB64.value.isNullOrBlank()){
            return correcto
        }


        if(!inspeccionFormViewModel.puertaPrincipal_tieneAveria.value!!
            && inspeccionFormViewModel.getPuertaPrincipalMandatorio().value!!
            && inspeccionFormViewModel.puertaPrincipal_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.servicioTrasera_tieneAveria.value!!
            && inspeccionFormViewModel.getServicioTraseraMandatorio().value!!
            && inspeccionFormViewModel.servicioTrasera_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.sepDosIn_tieneAveria.value!!
            && inspeccionFormViewModel.getSepDosInMandatorio().value!!
            && inspeccionFormViewModel.sepDosIn_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.colocacionRedes_tieneAveria.value!!
            && inspeccionFormViewModel.getColocacionRedesMandatorio().value!!
            && inspeccionFormViewModel.colocacionRedes_ImgB64.value.isNullOrBlank()){
            return correcto
        }

        if(!inspeccionFormViewModel.otros_tieneAveria.value!!
            && inspeccionFormViewModel.getOtrosMandatorio().value!!
            && inspeccionFormViewModel.otros_imagenes.value!!.isEmpty()){
            return correcto
        }


        correcto = true
     return correcto
 }

    private fun validateCheckboxes(): Boolean{
     var correcto = false

     if(!mBinding.cbRadomo.isChecked && !mBinding.cbNoAveriaRadomo.isChecked) return correcto
     if(!mBinding.cbCompCargaDelantero.isChecked && !mBinding.cbNoAveriaCompCargaDelantero.isChecked) return correcto
     if(!mBinding.cbCompCargaTrasero.isChecked && !mBinding.cbNoAveriaCompCargaTrasero.isChecked) return correcto
     if(inspeccionAeronaveViewModel.getEsWideBody().value!!){
         if(!mBinding.cbCompCargaBulk.isChecked && !mBinding.cbNoAveriaCompCargaBulk.isChecked) return correcto
     }

     if(!mBinding.cbCompIntDelantero.isChecked && !mBinding.cbNoAveriaCompIntDelantero.isChecked) return correcto
     if(!mBinding.cbCompIntTrasero.isChecked && !mBinding.cbNoAveriaCompIntTrasero.isChecked) return correcto
     if(inspeccionAeronaveViewModel.getEsWideBody().value!!){
         if(!mBinding.cbCompIntBulk.isChecked && !mBinding.cbNoAveriaCompIntBulk.isChecked) return correcto
     }

     if(!mBinding.cbSemialaIzq.isChecked && !mBinding.cbNoAveriaSemialaIzq.isChecked) return correcto
     if(!mBinding.cbSemialaDer.isChecked && !mBinding.cbNoAveriaSemialaDer.isChecked) return correcto
     if(!mBinding.cbAguaPotable.isChecked && !mBinding.cbNoAveriaAguaPotable.isChecked) return correcto
     if(!mBinding.cbAguaNegra.isChecked && !mBinding.cbNoAveriaAguaNegra.isChecked) return correcto
     if(!mBinding.cbPuertaPrincipal.isChecked && !mBinding.cbNoAveriaPuertaPrincipal.isChecked) return correcto
     if(!mBinding.cbServicioTrasera.isChecked && !mBinding.cbNoAveriaServicioTrasera.isChecked) return correcto

     if(!mBinding.cbSepDosIn.isChecked && !mBinding.cbNoAveriaSepDosIn.isChecked) return correcto
     if(!mBinding.cbColocacionRedes.isChecked && !mBinding.cbNoAveriaColocacionRedes.isChecked) return correcto
     if(!mBinding.cbOtros.isChecked && !mBinding.cbNoAveriaOtros.isChecked) return correcto
     correcto = true
     return correcto
 }


    private fun validateLLagadaSalida(): Boolean{
        var correcto = false

       if(!mBinding.cbLlegada.isChecked && !mBinding.cbSalida.isChecked) return correcto

        correcto = true
        return correcto
    }




}