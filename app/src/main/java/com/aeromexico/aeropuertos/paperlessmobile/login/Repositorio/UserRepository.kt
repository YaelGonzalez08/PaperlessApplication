package com.aeromexico.aeropuertos.paperlessmobile.login.Repositorio

import com.aeromexico.aeropuertos.paperlessmobile.login.DataSource.LoginDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.UserApi

class UserRepository(private val userApi: UserApi) {

    fun requestLoginDataSource(): LoginDataSource {
        return LoginDataSource(
            userApi
        )
    }

}