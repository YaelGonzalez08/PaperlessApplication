package com.aeromexico.aeropuertos.paperlessmobile.metar

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentGAMeReportBinding
import im.delight.android.webview.AdvancedWebView
import okhttp3.internal.wait
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GAMeReportFragment : Fragment() , AdvancedWebView.Listener{
        lateinit var binding:FragmentGAMeReportBinding
        lateinit var dialog:Dialogo
    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

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
        binding = FragmentGAMeReportBinding.inflate(layoutInflater)
        dialog = Dialogo(requireContext())


        binding.tvMensajesOperacionales.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.webview.setListener(activity,this)

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.setMixedContentAllowed(true)
        binding.webview.loadUrl("https://gamereportweb.aeromexico.com/")

        return binding.root
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        dialog.mostrarCargando(null)    }

    override fun onPageFinished(url: String?) {
        dialog.Ocultar()
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        dialog.mostrarError("Error",description)

    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

}