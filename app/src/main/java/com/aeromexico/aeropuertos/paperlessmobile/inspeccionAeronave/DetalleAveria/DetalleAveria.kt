package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DetalleAveria

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.MediaCameraPictureDialog
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDetalleAveriaBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetalleAveria : Fragment() {

    //Binding
    private lateinit var mBinding: FragmentDetalleAveriaBinding

    //ViewModels
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()

    //Variable global de elemento seleccionado
    var averia_seleccionada: String = ""

    //Observaciones e imagen
    var detalleTxt: String = ""
    var imgB64: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentDetalleAveriaBinding.inflate(inflater, container, false)
        this.setListeners()
        return this.mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    //Precargar imagen cuando se ha lanzado otro fragmento
    override fun onResume() {
        if(!this.imgB64.isNullOrEmpty()){
            this.mostrarImagen(CreateImageFile.setBitmapFromB64String(imgB64, 8))
        }
        super.onResume()
    }

    //Observar viewModel
    private fun observeViewModel(){
        when(averia_seleccionada){
            (Constants.TiposAveria.radomo.name) -> {
                inspeccionFormViewModel.getRadomoTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })

                inspeccionFormViewModel.getRadomoMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

            (Constants.TiposAveria.comp_carga_delantero.name) -> {
                inspeccionFormViewModel.getCompCargaDelanteroTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })

                inspeccionFormViewModel.getCompCargaDelanteroMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.comp_carga_trasero.name) -> {
                inspeccionFormViewModel.getCompCargaTraseroTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })

                inspeccionFormViewModel.getCompCargaTraseroMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.comp_carga_bulk.name) -> {
                inspeccionFormViewModel.getCompCargaBulkTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getCompCargaBulkMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

            (Constants.TiposAveria.comp_int_delantero.name) -> {
                inspeccionFormViewModel.getCompIntDelanteroTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getCompIntDelanteroMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.comp_int_trasero.name) -> {
                inspeccionFormViewModel.getCompIntTraseroTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getCompIntTraseroMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.comp_int_bulk.name) -> {
                inspeccionFormViewModel.getCompIntBulkTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getCompIntBulkMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

            (Constants.TiposAveria.semiala_izq.name) -> {
                inspeccionFormViewModel.getSemialaIzqTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getSemialaIzqMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.semiala_der.name) -> {
                inspeccionFormViewModel.getSemialaDerTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getSemialaDerMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

            (Constants.TiposAveria.agua_potable.name) -> {
                inspeccionFormViewModel.getAguaPotableTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getAguaPotableMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.agua_negra.name) -> {
                inspeccionFormViewModel.getAguaNegraTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getAguaNegraMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

            (Constants.TiposAveria.puerta_principal.name) -> {
                inspeccionFormViewModel.getPuertaPrincipalTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getPuertaPrincipalMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.puerta_trasera.name) -> {
                inspeccionFormViewModel.getServicioTraseraTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getServicioTraseraMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })

            }

            (Constants.TiposAveria.sep_dos_pulg.name) -> {
                inspeccionFormViewModel.getSepDosInTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getSepDosInMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }
            (Constants.TiposAveria.colocacion_redes.name) -> {
                inspeccionFormViewModel.getColocacionRedesTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getColocacionRedesMandatorio().observe(viewLifecycleOwner, {
                    mostrarTextoImgMandatoria(it)
                })
            }

        }
    }

    private fun mostrarOcultarCampoObservaciones(tieneAveria: Boolean){
        if(tieneAveria){
            mBinding.tilDetalleAveria.visibility = View.VISIBLE
        }else{
            mBinding.tilDetalleAveria.visibility = View.GONE
        }
    }

    //Establecer listeners
    private fun setListeners(){

        //Botones de tomar foto
        mBinding.btnAbrirCamara.setOnClickListener{

            parentFragmentManager.let {
                MediaCameraPictureDialog() { foto ->
                    this.imgB64 = CreateImageFile.getB64FromBitmap(foto)
                    mostrarImagen(foto)
                    this.setImage()

                }.show(it, "MediaCameraPictureDialog")
            }
        }

        //Input de texto
        mBinding.etDetalleAveria.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                detalleTxt =  mBinding.etDetalleAveria.text.toString()
                setDamageDetail()
            }
        })
    }

    //Establecer imagen y detalle del daño en ViewModel
    private fun setImage(){
        when(this.averia_seleccionada){
            (Constants.TiposAveria.radomo.name) -> this.inspeccionFormViewModel.setRadomoImgB64(this.imgB64)
            (Constants.TiposAveria.comp_carga_delantero.name) -> this.inspeccionFormViewModel.setCompCargaDelanteroImgB64(this.imgB64)
            (Constants.TiposAveria.comp_carga_trasero.name) -> this.inspeccionFormViewModel.setCompCargaTraseroImgB64(this.imgB64)
            (Constants.TiposAveria.comp_carga_bulk.name) -> this.inspeccionFormViewModel.setCompCargaBulkImgB64(this.imgB64)
            (Constants.TiposAveria.comp_int_delantero.name) -> this.inspeccionFormViewModel.setCompIntDelanteroImgB64(this.imgB64)
            (Constants.TiposAveria.comp_int_trasero.name) -> this.inspeccionFormViewModel.setCompIntTraseroImgB64(this.imgB64)
            (Constants.TiposAveria.comp_int_bulk.name) -> this.inspeccionFormViewModel.setCompIntBulkImgB64(this.imgB64)
            (Constants.TiposAveria.semiala_izq.name) -> this.inspeccionFormViewModel.setSemialaIzqImgB64(this.imgB64)
            (Constants.TiposAveria.semiala_der.name) -> this.inspeccionFormViewModel.setSemialaDerImgB64(this.imgB64)
            (Constants.TiposAveria.agua_potable.name) -> this.inspeccionFormViewModel.setAguaPotableImgB64(this.imgB64)
            (Constants.TiposAveria.agua_negra.name) -> this.inspeccionFormViewModel.setAguaNegraImgB64(this.imgB64)
            (Constants.TiposAveria.puerta_principal.name) -> this.inspeccionFormViewModel.setPuertaPrincipalImgB64(this.imgB64)
            (Constants.TiposAveria.puerta_trasera.name) -> this.inspeccionFormViewModel.setServicioTraseraImgB64(this.imgB64)
            (Constants.TiposAveria.sep_dos_pulg.name) -> this.inspeccionFormViewModel.setSepDosInImgB64(this.imgB64)
            (Constants.TiposAveria.colocacion_redes.name) -> this.inspeccionFormViewModel.setColocacionRedesImgB64(this.imgB64)

        }
    }

    private fun setDamageDetail(){
        when(this.averia_seleccionada){
            (Constants.TiposAveria.radomo.name) -> this.inspeccionFormViewModel.setRadomoDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_carga_delantero.name) -> this.inspeccionFormViewModel.setCompCargaDelanteroDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_carga_trasero.name) -> this.inspeccionFormViewModel.setCompCargaTraseroDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_carga_bulk.name) -> this.inspeccionFormViewModel.setCompCargaBulkDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_int_delantero.name) -> this.inspeccionFormViewModel.setCompIntDelanteroDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_int_trasero.name) -> this.inspeccionFormViewModel.setCompIntTraseroDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.comp_int_bulk.name) -> this.inspeccionFormViewModel.setCompIntBulkDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.semiala_izq.name) -> this.inspeccionFormViewModel.setSemialaIzqDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.semiala_der.name) -> this.inspeccionFormViewModel.setSemialaDerDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.agua_potable.name) -> this.inspeccionFormViewModel.setAguaPotableDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.agua_negra.name) -> this.inspeccionFormViewModel.setAguaNegraDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.puerta_principal.name) -> this.inspeccionFormViewModel.setPuertaPrincipalDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.puerta_trasera.name) -> this.inspeccionFormViewModel.setServicioTraseraDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.sep_dos_pulg.name) -> this.inspeccionFormViewModel.setSepDosInDescripcionAveria(this.detalleTxt)
            (Constants.TiposAveria.colocacion_redes.name) -> this.inspeccionFormViewModel.setColocacionRedesDescripcionAveria(this.detalleTxt)

        }
    }

    //Manipular interfaz (mostrar imagen, ocultar botones)
    private fun mostrarTextoImgMandatoria(esMandatoria: Boolean){
        mBinding.tvImagenMandatoria.visibility = if (esMandatoria) View.VISIBLE else View.GONE
    }


    private fun mostrarImagen(img:Bitmap){

        mBinding.mImageView.visibility = View.VISIBLE
       // mBinding.mImageView.setImageURI(uri)

        mBinding.mImageView.setOnClickListener{

            val imageView = ImageView(context)
            imageView.layoutParams = LinearLayout.LayoutParams(160, 160)
            imageView.setImageBitmap(img)

            MaterialAlertDialogBuilder(requireContext())
                .setView(imageView)
                .setPositiveButton(R.string.aceptar) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    // inspeccionSeleccionada = null
                }
                .show()
        }
        mBinding.btnAbrirCamara.text = getString(R.string.btnReintentarFoto)
        mBinding.btnAbrirCamara.setIconResource(R.drawable.ic_redo)

    }

    private fun reiniciarImagen(){
       // mBinding.mImageView.setImageURI(null)
        mBinding.mImageView.visibility = View.GONE
        this.imgB64 = ""
    }

}