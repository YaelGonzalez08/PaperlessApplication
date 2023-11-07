package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Finalizacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentConsentimientoFormBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentFinalizacionInspeccionAeronaveBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel.ConsentimientoFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager.viewModel.PagerViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel


class FinalizacionInspeccionAeronaveFragment : Fragment() {

    //Binding
    private lateinit var mBinding: FragmentFinalizacionInspeccionAeronaveBinding;

    //ViewModel
    private val inspeccionAeronaveViewModel: InspeccionAeronaveViewModel by activityViewModels()
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()
    private val consentimientoFormViewModel: ConsentimientoFormViewModel by activityViewModels()
    private val pagerViewModel: PagerViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        inspeccionAeronaveViewModel.getIdInspeccionCreada().observe(viewLifecycleOwner, {
            mBinding.pantallaFinalizacion.folio.text = getString(R.string.folio) + it
        })


        mBinding.btnContinuar.setOnClickListener{
            reiniciarViewModels()
            findNavController().popBackStack()
            activity?.onBackPressed()
        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFinalizacionInspeccionAeronaveBinding.inflate(inflater, container, false);

        mBinding.pantallaFinalizacion.textMensaje.text = getString(R.string.msg_exito_inspeccion_aeronave);

        return mBinding.root;
    }


    private fun reiniciarViewModels(){
        inspeccionAeronaveViewModel.reiniciarVM()
        inspeccionFormViewModel.reiniciarVM()
        consentimientoFormViewModel.reiniciarVM()
        pagerViewModel.reiniciarVM()
    }
 
}