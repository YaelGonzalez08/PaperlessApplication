package com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInfoOtraCargaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.MensajesOperacionalesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc

class infoOtraCargaFragment : Fragment() {

    private lateinit var mBinding: FragmentInfoOtraCargaBinding
    private  val viewModel: NotocViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentInfoOtraCargaBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun obtieneInfoEnDBLocal(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            llenaVistaConDatos(it)
        }
    }

    private fun llenaVistaConDatos(notoc: RequestNotoc){

    }

}