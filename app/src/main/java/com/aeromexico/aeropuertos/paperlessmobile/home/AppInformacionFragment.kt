package com.aeromexico.aeropuertos.paperlessmobile.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.AppContextSIngleton
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentAppInformacionBinding
import com.aeromexico.aeropuertos.paperlessmobile.login.view.LoginActivity
import com.aeromexico.aeropuertos.paperlessmobile.webService.Result
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AppInformacionFragment : Fragment() {

    lateinit var bindig: FragmentAppInformacionBinding
    lateinit var userStorage: DataStorageLogin
    private var mActivity: MainActivity? = null
    lateinit var d: Dialogo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindig = FragmentAppInformacionBinding.inflate(inflater, container, false)
        mActivity = activity as? MainActivity

        d= Dialogo(requireContext())
        userStorage = AppContextSIngleton.getInstance(null)?.let { DataStorageLogin(it) }!!
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.informacion)
        printInfoApp()

        return bindig.root
    }

    private fun printInfoApp() {
        bindig.apply {
            tvVersion.text = BuildConfig.VERSION_NAME
            tvCompilationDate.text = Constants.APP_COMPILE_INFO
            tvSupportInfo.text = Constants.APP_SUPPORT_INFO
            tvMessage.text = Constants.MESSAGE_4488
            tvGroupForTicketText.text = Constants.TEXT_TICKET
            tvGroupForTicket.text = Constants.GROUP_RELEASE_TICKET
            btnLogOut.setOnClickListener {
                mActivity?.logOut()
            }
            if(BuildConfig.PAPERLESS_AMBIENT.contains("dev")){
                tvAmbient.text = "Desarrollo"

            }else{
                tvAmbient.text = "Productivo"

            }
            /*btnIngles.setOnClickListener {
                MainActivity.getInstance()?.setLocate("en")
            }

            btnEs.setOnClickListener {
                MainActivity.getInstance()?.setLocate("es")
            }*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.buscar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}