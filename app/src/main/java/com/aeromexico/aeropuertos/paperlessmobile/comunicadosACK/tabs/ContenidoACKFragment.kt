package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs

import DetalleComunicado
import Documentos
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.MediaCameraPictureDialog
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters.FilesListAdapter
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.comunicadosViewModel
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.dialogs.ViewDocumentDialog
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository.ComunicadosRepository
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentContenidoACKBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import ng.softcom.android.utils.ui.showToast
import org.jetbrains.anko.toast
import java.io.File

class ContenidoACKFragment(var detalleComunicado: DetalleComunicado, var goToEncuesta: ()-> Unit) : Fragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {
    lateinit var binding: FragmentContenidoACKBinding
    lateinit var adapter: FilesListAdapter
    lateinit var model: comunicadosViewModel
    lateinit var dialog : Dialogo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContenidoACKBinding.inflate(layoutInflater)
        model = comunicadosViewModel(
            ComunicadosRepository(WebServiceApi().getComuncadisApi()),
            CoreRepository(WebServiceApi().getCoreApi())
        )
        dialog = Dialogo(requireContext())
        if (detalleComunicado.documentos != null) {
            adapter = FilesListAdapter(detalleComunicado.documentos!!,::downloadDocument)
            binding.recyclerFiles.adapter = adapter
        }
        binding.mbEnviar.setOnClickListener {
            goToEncuesta.invoke()
        }
        model.isLoading.observe(viewLifecycleOwner, Observer {
            if(it){
                dialog.mostrarCargando(getString(R.string.cargando))
            }else{
                dialog.Ocultar()
            }
        })
        return binding.root
    }

    private fun downloadDocument(documentos: Documentos) {
        model.getDownloadDocumentById(documentos.idArchivo).observe(this, Observer {
            if(it != null){
                val doc = it.result.detalleComunicado
                viewDocument(doc)
            }else{
                showToast("Error al Descargar documento")
            }
        })
    }

    private fun viewDocument(doc: Documentos) {
        // mandar aqui la peticion a la camara
        parentFragmentManager.let {
            ViewDocumentDialog(doc).show(it, "ViewDocumentDialog")
        }
    }
}