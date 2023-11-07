package com.aeromexico.aeropuertos.paperlessmobile
import android.app.Activity
import android.content.Context
import com.okta.oidc.*
import com.okta.oidc.clients.sessions.SessionClient
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.net.response.UserInfo
import com.okta.oidc.storage.security.DefaultEncryptionManager
import com.okta.oidc.util.AuthorizationException

class OktaManager(applicationContext: Context,var webAuth: WebAuthClient,var sessionClient: SessionClient) {


    fun isAuthenticated(): Boolean {
        return sessionClient.isAuthenticated
    }

    fun registerWebAuthCallback(callback: ResultCallback<AuthorizationStatus, AuthorizationException>, activity: Activity) {
        webAuth.registerCallback(callback, activity)
    }

    fun registerUserProfileCallback(callback: RequestCallback<UserInfo, AuthorizationException>) {
        sessionClient.getUserProfile(callback)
    }
    fun gettoken(): Tokens? {
        return sessionClient.tokens
    }

    fun signIn(activity: Activity, payload: AuthenticationPayload) {
        webAuth.signIn(activity, payload)
    }


    fun signOut(activity: Activity, callback: RequestCallback<Int, AuthorizationException>) {
        webAuth.signOut(activity, callback)
    }

    fun clearUserData() {
        sessionClient.clear()
    }
}
