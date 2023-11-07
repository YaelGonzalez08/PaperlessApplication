package com.aeromexico.aeropuertos.paperlessmobile.login.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeromexico.aeropuertos.paperlessmobile.OktaManager
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.login.DataSource.LoginDataSource
import com.aeromexico.aeropuertos.paperlessmobile.login.Repositorio.UserRepository
import com.aeromexico.aeropuertos.paperlessmobile.login.pojos.DominiosResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.LoginMainReponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.TokenResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository,private var oktaManager: OktaManager) : ViewModel() {

    lateinit var loginUser: String
    lateinit var passwordUser: String
    lateinit var responseLogin: MutableLiveData<LoginMainReponse>
    lateinit var responseToken: MutableLiveData<TokenResponse>

    var userLoginDataSource: LoginDataSource = repository.requestLoginDataSource()

    //response state
    var responseState= MutableLiveData<RequestState>()

    init {
        userLoginDataSource = repository.requestLoginDataSource()
        responseState = userLoginDataSource.responseState
    }

    fun IniciarSession() {
        responseLogin = userLoginDataSource.responseLogin
        userLoginDataSource.requestLogin(loginUser, passwordUser)
    }

    fun renovarToken() {
        responseToken = userLoginDataSource.responseToken
        userLoginDataSource.requesRenewToken()
    }

    fun getDomains(): MutableLiveData<DominiosResponse> {
        userLoginDataSource.requestGetDomains()
        return userLoginDataSource.responseDomians

    }
    fun getTokensOkta(): MutableLiveData<String?> {
        var token = MutableLiveData<String?>()
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        viewModelScope.launch {
            delay(2000)
            responseState.value = RequestState(RequestState.REQ_OK)
            if (oktaManager.isAuthenticated())
            token.postValue(oktaManager.gettoken()?.accessToken.toString())
            else
                token.postValue("")
        }
        return token
    }
}