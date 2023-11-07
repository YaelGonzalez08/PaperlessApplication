package com.aeromexico.aeropuertos.paperlessmobile.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.aeromexico.aeropuertos.paperlessmobile.OktaManager
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Fecha
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.observeOnce
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ActivityMainBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.viewModel.MainViewModel
import com.aeromexico.aeropuertos.paperlessmobile.login.Repositorio.UserRepository
import com.aeromexico.aeropuertos.paperlessmobile.login.ViewModel.LoginViewModel
import com.aeromexico.aeropuertos.paperlessmobile.login.view.LoginActivity
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.viewModel.MensajesOperacionalesViewModel
import com.aeromexico.aeropuertos.paperlessmobile.notoc.NotocViewModel
import com.aeromexico.aeropuertos.paperlessmobile.webService.Result
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val oktaManager: OktaManager by lazy { (application as PaperlessApplication).oktaManager }

    //ViewModels
    private lateinit var viewModel: MainViewModel
    private lateinit var mViewModelMO: MensajesOperacionalesViewModel

    lateinit var userStorage: DataStorageLogin
    lateinit var navController: NavController
    val REQUEST_LOGIN = 100
    var sessionActive = MutableLiveData<Boolean>()
    lateinit var dialogo: Dialogo
    lateinit var model: LoginViewModel

    //Notificaciones
    var mCantidadNotificaciones: Int = 0;
    var cantMOPendientes: Int = 0
    var pendIA: Int = 0
    var pendInpAeroPrimerVuelo = 0
    var pendInspEquipo = 0
    var pendInspecAeronave = 0
    var penDeshielo = 0
    var pendientesOrdenCarga = 0
    var pendientesNotoc = 0
    lateinit var loggedUser: Usuario


    fun setLocate(Lang: String) {
        dialogo = Dialogo(this)
        dialogo.mostrarPregunta(
            getString(R.string.cambio_idioma_label),
            getString(R.string.question_cambio_idioma)
        )
        dialogo.btnAceptar.setOnClickListener {
            finish()
            startActivity(intent)
        }
        dialogo.btnCerrar.setOnClickListener { dialogo.Ocultar() }

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            createConfigurationContext(config)

        } else {
            config.locale = locale

        }
        resources.updateConfiguration(config,  resources.displayMetrics)


        val editor =  getSharedPreferences("Settings", Context.MODE_PRIVATE)!!.edit()

        editor.putString("My_Lang", Lang)
        editor.apply()



    }

     fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences?.getString("My_Lang", "")
        if (language == "") {
            val locale = resources.configuration.locale
            Locale.setDefault(locale)

            val config = Configuration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale)
                createConfigurationContext(config)

            } else {
                config.locale = locale

            }
            resources.updateConfiguration(config,  resources.displayMetrics)


            val editor =  getSharedPreferences("Settings", Context.MODE_PRIVATE)!!.edit()

            editor.putString("My_Lang", language)
            editor.apply()
        }else {
            val locale =Locale(language)
            Locale.setDefault(locale)

            val config = Configuration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale)
                createConfigurationContext(config)

            } else {
                config.locale = locale

            }
            resources.updateConfiguration(config,  resources.displayMetrics)


            val editor =  getSharedPreferences("Settings", Context.MODE_PRIVATE)!!.edit()

            editor.putString("My_Lang", language)
            editor.apply()
        }
    }

    companion object {
        var instnce: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instnce
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadLocate()
        instnce = this
        dialogo = Dialogo(this)
        userStorage = DataStorageLogin(this)
        navController = Navigation.findNavController(this, R.id.fragmentMO)

        sessionActive.observe(this, Observer {
            if (it == false) {
                var intent = Intent(this, LoginActivity::class.java)
                startActivityForResult(intent, REQUEST_LOGIN)
                this.finish()
            } else if (it == true) {

            }
        })
        updateCampanita()

    }

    override fun onResume() {
        super.onResume()
        VerificarInicioDeSession()
    }

    fun logOut() {
        GlobalScope.launch {
            userStorage.guardarUserLogin(
                Result(
                    Usuario(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false,
                        "",
                        "",
                        "",
                        ""
                    ), ""
                ), "0"
            )
        }
        PaperlessApplication.alarm.cancelAlarm()
        sessionActive.value = false

    }

    private fun VerificarInicioDeSession() {
        userStorage.getTokenSession.asLiveData().observe(this, Observer {
            if (it != DataStorageLogin.NOT_DATA) {
                Log.i("Token ", "Cambiado")
                PaperlessApplication.tokenSession = it
            }
        })

        userStorage.getUserLogin.asLiveData().observe(this, Observer {
            if (it == null || it?.nombre == "") {
                sessionActive.postValue(false)
            } else {
                sessionActive.postValue(true)
                loggedUser = it
               // Snackbar.make(binding.root, "Hola: ${it.nombre}", Snackbar.LENGTH_SHORT).show()
            }
        })
        userStorage.getLastrenovationTime.asLiveData().observe(this, Observer {
            if (Fecha().CompararFechas(it) > 180) {
                mostrarDialogoSessionExpirada()
            } else if (Fecha().CompararFechas(it) in 170..179) {
                PaperlessApplication.alarm.cancelAlarm()
                val c: Calendar = Calendar.getInstance()
                c.add(Calendar.SECOND,15)
                PaperlessApplication.alarm.startAlarm(c, this)
            } else {
                PaperlessApplication.alarm.cancelAlarm()
                val c: Calendar = Calendar.getInstance()
                c.add(Calendar.MINUTE,(170 - Fecha().CompararFechas(it)))
                PaperlessApplication.alarm.startAlarm(c, this)
            }
        })
    }

    fun getUsuarioLogeado(): Usuario {
        return loggedUser
    }

    private fun mostrarDialogoSessionExpirada() {
        dialogo = Dialogo(this)
        dialogo.mostrarError(getString(R.string.sesion_expirada), getString(R.string.tiempo_agotado_debes_volver_a_iniciar_sesion))
        dialogo.btnCerrar.setOnClickListener {
            dialogo.Ocultar()
            logOut()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                navController.navigate(R.id.action_global_appInformacionFragment)
            }
            R.id.item_campana -> {
                navController.navigate(R.id.action_global_appPendientesFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun updateCampanita() {

//        mBuscarVueloViewModel = ViewModelProvider(this).get(BuscarVueloViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getCuenta().observe(this, {
            MandarnumeritoAcampanita()
        })
        viewModel.getFirmas().observe(this, {
            cantMOPendientes = it.size
            MandarnumeritoAcampanita()
        })
        viewModel.getCambioFirmas().observe(this, {
            viewModel.loadFirmasFaltantes()
            Log.i("Cambi de firma", it.toString())
            MandarnumeritoAcampanita()
        })
        viewModel.getEquiposPendientes().observe(this,{
            pendInspEquipo = it.size
            MandarnumeritoAcampanita()
        })
        viewModel.getDeshieloPendientes().observe(this, Observer {
            penDeshielo = it.size
            MandarnumeritoAcampanita()
        })
        viewModel.getCargaCombustibleCount().observe(this, Observer {
            pendientesOrdenCarga = it.size
            MandarnumeritoAcampanita()
        })


        viewModel.getInspecAeronaveCount().observe(this, {
            pendInspecAeronave = it
            MandarnumeritoAcampanita()
            Log.i("inspec aeronave", "estoy aqui")
        })
        viewModel.getPrimerVueloCount().observe(this,{
            pendInpAeroPrimerVuelo = it.size
            MandarnumeritoAcampanita()
        })

        viewModel.getNotocCount().observe(this){
            pendientesNotoc = it
            MandarnumeritoAcampanita()
        }

    }

    private fun MandarnumeritoAcampanita() {

        mCantidadNotificaciones =
            cantMOPendientes + pendIA + pendInpAeroPrimerVuelo + pendInspEquipo + pendInspecAeronave + penDeshielo + pendientesOrdenCarga + pendientesNotoc
    }


    //Campana de notificaciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.campana, menu);

        val menuItem = menu!!.findItem(R.id.item_campana);
        val actionView = menuItem.actionView;
        val badge_notificaciones = actionView?.findViewById<View>(R.id.campana_badge) as TextView;

        if (badge_notificaciones != null) {
            if (this.mCantidadNotificaciones == 0) {
                if (badge_notificaciones.visibility != View.GONE) {
                    badge_notificaciones.visibility = (View.GONE)
                }
            } else {
                badge_notificaciones.text = this.mCantidadNotificaciones.toString();
                if (badge_notificaciones.visibility != View.VISIBLE) {
                    badge_notificaciones.visibility = (View.VISIBLE)
                }
            }
        }

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }

        return true
    }
    var mostrarRenovDialog = true

    fun mostrarDialogoSessionExpiracion() {
        // timer de 10 minutos si no dan alguna opcion se cierra la session
        if (mostrarRenovDialog){
            mostrarRenovDialog = false
            var dialogo2 = Dialogo(this)
            model =
                LoginViewModel(
                    UserRepository(
                        WebServiceApi().getUserApi()
                    ),oktaManager
                )

            dialogo2.mostrarPregunta(
                getString(R.string.sesion_por_expirar),
                getString(R.string.desea_continuar_con_la_sesion_activa)
            )
            dialogo2.btnCerrar.visibility = View.GONE
            dialogo2.btnAceptar.setOnClickListener {
               // dialogo2.btnCerrar.visibility= View.VISIBLE
                dialogo2.Ocultar()
                userStorage.getLastrenovationTime.asLiveData().observeOnce(Observer {
                    if (Fecha().CompararFechas(it) > 60) {
                        mostrarDialogoSessionExpirada()
                    } else {
                        model.renovarToken()
                        Log.i("debug", "renovando token ")
                        // renovar token
                        observadorRenovacionToken()
                    }
                })

            }
            dialogo2.btnCerrar.setOnClickListener {
              //  dialogo2.btnCerrar.visibility= View.VISIBLE
                dialogo2.Ocultar()
                PaperlessApplication.alarm.close()
            }
        }


    }

    private fun observadorRenovacionToken() {
        dialogo = Dialogo(this)
        dialogo.mostrarCargando(null)
        model.responseToken.observeOnce {
            dialogo.Ocultar()
            mostrarRenovDialog = true
            if (it != null) {
                // Snackbar.make(binding.root, " ${it.message}", Snackbar.LENGTH_SHORT).show()
                if (it.result?.token != null) {
                    Log.i("debug", " token respuesta ")
                    PaperlessApplication.alarm.cancelAlarm()
                    val c: Calendar = Calendar.getInstance()
                    //   PaperlessApplication.alarm.startAlarm(c, this)
                    GlobalScope.launch {
                        userStorage.guardarToken(it.result.token, c.timeInMillis.toString())
                    }
                    //VerificarInicioDeSession()
                } else {
                    mostrarDialogoSessionExpiracion()
                }
            } else {
                mostrarDialogoSessionExpiracion()
            }
        }
    }
}