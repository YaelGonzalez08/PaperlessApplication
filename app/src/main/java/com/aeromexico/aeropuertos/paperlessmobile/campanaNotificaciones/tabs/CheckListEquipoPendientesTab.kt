package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters.CheckDiarioItemPendienteAdapter
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.viewmodel.CheckListPendientesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentEquipoDiarioTabBinding
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.PreguntasCheckListToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.repository.CheckListRepository
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.viewModels.CheckListDiarioViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.gson.Gson

class CheckListEquipoPendientesTab : Fragment() {
    lateinit var binding: FragmentEquipoDiarioTabBinding
    lateinit var model: CheckListPendientesViewModel
    var lista: ArrayList<CheckEquipoDiarioEntity> = arrayListOf()
    lateinit var adapter: CheckDiarioItemPendienteAdapter
    lateinit var dialogo: Dialogo
    lateinit var checkModel: CheckListDiarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipoDiarioTabBinding.inflate(inflater, container, false)
        dialogo = Dialogo(requireContext())
        model = CheckListPendientesViewModel()
        adapter =
            CheckDiarioItemPendienteAdapter(lista, ::btnEnviar, ::clickDeleteFromBD)
        binding.recyclerChecklist.adapter = this@CheckListEquipoPendientesTab.adapter
        checkModel =
            CheckListDiarioViewModel(CheckListRepository(WebServiceApi().getCheckListApi()))

        model.allChecks.observe(viewLifecycleOwner, { it ->
            lista.clear()

            for (c in it) {
                lista.add(c)
            }

            if (!lista.isNullOrEmpty()) {
                adapter.updateList(lista)
                binding.msjSinPendientes.root.visibility = View.GONE
            }else{
                binding.msjSinPendientes.textSinpendientes.text = getString(R.string.no_pendientes_check_list_enviar)
                binding.msjSinPendientes.root.visibility = View.VISIBLE
            }

        })

        return binding.root
    }

    private fun clickDeleteFromBD(objtCheck: CheckEquipoDiarioEntity) {

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
                objtCheck.id,
                Gson().fromJson(objtCheck.datos.toString(), CheckToServer::class.java)
            )

        }

    }

    private fun btnEnviar(objtCheck: CheckEquipoDiarioEntity) {
        var c = Gson().fromJson(objtCheck.datos.toString(), CheckToServer::class.java)

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

        })
    }

    private fun SendToServerCheckList(index: Int, checkToServer: CheckToServer) {

        checkModel.SentCheckList(checkToServer).observeOnce { responseCheck ->

            if (responseCheck != null) {
                dialogo.Ocultar()
                responseCheck.result.let {
                    dialogo = Dialogo(requireContext())
                    dialogo.mostrarMensajeConfirmacion(
                        "Envio Exitoso",
                        "En checkList se envio correctamente folio: ${it?.folio}"
                    )
                    dialogo.btnAceptar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                    dialogo.btnCerrar.setOnClickListener {
                        dialogo.Ocultar()
                    }
                    deleteFromBD(index, checkToServer)
                }

            } else {
                dialogo.Ocultar()
                dialogo = Dialogo(requireContext())
                dialogo.mostrarError(
                    "Error de Conexión ",
                    "Ocurrio un Error Intenta mas tarde !"
                )
                dialogo.btnCerrar.setOnClickListener {
                    dialogo.Ocultar()

                }

            }
        }
    }



    private fun deleteFromBD(index: Int, checkToServer: CheckToServer) {
        /* Snackbar.make(
             binding.root,
             "$index vas a eliminar  ${checkToServer.equipo?.numeroActivo} --- ${checkToServer.equipo?.descripcion}",
             Snackbar.LENGTH_SHORT
         ).show()*/
        model.deleteCheck(index, checkToServer)
    }


}
