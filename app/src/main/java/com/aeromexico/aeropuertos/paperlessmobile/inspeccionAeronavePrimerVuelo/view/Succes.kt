package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentSuccesBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity


class Succes : Fragment(), View.OnClickListener{
    lateinit var binding: FragmentSuccesBinding
    lateinit var mActivity: MainActivity
    val args:SuccesArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccesBinding.inflate(layoutInflater, container, false)
        mActivity = activity as MainActivity
        mActivity.supportActionBar?.hide()
        setBackButtonSystem(requireActivity())

        binding.apply {
            btnAceptar.setOnClickListener(this@Succes)

            succesMessage.folio.text = "Folio: ${args.response.result.primerVuelo.idPrimerVuelo}"

            succesMessage.imagenCentral.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_inspeccion_aeronave, null)

            succesMessage.textMensaje.text = "Reporte del primer vuelo del día se ha enviado correctamente."
        }
        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v?.id) {
                btnAceptar.id -> toHome()
            }
        }
    }

    private fun toHome() {
        startActivity(Intent(activity, MainActivity::class.java))
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
    }
}