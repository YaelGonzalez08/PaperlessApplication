package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.adapters

import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.AveriaCaptureDialogDialog
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemAveriaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemFotoInspecAeronaveBinding
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.TiposAveriaResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AveriaViewHolder(
    var binding: ItemAveriaBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): AveriaViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemAveriaBinding.inflate(layoutInflater, parent, false)
            return AveriaViewHolder(itemBinding)
        }
    }

    fun bind(
        item: Averia,
        position: Int,
        update: (Averia, Int) -> Unit,
        addOPhoto: (Averia, Int, String?) -> Unit,
        updateSilence: (Averia, Int) -> Unit,
        viewPhoto: (Bitmap) -> Unit,
        addDanio:(Averia,Int,Int)-> Unit
    ) {

        binding.apply {
            titOnlyNumber.visibility = View.GONE
            titDetalle.visibility = View.GONE
            titArea.visibility = View.GONE

            checkFalse.visibility = View.VISIBLE
            checkTrue.visibility = View.VISIBLE
            tvTieneAveriaPuertaPrincipal.visibility = View.VISIBLE
            labelSI.visibility = View.VISIBLE
            tvNoPuertaPrincipal.visibility = View.VISIBLE

            checkTrue.setOnClickListener {
                item.tieneAveria = true
                checkFalse.isChecked = false
                item.descripcionAveria = ""
                item.naSelected = false
                update.invoke(item, position)

            }
            checkFalse.setOnClickListener {
                item.tieneAveria = false
                checkTrue.isChecked = false
                item.descripcionAveria = ""
                item.naSelected = false
                update.invoke(item, position)

            }

            replacePhoto.setOnClickListener {
                addOPhoto.invoke(item, position, item.imagenes?.firstOrNull()?.imgB64 ?: null)

            }

            if (item.EsObligatorio == true) {
                tvImagenMandatoria.visibility = View.VISIBLE
            } else {
                tvImagenMandatoria.visibility = View.GONE
            }


            txtAveria.text = item.TipoAveria
            if (item.Codigo?.contains("sep_dos_pulg") == true
                || item.Codigo?.contains("straps") == true
                || item.Codigo?.contains("gpu_cmb_jet") == true
                || item.Codigo?.contains("avih") == true
                || item.Codigo?.contains("segre_prio_cnx_turi") == true
                || item.Codigo?.contains("ldm") == true
            ) {
                tvTieneAveriaPuertaPrincipal.visibility = View.INVISIBLE
            } else {
                tvTieneAveriaPuertaPrincipal.visibility = View.VISIBLE
            }

            if (item.tieneAveria != null) {
                tvNA.visibility = View.GONE
                checkNa.visibility = View.GONE

                item.tieneAveria.let {
                    if (it == true) {
                        checkTrue.isChecked = true
                        checkFalse.isChecked = false
                        checkNa.isChecked = false
                        titDetalle.visibility = View.VISIBLE
                        if (item?.TipoAveria?.contains("Otros") == false) {
                            txtDetalle.setText(item.descripcionAveria)
                        }
                    } else {
                        checkFalse.isChecked = true
                        checkTrue.isChecked = false
                        checkNa.isChecked = false
                        titDetalle.visibility = View.GONE
                        titArea.visibility = View.GONE
                    }
                }
            }

            if (item.respuestaAux?.contains("N/A") == true) {
                if (item.descripcionAveria?.contains("N/A") == true) {
                    checkNa.isChecked = true
                }
                tvNA.visibility = View.VISIBLE
                checkNa.visibility = View.VISIBLE
                checkNa.setOnClickListener {
                    item.tieneAveria = null
                    checkTrue.isChecked = false
                    checkFalse.isChecked = false
                    item.descripcionAveria = "N/A"
                    item.naSelected = true
                    updateSilence.invoke(item, position)
                    titDetalle.visibility = View.GONE

                }

            }
            if (item.respNumerica == true) {
                titOnlyNumber.visibility = View.VISIBLE

                checkFalse.visibility = View.GONE
                checkTrue.visibility = View.GONE
                tvTieneAveriaPuertaPrincipal.visibility = View.GONE
                labelSI.visibility = View.GONE
                tvNoPuertaPrincipal.visibility = View.GONE
                tvNA.visibility = View.GONE
                checkNa.visibility = View.GONE

                if (!item.respuestaAux.isNullOrEmpty()) {
                    txtNumber.setText(item.respuestaAux?.toString())
                }
                txtNumber.setOnClickListener {
                    addDanio.invoke(item,position, AveriaCaptureDialogDialog.TIPO_NUMERICO)
                }

            }
            //logica fotos
            if (item?.TipoAveria?.contains("Otros") == true) {
                replacePhoto.visibility = View.GONE
                titOnlyNumber.visibility = View.GONE
                item.tieneAveria?.let {
                    if (it) {
                        titDetalle.visibility = View.VISIBLE
                        titArea.visibility = View.VISIBLE
                    } else {
                        titDetalle.visibility = View.GONE
                        titArea.visibility = View.GONE
                    }
                }

                if (!item.descripcionAveria.isNullOrEmpty()) {

                    var areatext = item.descripcionAveria.toString().substringBefore(':')
                    if (!areatext.isNullOrEmpty()) txtArea.setText(areatext)

                    var detalletext = item.descripcionAveria.toString().substringAfter(':')
                    if (!detalletext.isNullOrEmpty()) txtDetalle.setText(
                        detalletext.substring(
                            0,
                            detalletext.length
                        )
                    )

                }

                btnAbrirCamara.setOnClickListener {
                    if (item?.imagenes?.size!! < 5) {
                        addOPhoto.invoke(item, position, null)
                    }

                }

                binding.fotoUno.root.visibility = View.GONE
                binding.fotoDos.root.visibility = View.GONE
                binding.fotoTres.root.visibility = View.GONE
                binding.fotoCuatro.root.visibility = View.GONE
                binding.fotoCinco.root.visibility = View.GONE

                for (fotoIndex in 0..item?.imagenes?.count()!!) {


                    if (fotoIndex == 1) {
                        funfotoAdapter(
                            binding.fotoUno,
                            fotoIndex,
                            item,
                            position,
                            addOPhoto,
                            update,
                            viewPhoto
                        )
                    }
                    if (fotoIndex == 2) {
                        funfotoAdapter(
                            binding.fotoDos,
                            fotoIndex,
                            item,
                            position,
                            addOPhoto,
                            update,
                            viewPhoto
                        )
                    }
                    if (fotoIndex == 3) {
                        funfotoAdapter(
                            binding.fotoTres,
                            fotoIndex,
                            item,
                            position,
                            addOPhoto,
                            update,
                            viewPhoto
                        )
                    }
                    if (fotoIndex == 4) {
                        funfotoAdapter(
                            binding.fotoCuatro,
                            fotoIndex,
                            item,
                            position,
                            addOPhoto,
                            update,
                            viewPhoto
                        )
                    }
                    if (fotoIndex == 5) {
                        funfotoAdapter(
                            binding.fotoCinco,
                            fotoIndex,
                            item,
                            position,
                            addOPhoto,
                            update,
                            viewPhoto
                        )
                    }
                }
                txtDetalle.setOnClickListener {
                    addDanio.invoke(item,position,AveriaCaptureDialogDialog.TIPO_OTROS)
                }
                txtArea.setOnClickListener {
                    addDanio.invoke(item,position,AveriaCaptureDialogDialog.TIPO_OTROS)
                }



            } else {
                txtDetalle.setOnClickListener {
                    addDanio.invoke(item,position, AveriaCaptureDialogDialog.TIPO_DESCRIPCION)
                }


                if (!item.imagenes.isNullOrEmpty()) {
                    var neverEmpty = true
                    item.imagenes!!.forEach {
                        if(it.imgB64.isNullOrEmpty()){
                            neverEmpty = false
                        }
                    }
                    replacePhoto.visibility = if(neverEmpty) View.VISIBLE else View.GONE
                    btnAbrirCamara.setText("Cambiar Foto")
                }
                btnAbrirCamara.setOnClickListener {
                    addOPhoto.invoke(item, position, item.imagenes?.firstOrNull()?.imgB64 ?: null)

                }
            }


        }
    }

    private fun funfotoAdapter(
        view: ItemFotoInspecAeronaveBinding,
        fotoIndex: Int,
        item: Averia,
        position: Int,
        addOPhoto: (Averia, Int, String?) -> Unit,
        update: (Averia, Int) -> Unit,
        viewPhoto: (Bitmap) -> Unit
    ) {

        view.root.visibility = View.VISIBLE
        view.tvFoto.text = "Imagen $fotoIndex"
        view.btnVerFoto.setOnClickListener {
            //metodo para mostrar la foto
            viewPhoto.invoke(CreateImageFile.setBitmapFromB64String(item?.imagenes?.get(fotoIndex - 1)?.imgB64.toString()))
        }
        view.btnBorrarFoto.setOnClickListener {
            item?.imagenes?.removeAt(fotoIndex - 1)
            update.invoke(item, position)
        }


    }
}