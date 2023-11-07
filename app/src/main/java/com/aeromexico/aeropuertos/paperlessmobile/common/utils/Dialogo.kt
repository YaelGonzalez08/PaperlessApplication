package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.DialogMessageBinding
import com.bumptech.glide.Glide

class Dialogo(var context: Context) {

    private val dialogBuilder: AlertDialog.Builder
    var btnAceptar: TextView
    var btnCerrar: TextView
    private lateinit var dialog: AlertDialog

    companion object {
        lateinit var binding: DialogMessageBinding
    }

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = DialogMessageBinding.inflate(layoutInflater)
        btnAceptar = binding.btnAceptar
        btnCerrar = binding.btnCerrar
        dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(binding.root)
        dialogBuilder.setCancelable(false)
        dialog = dialogBuilder.create()
    }

    fun mostrarError(titulo: String?, mensaje: String?) {

        // muestra el error con solo el boton de cerrar el dialogo
        cargarVista(titulo, mensaje)
        binding.imagen.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_rojo_carmin))
        binding.imagen.background = ContextCompat.getDrawable(context, R.drawable.ic_error)
        binding.btnAceptar.visibility = View.GONE
        dialog.show()
    }

    fun mostrarAviso(titulo: String?, mensaje: String?) {

        // muestra el error con solo el boton de cerrar el dialogo
        cargarVista(titulo, mensaje)
        binding.imagen.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.amarillo_mensaje_aviso))
        binding.imagen.background = ContextCompat.getDrawable(context, R.drawable.ic_error)
        binding.btnAceptar.visibility = View.GONE
        btnCerrar.background = ContextCompat.getDrawable(context, R.drawable.background_logout_button_yellow)
        dialog.show()
    }

    fun mostrarMensajeConfirmacion(titulo: String?, mensaje: String?) {

        //muestra mensaje con opciond e dar aceptar o cerrar el dialogo
        cargarVista(titulo, mensaje)
        binding.imagen.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_verde))
        binding.imagen.background =
            ContextCompat.getDrawable(context, R.drawable.ic_palomita_con_circulo)
        dialog.show()
    }


    fun mostrarPregunta(titulo: String?, mensaje: String?) {
        cargarVista(titulo, mensaje)
        binding.imagen.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_rojo_carmin))
        binding.imagen.background =
            ContextCompat.getDrawable(context, R.drawable.ic_pregunta)

        btnAceptar.text =  context.getString(R.string.confirmar)
        btnCerrar.text = context.getString(R.string.cancel)

        dialog.show()
    }


    fun mostrarCargando(mensaje: String?) {
        Glide.with(PaperlessApplication.contextAppSingleton).load(R.drawable.loader).into(binding.cargando);

        //muestra solo el mensaje de cargando sin botones de cerrar ni aceptar
        if(mensaje.isNullOrEmpty()){
            cargarVista(null, context.getString(R.string.espere))
        }else{
            cargarVista(null,mensaje)
        }

        binding.apply {
            btnCerrar.visibility = View.GONE
            btnAceptar.visibility = View.GONE
            imagen.visibility = View.GONE
            cargando.visibility = View.VISIBLE
        }
        dialog.show()
    }

    private fun cargarVista(titulo: String?, mensaje: String?) {

        binding.apply {
            /*btnCerrar.setOnClickListener {
                Ocultar()
            }*/
            titulo.also {
                txtTitulo.text = it
            }
            mensaje.also {
                txtMensaje.text = it
            }
        }

    }

    fun Ocultar() {
        // si lo mandas a llamar se cierra el dialogo
        if (dialog.isShowing) {
            dialog.dismiss()
            binding.apply {
                btnCerrar.visibility = View.VISIBLE
                btnAceptar.visibility = View.VISIBLE
                imagen.visibility = View.VISIBLE
                cargando.visibility = View.GONE
            }
        }
    }

}