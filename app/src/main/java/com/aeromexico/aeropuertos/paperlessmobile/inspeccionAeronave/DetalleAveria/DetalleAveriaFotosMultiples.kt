package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DetalleAveria

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.MediaCameraPictureDialog
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDetalleAveriaBinding
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDetalleAveriaFotosMultiplesBinding
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel.InspeccionFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.FotoAdapter
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter.FotoClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetalleAveriaFotosMultiples : Fragment(), FotoClickListener {

    //Binding
    private lateinit var mBinding: FragmentDetalleAveriaFotosMultiplesBinding

    //ViewModels
    private val inspeccionFormViewModel: InspeccionFormViewModel by activityViewModels()

    //RecyclerView
    private lateinit var mAdapter: FotoAdapter
    private lateinit var gridLayout: GridLayoutManager

    //Variable global de elemento seleccionado
    var averia_seleccionada: String = ""

    //Observaciones e imagen
    var detalleTxt: String = ""
    var imgB64: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(averia_seleccionada){
            (Constants.TiposAveria.otro.name) -> {
                inspeccionFormViewModel.otros_imagenes.value = arrayListOf()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentDetalleAveriaFotosMultiplesBinding.inflate(inflater, container, false)
        this.setListeners()

        this.setupRecyclerView()
        return this.mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        super.onViewCreated(view, savedInstanceState)
    }



    //Observar viewModel
    private fun observeViewModel(){
        when(averia_seleccionada){
            (Constants.TiposAveria.otro.name) -> {
                inspeccionFormViewModel.getOtrosTieneAveria().observe(viewLifecycleOwner, {
                    mostrarOcultarCampoObservaciones(it)
                })
                inspeccionFormViewModel.getOtrosImagenes().observe(viewLifecycleOwner,{
                    mAdapter.setList(it)
                    updateBotonFoto(it)
                })

                inspeccionFormViewModel.getOtrosMandatorio().observe(viewLifecycleOwner, {
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
                    val imagen = CreateImageFile.getB64FromBitmap(foto)
                    // mostrarImagen(foto)
                    setImage(imagen)

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


    //RecyclerView y eventos de ver / eliminar foto
    private fun setupRecyclerView() {

        mAdapter = FotoAdapter(mutableListOf(), this)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerViewFotos.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    override fun onDelete(foto: String) {
        when(this.averia_seleccionada){
            (Constants.TiposAveria.otro.name) ->  inspeccionFormViewModel.deleteOtrosImagen(foto)
        }

    }

    override fun onView(foto: String) {
        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(160, 160)

        val decodedString: ByteArray = Base64.decode(foto, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        imageView.setImageBitmap(decodedByte)

        MaterialAlertDialogBuilder(requireContext())
            .setView(imageView)
            .setPositiveButton(R.string.aceptar) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }


    //Manipular interfaz (mostrar imagen, ocultar botones)
    private fun mostrarTextoImgMandatoria(esMandatoria: Boolean){
        mBinding.tvImagenMandatoria.visibility = if (esMandatoria) View.VISIBLE else View.GONE
    }



    //Establecer imagen y detalle del daño en ViewModel
    private fun setImage(imagen: String){
        when(this.averia_seleccionada){
            (Constants.TiposAveria.otro.name) -> {
                inspeccionFormViewModel.addOtrosImagen(imagen);
            }
        }
    }

    private fun setDamageDetail(){
        when(this.averia_seleccionada){
            (Constants.TiposAveria.otro.name) -> this.inspeccionFormViewModel.setOtrosDescripcionAveria(this.detalleTxt)
        }
    }


    //Mostrar / ocultar botón de tomar foto
    private fun  updateBotonFoto(fotos: MutableList<String>){
        if(fotos.size == 5){
            mBinding.btnAbrirCamara.visibility = View.GONE
        }else{
            mBinding.btnAbrirCamara.visibility = View.VISIBLE
        }
    }

}