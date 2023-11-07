package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters.DeshieloPendienteAdapter
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.viewmodel.DeshieloPendienteViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentDeshieloPendientesTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class DeshieloPendientesTab : Fragment() {
    lateinit var binding: FragmentDeshieloPendientesTabBinding
    lateinit var adapter : DeshieloPendienteAdapter
    lateinit var model:DeshieloPendienteViewModel
    var lista: ArrayList<DeshieloEntity> = arrayListOf()
    lateinit var dialogo:Dialogo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeshieloPendientesTabBinding.inflate(layoutInflater)
        adapter = DeshieloPendienteAdapter(arrayListOf(), ::btnEnviar,::clickDeleteFromBD)
        model = ViewModelProvider(requireActivity()).get(DeshieloPendienteViewModel::class.java)
        binding.recyclerDeshielo.adapter = this@DeshieloPendientesTab.adapter
        model.getDeshieloPendientes().observe(viewLifecycleOwner, Observer {
            lista.clear()

            for (c in it) {
                lista.add(c)
            }

            if (!lista.isNullOrEmpty()) {
                adapter.updateList(lista)
                binding.msjSinPendientes.root.visibility = View.GONE
            }else{
                binding.msjSinPendientes.root.visibility = View.VISIBLE
                binding.msjSinPendientes.textSinpendientes.text = "No hay solicitudes pendientes de Servicio de Deshielo / AntiHielo."
            }
        })


        return binding.root
    }
    private fun clickDeleteFromBD(obj: DeshieloEntity) {

        dialogo = Dialogo(requireContext())
        dialogo.mostrarPregunta(
            "¡Atencion!",
            "El checklist aun no esta enviado, ¿Estas Seguro de ELIMINAR este CheckList?, No se enviara ningun cambio,  y no podras revertir esto"
        )
        dialogo.btnCerrar.setOnClickListener {
            dialogo.Ocultar()
        }
        dialogo.btnAceptar.setOnClickListener {
            dialogo.Ocultar()
            deleteFromBD(
                obj.id,
                Gson().fromJson(obj.datos.toString(), DeshieloToServer::class.java)
            )

        }

    }
    private fun btnEnviar(objtCheck: DeshieloEntity) {

        var c = Gson().fromJson(objtCheck.datos.toString(), DeshieloToServer::class.java)

/*
        dialogo = Dialogo(requireContext())
        dialogo.mostrarCargando(null)
        model.readImegesChech(c.fecha).observe(viewLifecycleOwner, Observer { l ->

            Log.i("fotos en check", "${l.size}")
            /* Snackbar.make(
                 binding.root,
                 "fotos ${l.size} --- ${c.equipo?.descripcion}",
                 Snackbar.LENGTH_SHORT
             ).show()*/
            if (l != null) {
                var lis: ArrayList<PreguntasCheckListToServer> = arrayListOf()

                l.forEach {
                    lis.add(
                        PreguntasCheckListToServer(
                            id = it.id,
                            estate = it.estate!!,
                            observation = it.observation,
                            photo = it.photo
                        )
                    )
                }
                c.lista = lis
                SendToServerCheckList(objtCheck.id, c)
            } else {
                dialogo.Ocultar()
            }

        })*/
    }

    private fun deleteFromBD(index: Int, obj: DeshieloToServer) {
         Snackbar.make(
             binding.root,
             "$index vas a eliminar  ${obj?.vuelo?.numVuelo}  ${obj?.vuelo?.origen}-${obj?.vuelo?.destino}",
             Snackbar.LENGTH_SHORT
         ).show()

        model.deleteFromBD(index, obj)
    }
}