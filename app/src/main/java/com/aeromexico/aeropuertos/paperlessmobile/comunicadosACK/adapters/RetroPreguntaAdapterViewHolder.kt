package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import CuestionarioComunicado
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemResolveCuestionarioAckBinding


public class RetroPreguntaAdapterViewHolder(var binding: ItemResolveCuestionarioAckBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): RetroPreguntaAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemResolveCuestionarioAckBinding.inflate(layoutInflater, parent, false)
            return RetroPreguntaAdapterViewHolder(itemBinding)
        }
    }

    fun bind(
        item: CuestionarioComunicado,
        position: Int
    ) {
        binding.apply {

            textQuestion.text = "${position + 1}.- ${item.pregunta}"
            if(!item.justificacion.isNullOrEmpty()) textJustify.text = "Justificacion: ${item.justificacion}"
            if (item.esPreguntaAbierta) {
                linearCorrect.visibility = View.GONE
                textAnswers.text = "${item.respuestaManual}\n "
            } else {
                for (i: Respuestas in item.respuestas) {
                    if (i.esCorrecta) {
                        textCorrectAnswer.text = i.respuesta
                    }
                    textAnswers.text = "${textAnswers.text}-${i.respuesta}\n"
                }
            }

        }

    }
}