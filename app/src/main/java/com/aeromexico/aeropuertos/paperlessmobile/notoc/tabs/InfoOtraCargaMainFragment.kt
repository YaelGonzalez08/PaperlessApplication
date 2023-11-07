package com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInfoOtraCargaMainBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.BultosAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.InfoMercanciaRecyclerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.InfoOtraCargaClickListener
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.InfoOtraCargaRecyclerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.Bultos
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.CatPosicion
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.OtraCarga

class InfoOtraCargaMainFragment : Fragment() {

    private lateinit var mBinding: FragmentInfoOtraCargaMainBinding
    private lateinit var mAdapter: InfoOtraCargaRecyclerAdapter
    private lateinit var adapterBultos: BultosAdapter
    private lateinit var gridLayout: GridLayoutManager
    private lateinit var gridLayoutBultos: GridLayoutManager
    private  val viewModel: NotocViewModel by activityViewModels()
    private lateinit var posiciones: List<CatPosicion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentInfoOtraCargaMainBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        obtieneInfoEnDBLocal()
    }
    private fun obtieneInfoEnDBLocal(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            val nombres: MutableList<String> = mutableListOf()
            it.catPosicion.forEach {
                nombres.add(it.descPosicion)
            }
            posiciones = it.catPosicion
            mAdapter.setInfo(it.otraCarga,nombres)
            mAdapter.setEditable(it.editable)

            if(it.otraCarga.count() == 0){
                mBinding.recyclerView.visibility = View.GONE
                mBinding.tvNil.visibility = View.VISIBLE
            }
            else
            {
                mBinding.recyclerView.visibility = View.VISIBLE
                mBinding.tvNil.visibility = View.GONE
            }
        }

    }

    private fun setupRecyclerView() {
        mAdapter = InfoOtraCargaRecyclerAdapter(mutableListOf(),arrayListOf(),::onSetPosicion, ::setupRecyclerBultos, ::setBultos, true)
        gridLayout = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }

    private fun onSetPosicion(posicion: String, i: Int){
        posiciones.forEach {
            if(it.descPosicion == posicion)
                viewModel.notoc.otraCarga[i].posicion = it.idPosicionNOTOC
        }
        viewModel.setNotocEntity(viewModel.notoc)
    }

    private fun setupRecyclerBultos(recyclerView: RecyclerView) {
        adapterBultos = BultosAdapter(mutableListOf())
        gridLayoutBultos = GridLayoutManager(context, resources.getInteger(R.integer.main_columns))
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutBultos
            adapter = adapterBultos
        }
    }
    private fun setBultos(bultos: List<Bultos>){
        adapterBultos.setInfo(bultos)
    }




}