package com.aeromexico.aeropuertos.paperlessmobile.searchList.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentSearchListBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.searchList.adapters.ViewPegerAdapterCheckList
import com.google.android.material.tabs.TabLayoutMediator


class SearchListFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSearchListBinding
    private var mActivity: MainActivity? = null
    private val args: SearchListFragmentArgs by navArgs()
    private var percnotaSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchListBinding.inflate(layoutInflater, container, false)
        mActivity = activity as? MainActivity
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.tool_bar_title_search_list)
        setupActionBar()
        setBackButtonSystem(requireActivity())
        setHeaders()

        binding.apply {
            btnUpdate.setOnClickListener {
                vpFormCheckList.adapter = ViewPegerAdapterCheckList(requireActivity().supportFragmentManager, lifecycle, args?.vuelo?.guid, args.lpd)
                TabLayoutMediator(tabLayout, vpFormCheckList) { tab, position ->
                    if(args.lpd) {
                        when (position) {
                            0 -> tab.text = "O.O./AAR"
                            1 -> tab.text = "PAB"
                            2 -> tab.text = "Cocinas"
                            3 -> tab.text = "Tierra"
                            4 -> tab.text = "Piloto"
                        }
                    } else {
                        when (position) {
                            0 -> tab.text = "O.O./AAR"
                            1 -> tab.text = "Cocinas"
                            2 -> tab.text = "Tierra"
                            3 -> tab.text = "Piloto"
                        }
                    }

                }.attach()
                setTabLayOutIcons()

            }

            vpFormCheckList.adapter = ViewPegerAdapterCheckList(requireActivity().supportFragmentManager, lifecycle, args?.vuelo?.guid, args.lpd)
            TabLayoutMediator(tabLayout, vpFormCheckList) { tab, position ->
                if(args.lpd) {
                    when (position) {
                        0 -> tab.text = "O.O./AAR"
                        1 -> tab.text = "PAB"
                        2 -> tab.text = "Cocinas"
                        3 -> tab.text = "Tierra"
                        4 -> tab.text = "Piloto"
                    }
                } else {
                    when (position) {
                        0 -> tab.text = "O.O./AAR"
                        1 -> tab.text = "Cocinas"
                        2 -> tab.text = "Tierra"
                        3 -> tab.text = "Piloto"
                    }
                }

            }.attach()
            percnota.setOnClickListener(this@SearchListFragment)
        }

        setTabLayOutIcons()

        return binding.root
    }

    private fun setHeaders() {
        args.let {
            binding.apply {
                tvDateFlightValue.text = it.vuelo?.fechaVueloLocal
                tvFlightNumberValue.text = it.vuelo?.numVuelo.toString()
                tvEnrollmentValue.text = it.vuelo?.matricula
                tvRouteValue.text = getString(R.string.route_fligth, it.vuelo?.origen, it.vuelo?.destino)
            }
        }
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.search_list)
        setHasOptionsMenu(true)
    }

    private fun setTabLayOutIcons() {
        binding.apply {
            tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_number_one)
            tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_numero__two)
            tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_number_tree)
            tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_number_four)
            if (args.lpd) tabLayout.getTabAt(4)?.setIcon(R.drawable.ic_five)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(activity, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setBackButtonSystem(requireActivity: FragmentActivity) {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity.onBackPressedDispatcher.addCallback(callBack)
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (view?.id) {
               R.id.percnota -> if (percnotaSelected) {
                    percnota.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_moon, null)
                    percnotaSelected = false
                } else {
                    percnota.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_moon_blue, null)
                    percnotaSelected = true
                }
            }
            }
        }
}