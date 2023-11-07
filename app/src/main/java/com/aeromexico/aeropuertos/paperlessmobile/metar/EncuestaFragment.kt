package com.aeromexico.aeropuertos.paperlessmobile.metar

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentEncuestaBinding

class EncuestaFragment : Fragment() {
    lateinit var binding: FragmentEncuestaBinding

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.setMixedContentAllowed(false)
        binding.webview.loadUrl("https://forms.office.com/Pages/ResponsePage.aspx?id=u29ZJTxgskaa4aln4WKi_yzyi-0HjDFHifWqiCeyRO1UNDQ4M1UzMEQwUk9NSFNZM01XNEVGSzdDMiQlQCNjPTEu")
    }

    override fun onStop() {
        super.onStop()
        binding.webview.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEncuestaBinding.inflate(layoutInflater)

        binding.tvMensajesOperacionales.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }
}