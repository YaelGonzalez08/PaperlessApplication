package com.aeromexico.aeropuertos.paperlessmobile.searchList.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentMessageLPDBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view.InformacionManualVueloFragmentDirections
import com.aeromexico.aeropuertos.paperlessmobile.searchList.viewModel.SerarchListViewModel

class MessageLPDFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentMessageLPDBinding
    lateinit var modelSearch: SerarchListViewModel
    private val args: MessageLPDFragmentArgs by navArgs()
    lateinit var diag: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageLPDBinding.inflate(inflater, container, false)
        diag = Dialogo(requireContext())
        diag.mostrarCargando("Verificando datos previamente enviados")
        modelSearch = SerarchListViewModel()

        binding.apply {
            modelSearch.getInfo(args.vuelo!!.guid)

            modelSearch.responseState.observe(viewLifecycleOwner, { response ->
                diag.Ocultar()
                binding.cover.visibility = View.GONE
            })

            modelSearch.responseGetInfo.observe(viewLifecycleOwner, { response ->
                if(response.result.searchList.info) {
                    val action = MessageLPDFragmentDirections.actionMessageLPDFragmentToSearchListFragment(args.vuelo, response.result.searchList.lPD)
                    findNavController().navigate(action)
                } else
                    binding.cover.visibility = View.GONE
            })

            btnNo.setOnClickListener(this@MessageLPDFragment)
            btnSi.setOnClickListener(this@MessageLPDFragment)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when(v?.id){

                btnSi.id -> findNavController().navigate(MessageLPDFragmentDirections.actionMessageLPDFragmentToSearchListFragment(args.vuelo, true))
                btnNo.id -> findNavController().navigate(MessageLPDFragmentDirections.actionMessageLPDFragmentToSearchListFragment(args.vuelo, false))
            }
        }
    }
}