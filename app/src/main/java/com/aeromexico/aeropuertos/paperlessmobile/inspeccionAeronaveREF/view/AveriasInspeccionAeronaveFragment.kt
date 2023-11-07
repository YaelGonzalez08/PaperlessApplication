package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.view

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentAveriasInspeccionAeronaveBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel.InspeccionAeronaveViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.adapters.AveriaAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Imagenes
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class AveriasInspeccionAeronaveFragment(
    var vuelo: Vuelos?,
    var inspeccionAeronave: InspeccionAeronave,
    var toConcentiemto:(inspeccion: InspeccionAeronave) -> Unit
) : Fragment() {
    lateinit var model: InspeccionAeronaveViewModel
    lateinit var binding: FragmentAveriasInspeccionAeronaveBinding
    lateinit var averiasAdapter: AveriaAdapter
    private lateinit var userLogginData: Usuario
   private  var errorResponse = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAveriasInspeccionAeronaveBinding.inflate(layoutInflater)
        model = InspeccionAeronaveViewModel()
        averiasAdapter = AveriaAdapter(arrayListOf(), ::addPhoto,::viewPhoto,::addComentarioDanio)
        binding.averiasRecycler.apply {
            adapter = averiasAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        }
        MainActivity.getInstance()?.getUsuarioLogeado()?.let { userdata ->
            userLogginData = userdata
        }
        binding.apply {

            if (inspeccionAeronave.esLlegada == true) {
                cbLlegada.isChecked = true
                tvNotaLLegadaSalida.text = getString(R.string.warning_llegada)
            } else {
                cbSalida.isChecked = true
                tvNotaLLegadaSalida.text = getString(R.string.warning_salida)
            }
            if (inspeccionAeronave.pernocta == true) {
                switchPernocta.isChecked = true
            }
            btnGuardarLocal.setOnClickListener {
                saveForm()
            }
            btnEnviarForm.setOnClickListener {
                toConcentiemto.invoke(inspeccionAeronave)
            }

        }

        if (!inspeccionAeronave.averias.isNullOrEmpty()) {
            if (vuelo != null && vuelo!!.tipoCabina != null) {
                if (vuelo!!.tipoCabina.toLowerCase().contains("nb")) {
                    inspeccionAeronave.averias = inspeccionAeronave.averias!!.filter { av ->
                        !av.TipoAveria!!.contains("bulk")
                    } as ArrayList<Averia>

                }
            }
            averiasAdapter.updateList(inspeccionAeronave.averias!!)
        }
        model.responseError.observe(viewLifecycleOwner, Observer {
            it.let {
                errorResponse = it.toString()
            }
        })
        return binding.root
    }

    private fun isCompleteForm() {

        var evretingOK = true
        averiasAdapter.lista.forEach {
            if(it.tieneAveria == true ){
                if(it.imagenes.isNullOrEmpty() || it.descripcionAveria.isNullOrEmpty()){
                    evretingOK = false
                }
                it.imagenes?.forEach { objImagen ->
                    if(objImagen.imgB64.isNullOrEmpty()){
                        evretingOK = false
                    }
                }
            }
            if(it.EsObligatorio==true){
                if(it.imagenes.isNullOrEmpty()){
                    evretingOK = false
                }
                it.imagenes?.forEach { objImagen ->
                    if(objImagen.imgB64.isNullOrEmpty()){
                        evretingOK = false
                    }
                }
            }
            if(it.respNumerica == true ){
                if(it.respuestaAux.isNullOrEmpty()){
                    evretingOK = false
                }
            }
            if(it.descripcionAveria.toString().contains("N/A")) {
                it.naSelected = true
            }
        }
        var listavalidada = averiasAdapter.lista.filter {  it.tieneAveria == null && it.naSelected==false && it.respNumerica==false }
        if (listavalidada.isNotEmpty()) {
            //showToast("faltan insprecciones")
            binding.errorIncompleto.visibility = View.VISIBLE
            evretingOK = false
        }
        if(evretingOK){
            //showToast("No falta ninguna inpeccion")
            binding.errorIncompleto.visibility = View.GONE
            binding.errorFaltanFotos.visibility = View.GONE

            binding.btnEnviarForm.isVisible = true
        }else{
            binding.errorFaltanFotos.visibility = View.VISIBLE
        }

    }

    private fun saveForm() {
        inspeccionAeronave.averias = averiasAdapter.lista.toList() as ArrayList<Averia>
        inspeccionAeronave.modificadoPor = userLogginData.userGuid
        inspeccionAeronave.fechaModificacion = Fecha().parser.format(Date()).toString()
        inspeccionAeronave.enviadoPor = userLogginData.userGuid
        inspeccionAeronave.pernocta = binding.switchPernocta.isChecked

       var dial = Dialogo(requireContext())
        dial.mostrarCargando(null)
        model.saveInspeccion(inspeccionAeronave.copy()).observe(viewLifecycleOwner, Observer {
            //showToast(it.toString())
            dial.Ocultar()
            isCompleteForm()
            if (it != null) {
                if(!it.error){
                    //si todo bien preguntamos is ya eta compelto apra pasar a firmar
                    //aqui falta que cuando carge lo elimine cde local junto con las imagenes
                    model.deleteByguid(inspeccionAeronave.guidVuelo.toString(),inspeccionAeronave.esLlegada!!)
                }else{
                    dial.mostrarError("Error!",it.errorMessage)
                    dial.btnCerrar.setOnClickListener {
                        dial.Ocultar()
                    }
                }

            }else{
                // guardado local
                model.saveLocal(inspeccionAeronave)
                dial.mostrarAviso(  "Error ","${errorResponse?:""}\nEste formato se guardo de manera local")
                dial.btnCerrar.setOnClickListener {
                    dial.Ocultar()
                    activity?.onBackPressed()
                }
                dial.btnAceptar.setOnClickListener {
                    dial.Ocultar()
                    activity?.onBackPressed()
                }
            }
        })
    }
    fun viewPhoto(img: Bitmap){
        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(160, 160)
        imageView.setImageBitmap(img)

        MaterialAlertDialogBuilder(requireContext())
            .setView(imageView)
            .setPositiveButton(R.string.aceptar) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }
    private fun addComentarioDanio(averia: Averia, pos: Int,type:Int) {
        parentFragmentManager.let {
            AveriaCaptureDialogDialog(averia,type) { newAveria ->
                averiasAdapter.update(newAveria, pos)
            }.show(it, "AveriaCaptureDialogDialog")
        }
    }
    private fun addPhoto(averia: Averia, pos: Int,imgPreview:String?) {
        parentFragmentManager.let {
            MediaCameraPictureDialog(imgPreview) { foto ->
                if (averia?.TipoAveria?.contains("Otros") == true) {
                   if(imgPreview == null){
                       averia.imagenes?.add(Imagenes(CreateImageFile.getB64FromBitmap(foto)))
                   }
                }else{
                    averia.imagenes =
                        arrayListOf<Imagenes>(Imagenes(CreateImageFile.getB64FromBitmap(foto)))
                }
                averiasAdapter.update(averia, pos)
            }.show(it, "MediaCameraPictureDialog")
        }
    }


}