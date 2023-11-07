package com.aeromexico.aeropuertos.paperlessmobile.notoc.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentInfoMercanciaMainBinding
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter.InfoMercanciaRecyclerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.CatPosicion


class InfoMercanciaMainFragment : Fragment() {
    private lateinit var mBinding: FragmentInfoMercanciaMainBinding

    private lateinit var mAdapter: InfoMercanciaRecyclerAdapter
    private lateinit var gridLayout: GridLayoutManager

    private  val viewModel: NotocViewModel by activityViewModels()
    private lateinit var posiciones: List<CatPosicion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentInfoMercanciaMainBinding.inflate(inflater,container,false)
        setupRecyclerView()
        obtieneInfoEnDBLocal()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("InfoMerc",viewModel.notoc.toString())
        /*Snackbar.make(mBinding.root, "Info ${viewModel.notoc}", Snackbar.LENGTH_LONG)
            .show()*/
//        mAdapter.setInfo(viewModel.notoc.informacionMercancia)
    }

    private fun obtieneInfoEnDBLocal(){
        viewModel.notocLiveData.observe(viewLifecycleOwner){
            //Toast.makeText(context,"si llega",Toast.LENGTH_LONG).show()
            Log.i("InfoMercLive",viewModel.notoc.toString())
            val nombres: MutableList<String> = mutableListOf()
            it.catPosicion.forEach {
                nombres.add(it.descPosicion)
            }
            posiciones = it.catPosicion
            mAdapter.setInfo(it.informacionMercancia, nombres)
            mAdapter.setEditable(it.editable)

            if(it.informacionMercancia.count() ==0){
                mBinding.recyclerView.visibility = View.GONE
                mBinding.tvNil.visibility = View.VISIBLE
            }
            else
            {
                mBinding.recyclerView.visibility = View.VISIBLE
                mBinding.tvNil.visibility = View.GONE
            }
        }



        /*viewModel.posicionArregloInfoMercancia.observe(viewLifecycleOwner){

            mAdapter.setPosiciones(it)
        }*/
    }

    private fun setupRecyclerView() {
        mAdapter = InfoMercanciaRecyclerAdapter(mutableListOf(), arrayListOf(),::onSetPosicion, true)
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
                viewModel.notoc.informacionMercancia[i].posicion = it.idPosicionNOTOC


        }
        viewModel.setNotocEntity(viewModel.notoc)

    }




}