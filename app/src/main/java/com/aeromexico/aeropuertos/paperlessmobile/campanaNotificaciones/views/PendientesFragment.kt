package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.viewmodel.CheckListPendientesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.adapters.PendientesPagerAdapter
import com.aeromexico.aeropuertos.paperlessmobile.databinding.PendientesFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity

class PendientesFragment : Fragment(), AdapterView.OnItemClickListener {

    private lateinit var viewModel: CheckListPendientesViewModel
    private lateinit var mBinding: PendientesFragmentBinding
    private var mActivity: MainActivity? = null
    private lateinit var adapterPagerTabs: PendientesPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckListPendientesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = PendientesFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStepper()
        setupActionBar()
        setupSpinner()
    }

    private fun setupStepper() {
        adapterPagerTabs = PendientesPagerAdapter(childFragmentManager)
        mBinding.tabsContent.adapter = adapterPagerTabs
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.pendientes)
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

    private fun setupSpinner() {
        val items = listOf(R.array.modulosArray)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.modulosArray,
            R.layout.list_item
        ).also { adapter ->
            (mBinding.modulosSpinner.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (mBinding.modulosSpinner.editText as? AutoCompleteTextView)?.setText(
                adapter.getItem(0).toString(), false
            )
        }
        (mBinding.modulosSpinner.editText as? AutoCompleteTextView)?.onItemClickListener = this
    }

    private fun setGoToTab(i: Int) {
        Log.i("setGoToTab ->","$i")
        when (i) {
            0 -> {
                mBinding.tabsContent.currentItem = 0
            }
            1 -> {
                mBinding.tabsContent.currentItem = 1
            }

            2 -> {
                mBinding.tabsContent.currentItem = 2
            }
            3 -> {
                mBinding.tabsContent.currentItem = 3
            }
            4 -> {
                mBinding.tabsContent.currentItem = 4
            }
            5->{
                mBinding.tabsContent.currentItem = 5
            }
            6 ->{
                mBinding.tabsContent.currentItem = 6
            }
        }
    }


    override fun onItemClick(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        setGoToTab(pos)
        Toast.makeText(requireContext(), "cambio", Toast.LENGTH_SHORT).show()
    }

}