package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.tabs

import CuestionarioComunicado
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.hideKeyboard
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters.preguntasAdapter
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentEncuestaACKBinding
import ng.softcom.android.utils.ui.showToast


class EncuestaACKFragment(
    var cuestionarioComunicado: ArrayList<CuestionarioComunicado>,
    var newlist: (item: ArrayList<CuestionarioComunicado>) -> Unit,
    var respuestas: (item: ArrayList<Respuestas>) -> Unit
) :
    Fragment() {
    lateinit var binding: FragmentEncuestaACKBinding
    lateinit var adapter: preguntasAdapter
    lateinit var listarespuestas: MutableList<Respuestas?>

    override fun onPause() {
        super.onPause()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEncuestaACKBinding.inflate(layoutInflater)
        if(cuestionarioComunicado.isNullOrEmpty()){
            binding.tittleResponse.text = "Este comunicado no contiene preguntas."
        }
        adapter = preguntasAdapter(cuestionarioComunicado, ::respuestaSeleccionada)
        listarespuestas = mutableListOf(*arrayOfNulls(cuestionarioComunicado.size))

        binding.recycler.adapter = adapter

        binding.mbEnviar.setOnClickListener {
            it.hideKeyboard()
            if (!listarespuestas.contains(null)) {
                validAllAnswersItsCorrect()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Faltan preguntas por responder ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun validAllAnswersItsCorrect() {

// con el position del array dentro de un foreach, vaas a notificar al adaptador si la respuesta esta mal poniendo un mensaje de respuesta incorrecta verifica,

        // la comparacion es si la respuesta dentro de listarespuesta es diferente de correcta es cuando seleccionaron una mal y ahi cuando la encontremos en la posicion del array la mandamos al adapter
        // y evitamos asi que pase al proceso de aqui abajo
        var everyok = true
        var badQuestiosn = ""
        for (position in listarespuestas.indices) {
            if ( listarespuestas[position]?.esCorrecta == false) {
                everyok = false
                // showToast("la pregunta $position esta mal resuelta")
                badQuestiosn += "${position+1}.-${adapter.list[position].pregunta}\n"
            }
        }

        if (everyok) {
            newlist.invoke(adapter.getLista())
            var arrayrespuestas: ArrayList<Respuestas> = arrayListOf()
            listarespuestas.forEach { res ->
                if (res != null) {
                    arrayrespuestas.add(res)
                }
            }
            respuestas.invoke(arrayrespuestas)
        }else{
            var d:Dialogo = Dialogo(requireContext())
            d.mostrarError(titulo = "Respuestas Erroneas", mensaje = badQuestiosn)
            d.btnCerrar.setOnClickListener {
                d.Ocultar()
            }
        }

    }

    private fun respuestaSeleccionada(respuesta: Respuestas, i: Int) {
        listarespuestas[i] = respuesta
    }

}