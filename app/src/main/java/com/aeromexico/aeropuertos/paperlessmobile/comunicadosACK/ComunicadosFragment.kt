package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.adapters.comunicadoAdapter
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Comunicados
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository.ComunicadosRepository
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentComunicadosBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class ComunicadosFragment : Fragment() {
    lateinit var binding: FragmentComunicadosBinding
    lateinit var model: comunicadosViewModel
    lateinit var adapter: comunicadoAdapter
    private var mActivity: MainActivity? = null
    lateinit var dialog : Dialogo
    override fun onResume() {
        super.onResume()
        setupActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComunicadosBinding.inflate(layoutInflater)
        model = comunicadosViewModel(ComunicadosRepository(WebServiceApi().getComuncadisApi()),
            CoreRepository(WebServiceApi().getCoreApi())
        )
        dialog = Dialogo(requireContext())
        adapter = comunicadoAdapter(arrayListOf(), ::onclicComunicado)
        binding.rec.adapter = adapter

        observers()

        return binding.root
    }

    private fun onclicComunicado(item: Comunicados) {
        var dest =
            ComunicadosFragmentDirections.actionComunicadosFragmentToVerComunicadoFragment(item.idComunicado)
        findNavController().navigate(dest)

    }

    private fun observers() {
        model.isLoading.observe(viewLifecycleOwner, Observer {
            if(it){
                dialog.mostrarCargando(getString(R.string.cargando))
            }else{
                dialog.Ocultar()
            }
        })
        model.getComunicados().observe(viewLifecycleOwner, Observer {
            it?.result?.let { result ->
                result?.comunicados.let { list ->
                    list?.sortByDescending { e -> e.prioritario}
                    adapter.updateList(
                        list ?: arrayListOf()
                    )
                }
            }
            it.message.let {
             //   Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Comunicados"
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}