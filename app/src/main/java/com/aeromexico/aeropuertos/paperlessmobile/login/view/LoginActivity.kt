package com.aeromexico.aeropuertos.paperlessmobile.login.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.OktaManager
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.DialogPrintUtil
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage.DataStorageLogin
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.*
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ActivityLoginBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.aeromexico.aeropuertos.paperlessmobile.login.ViewModel.LoginViewModel
import com.aeromexico.aeropuertos.paperlessmobile.login.Repositorio.UserRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Result
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi
import com.google.android.material.snackbar.Snackbar
import com.okta.oidc.AuthenticationPayload
import com.okta.oidc.AuthorizationStatus
import com.okta.oidc.RequestCallback
import com.okta.oidc.ResultCallback
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.util.AuthorizationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ng.softcom.android.utils.ui.showSnackBar
import ng.softcom.android.utils.ui.showToast
import okhttp3.internal.filterList
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : AppCompatActivity(),
    RequestCallback<UserInfo, AuthorizationException> {
    private val oktaManager: OktaManager by lazy { (application as PaperlessApplication).oktaManager }
    lateinit var payload: AuthenticationPayload

    lateinit var binding: ActivityLoginBinding
    lateinit var model: LoginViewModel
    lateinit var userStorage: DataStorageLogin
    lateinit var dialogo: Dialogo
    lateinit var listDominio: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        dialogo = Dialogo(this)
        PaperlessApplication.tokenSession = ""
        payload = AuthenticationPayload.Builder().build()

        listDominio = arrayListOf(resources.getString(R.string.select_domian))
        model =
            LoginViewModel(
                   UserRepository(
                    WebServiceApi().getUserApi()
                ),oktaManager
            )
        userStorage = DataStorageLogin(this)
        observers()
        binding.btnLogin.setOnClickListener {
            if (verificarCampos()) {
                dialogo.mostrarCargando("")
                model.IniciarSession()
                observadorIniciarSession()
            }
        }
        binding.apply {
            if (BuildConfig.PAPERLESS_AMBIENT.contains("dev")) {
                tvAmbient.text = "Desarrollo"

            } else {
                tvAmbient.text = "Productivo"

            }
            tvVersion.text = BuildConfig.VERSION_NAME
            /*
            oktaLogin.root.setOnClickListener {
                toast("isAuth ${oktaManager.isAuthenticated()} ${oktaManager.gettoken()?.accessToken.toString()}")
                desactiveUserPw(true)
                oktaManager.registerWebAuthCallback(getCAllbackWebAth(), this@LoginActivity)
                oktaManager.signIn(this@LoginActivity, payload)
            }
            ivPaperless.setOnClickListener {
                toast("isAuth ${oktaManager.isAuthenticated()} ${oktaManager.gettoken()?.accessToken.toString()}")
                oktaManager.signOut(this@LoginActivity, getSignOutCallback())
            }
            tvLogin.setOnClickListener {
                toast("isAuth ${oktaManager.isAuthenticated()} ${oktaManager.gettoken()?.accessToken.toString()}")
                oktaManager.registerUserProfileCallback(this@LoginActivity)
            }
             */
        }
        desactiveUserPw(true)
        model.getDomains().observeOnce(Observer {
            if (it != null) {
                if (it.result != null) {
                    if (!it.result.dominios.isNullOrEmpty()) {
                        desactiveUserPw(false)
                        var listDomaians = it.result.dominios
                        listDomaians.filter { d -> d.activo }
                        listDomaians.forEach { s -> listDominio.add(s.dominio) }

                        binding.spinnerDominio.root.background =
                            resources.getDrawable(R.color.color_gray_white_background_checklist)
                        binding.spinnerDominio.spinner.adapter = ArrayAdapter(
                            baseContext,
                            android.R.layout.simple_list_item_1,
                            listDominio
                        )

                    }
                }
            }
        })

    }

    private fun observadorIniciarSession() {
        model.responseLogin.observe(this, Observer {
            dialogo.Ocultar()
            if (it != null) {
                //Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT)
                    .show();
                if (it.status == RequestState.REQ_OK && it.result != null) {
                    GuardarSession(it.result)
                }
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_conexion),
                    Snackbar.LENGTH_SHORT
                )
                    .show();
            }
        })
    }

    private fun getCAllbackWebAth(): ResultCallback<AuthorizationStatus, AuthorizationException> {
        return object : ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onSuccess(result: AuthorizationStatus) {
                desactiveUserPw(false)
                Log.d("LoginActivity", "result -> ${result.toString()}")
                when (result) {
                    AuthorizationStatus.AUTHORIZED -> Log.d(
                        "LoginActivity",
                        "Login succes ${oktaManager.gettoken()}"
                    )
                    AuthorizationStatus.SIGNED_OUT -> Log.d("LoginActivity", "Signed out")
                    AuthorizationStatus.CANCELED -> Log.d("LoginActivity", "Canceled")
                    AuthorizationStatus.ERROR -> Log.d("LoginActivity", "Error")
                    AuthorizationStatus.EMAIL_VERIFICATION_AUTHENTICATED -> Log.d(
                        "LoginActivity",
                        "Email verification authenticated"
                    )
                    AuthorizationStatus.EMAIL_VERIFICATION_UNAUTHENTICATED -> Log.d(
                        "LoginActivity",
                        "Email verification unauthenticated"
                    )
                }
            }

            override fun onCancel() {
                desactiveUserPw(false)
                Log.d("LoginActivity", "Canceled")
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                desactiveUserPw(false)
                Log.d("LoginActivity", "Error: $msg  ${exception?.errorDescription}")
            }
        }
    }

    private fun desactiveUserPw(b: Boolean) {
        binding.apply {
            etEmail.isEnabled = !b
            etPassword.isEnabled = !b
            spinnerDominio.spinner.isEnabled = !b
            btnLogin.isEnabled = !b
        }
    }

    fun observers() {

        model.responseState.observe(this, Observer {
            if (it.state == RequestState.REQ_IN_PROGRESS) {
                dialogo.mostrarCargando("")
                binding.btnLogin.isEnabled = false
                binding.btnLogin.alpha = .5f
                binding.etPassword.hideKeyboard()
            } else if (it.state == RequestState.REQ_BAD || it.state == RequestState.REQ_OK) {
                dialogo.Ocultar()
                binding.btnLogin.isEnabled = true
                binding.btnLogin.alpha = 1f
            }
        })
    }

    private fun GuardarSession(result: Result) {
        /*val c: Calendar = Calendar.getInstance()
        PaperlessApplication.alarm.startAlarm(c,this)*/
        GlobalScope.launch {
            userStorage.guardarUserLogin(result, Calendar.getInstance().timeInMillis.toString())
        }
        userStorage.getTokenSession.asLiveData().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                //aqui agregar el finish para cerrar esta ventana
                setResult(Activity.RESULT_OK, intent)
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun verificarCampos(): Boolean {

        var s = binding.spinnerDominio.spinner.selectedItemPosition
        if (s <= 0) {
            showToast("${resources.getString(R.string.field)}")
            return false
        }else{
            model.loginUser = "${binding.etEmail.text.toString().trim()}${listDominio[s]?:""}"
        }

        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(model.loginUser).matches()
        ) {
            if (!binding.etPassword.text.isNullOrEmpty()) {
                model.passwordUser = binding.etPassword.text.toString()
                true
            } else {
                binding.etPassword.error = getString(R.string.error_campo_invalido)
                false
            }
        } else {
            binding.etEmail.error = getString(R.string.error_campo_invalido)
            false
        }
    }

    private fun getUserProfileCallback(): RequestCallback<UserInfo, AuthorizationException> {
        return object : RequestCallback<UserInfo, AuthorizationException> {
            override fun onSuccess(result: UserInfo) {
                var token = oktaManager.gettoken()
                println(token?.accessToken.toString())
                Log.d("HomeActivity", "accessToken: ${result.toString()}")

                binding.etEmail.setText("Hello, ${result.toString()}!")
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                Log.d("HomeActivity", "Error: $msg")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        /*
        model.getTokensOkta().observe(this, Observer {
            if(it.isNullOrEmpty()){
                desactiveUserPw(false)
            }else{
                toast(it)
            }
        })
         */
        changeSplashScreem(false)

    }
    override fun onPause() {
        super.onPause()
        changeSplashScreem(true)
    }
    private fun changeSplashScreem(isShow: Boolean) {

        binding.apply {
            if (isShow) {
                splashPause.root.visibility = View.VISIBLE

            } else {
                model.waithSeconds().observeOnce {
                    splashPause.root.visibility = View.GONE
                }

            }
        }
    }

    private fun getSignOutCallback(): RequestCallback<Int, AuthorizationException> {
        return object : RequestCallback<Int, AuthorizationException> {
            override fun onSuccess(result: Int) {
                oktaManager.clearUserData()
                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                Log.d("HomeActivity", "Error: $msg")
            }
        }
    }

    override fun onSuccess(result: UserInfo) {
        var token = oktaManager.gettoken()
        println(token?.accessToken.toString())
        Log.d("HomeActivity", "accessToken: ${result.toString()}")
        binding.etEmail.setText("Hello, ${result.toString()}!")
    }

    override fun onError(error: String?, exception: AuthorizationException?) {
        Log.d("HomeActivity", "Error: $error")
    }

}
