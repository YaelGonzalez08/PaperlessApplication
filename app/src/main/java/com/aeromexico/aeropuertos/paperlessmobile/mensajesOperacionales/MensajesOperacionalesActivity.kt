package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.adapters.OnClickListener
import com.aeromexico.aeropuertos.paperlessmobile.common.adapters.PaperlessAdapter
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ActivityMensajesOperacionalesBinding
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.viewModel.MensajesOperacionalesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos

class MensajesOperacionalesActivity : AppCompatActivity(), OnClickListener {
    private lateinit var mBinding: ActivityMensajesOperacionalesBinding

    private lateinit var mAdapter: PaperlessAdapter
    private lateinit var gridLayout: GridLayoutManager
    private var mActivity: MensajesOperacionalesActivity? = null

    //MVVM
    private lateinit var mMOViewModel: MensajesOperacionalesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMensajesOperacionalesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

//        setupViewModel()
//        setupRecyclerView()
        val controller = findNavController(R.id.fragmentMO)
//        setupActionBar()
    }



    private fun setupViewModel() {
        mMOViewModel = ViewModelProvider(this).get(MensajesOperacionalesViewModel::class.java)

    }

    /*private fun setupRecyclerView() {
        mAdapter = PaperlessAdapter(mutableListOf(),this)
        gridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerViewlayout.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = mAdapter
        }
    }*/

    private fun setupActionBar(){
        this.setTitle("Mensajes Operacionales")

    }

    override fun onClick(flightEntity: Vuelos) {
        TODO("Not yet implemented")
    }
}