package com.aeromexico.aeropuertos.paperlessmobile.metar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentPdfBinding
import com.aeromexico.aeropuertos.paperlessmobile.metar.Model.PDFViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import org.jetbrains.anko.toast
import java.io.File

class PdfFragment : Fragment() {
    lateinit var binding:FragmentPdfBinding
    lateinit var d:Dialogo
    lateinit var model: PDFViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        d = Dialogo(requireContext())
        binding = FragmentPdfBinding.inflate(inflater, container, false)
        model = PDFViewModel(CoreRepository(WebServiceApi().getCoreApi()))

        model.responseState.observe(viewLifecycleOwner, Observer {
            if (it.state == RequestState.REQ_IN_PROGRESS){
                d.mostrarCargando(getString(R.string.cargando))
            } else{
                d.Ocultar()
            }
        })

        model.getPDFFile().observe(viewLifecycleOwner, Observer {
            if (it != null){
                var filePDF: File? = CreateImageFile.getPDFFIleFromB64Encode(it.result)
                if(filePDF != null){
                    binding.pdf.fromFile( filePDF)
                        .enableAnnotationRendering(true)
                        .defaultPage(0)
                        .swipeHorizontal(false)
                        .spacing(12)
                        .enableAntialiasing(true)
                        .load()
                }else
                    requireContext().toast("Error al Cargar PDF")
            }else{
                requireContext().toast("Error al Cargar PDF")

            }
        })

        return binding.root
    }

}