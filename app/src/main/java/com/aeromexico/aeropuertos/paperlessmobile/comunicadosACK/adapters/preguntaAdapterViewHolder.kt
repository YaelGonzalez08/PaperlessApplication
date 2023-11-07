package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters

import CuestionarioComunicado
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ItemPreguntaEncuestaAckBinding
import java.util.*


public class preguntaAdapterViewHolder(var binding: ItemPreguntaEncuestaAckBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): preguntaAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding =
                ItemPreguntaEncuestaAckBinding.inflate(layoutInflater, parent, false)
            return preguntaAdapterViewHolder(itemBinding)
        }
    }

    fun bind(
        item: CuestionarioComunicado,
        onRespuestaManual: (item: CuestionarioComunicado,position:Int) -> Unit,
        position:Int,
        respuestaSeleccionada:(respuesta:Respuestas,position:Int) -> Unit
    ) {
        binding.apply {
            if(position %2 == 0){
                binding.content.setBackgroundResource(R.color.color_gray_white_background_checklist)
            }
            txtQuestion.text = "${position+1}.- ${item.pregunta}"

            if (item.esPreguntaAbierta){
                tilRespuestaAbierta.visibility = View.VISIBLE
                radioGroup.visibility = View.GONE
                txtRespuestaAbierta.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        item.respuestaManual = s.toString()
                        onRespuestaManual(item,position)
                        item.respuestas.last().respuesta = s.toString()
                        item.respuestas.last().esCorrecta = true
                        respuestaSeleccionada(item.respuestas.last(),position)
                    }
                })

            }else{
                tilRespuestaAbierta.visibility = View.GONE
                radioGroup.visibility = View.VISIBLE

                val buttons = item.respuestas.size

                val rgp = binding.radioGroup
                rgp.orientation = LinearLayout.VERTICAL

                val colorAsnwer = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        Color.BLACK,  //disabled
                        root.resources.getColor(R.color.primaryColor) //enabled
                    )
                )

                for (i: Respuestas in item.respuestas) {
                    val rbn = RadioButton(root.context)
                    rbn.id = View.generateViewId()
                    rbn.text = "${i.respuesta}"
                    rbn.buttonTintList = colorAsnwer
                    rbn.setOnClickListener{
                        respuestaSeleccionada(i,position)
                    }
                    rgp.addView(rbn)
                }
            }

        }

    }
}