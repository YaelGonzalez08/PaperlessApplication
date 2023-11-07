package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.tabs

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentEquipoCheckListTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.adapters.ListaToCheckDiarioAdapter
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Preguntas
import com.google.android.material.snackbar.Snackbar

class EquipoCheckListTab(var lista: List<Preguntas>) : Fragment() {
    lateinit var binding: FragmentEquipoCheckListTabBinding
    lateinit var adapter: ListaToCheckDiarioAdapter
    var fechavencimientoExtintor: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipoCheckListTabBinding.inflate(inflater, container, false)

        adapter = ListaToCheckDiarioAdapter(lista, ::onclicAddPhotoHelper, ::onAddDateHelper)
        binding.recyclerChecklist.adapter = adapter


        return binding.root
    }

    private fun onAddDateHelper(item: Preguntas, position: Int) {
        parentFragmentManager.let { it1 ->
            DatePickerDialogHelper(
                oneLastYear = true,
                threDaysAvailable = false
            ) { day, month, year ->
                if (adapter.lista[position].observation.toString().contains("#")) {
                    adapter.lista[position].observation = "${changeFormatDate(day, month, year)}#${
                        item.observation.toString().substringAfter("#")
                    }"
                } else {
                    if (item.observation.toString().isNullOrEmpty()) {
                        adapter.lista[position].observation =
                            "${changeFormatDate(day, month, year)}#"
                    } else {
                        adapter.lista[position].observation =
                            "${changeFormatDate(day, month, year)}#${item.observation.toString()}"
                    }
                }
                fechavencimientoExtintor = changeFormatDate(day, month, year)
                adapter.addPhotoInItem(adapter.lista)
            }.show(it1, "DateDialog")
        }

    }

    fun onclicAddPhotoHelper(item: Preguntas, position: Int) {
        // mandar aqui la peticion a la camara
        parentFragmentManager.let {
            MediaCameraPictureDialog() { foto ->
                // //remplazar adapter por bitmap o b64
                adapter.lista[position].photo = CreateImageFile.getB64FromBitmap(foto)
                adapter.addPhotoInItem(adapter.lista)
            }.show(it, "MediaCameraPictureDialog")
        }

    }

    fun validateCheckList(): Boolean {
        var isAllResponsed = true
        adapter.lista.let {
            for (item: Preguntas in it) {
                if (!(item.estate != 0 && item.estate != null)) {
                    isAllResponsed = false
                    val section =
                        if (item.titulo == "") "CONDICIONES DE SEGURIDAD: " else item.titulo
                    Snackbar.make(
                        binding.root,
                        "${getString(R.string.revisa_todos_los_estados)}$section",
                        Snackbar.LENGTH_SHORT
                    ).show();
                    return@let
                }
                if (item.titulo == "EXTINTOR") {
                    if (fechavencimientoExtintor.isNullOrEmpty()) {
                        isAllResponsed = false
                        Snackbar.make(
                            binding.root,
                            "Falta Fecha de vencimiento en Extintor",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        return@let
                    }
                }
            }

        }

        return isAllResponsed
    }
}