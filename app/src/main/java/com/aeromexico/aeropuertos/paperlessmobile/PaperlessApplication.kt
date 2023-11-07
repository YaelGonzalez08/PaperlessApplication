package com.aeromexico.aeropuertos.paperlessmobile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.utilities.Printooth
import com.aeromexico.aeropuertos.paperlessmobile.common.database.PaperlessAPI
import com.aeromexico.aeropuertos.paperlessmobile.common.database.DB.PaperlessDatabase
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.AppContextSIngleton
import com.aeromexico.aeropuertos.paperlessmobile.home.AlarmaRenovacionSession
import com.gu.toolargetool.TooLargeTool
import com.okta.oidc.OIDCConfig
import com.okta.oidc.Okta
import com.okta.oidc.storage.security.DefaultEncryptionManager

class PaperlessApplication: Application() {
    companion object{
        lateinit var contextAppSingleton: Context
        lateinit var database: PaperlessDatabase
        lateinit var paperlessAPI: PaperlessAPI
        var tokenSession:String = ""
        lateinit var alarm:AlarmaRenovacionSession
    }
    lateinit var oktaManager: OktaManager

    override fun onCreate() {
        super.onCreate()

      /*  val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("Not yet implemented")
                database.execSQL("drop table FlightEntity")
            }
        }*/
        TooLargeTool.startLogging(this)
        database = PaperlessDatabase.getDatabase(this)
       /* database = Room.databaseBuilder(this,
            PaperlessDatabase::class.java,
            "PaperlessDatabase")
//                .addMigrations(MIGRATION_1_2)
            .build()*/

        //Volley
        paperlessAPI = PaperlessAPI.getInstance(this)
        contextAppSingleton = AppContextSIngleton.getInstance(this.applicationContext)!!
        alarm = AlarmaRenovacionSession.getInstance()!!
        Printooth.init(this)

        val config = OIDCConfig.Builder()
            .clientId("0oa9mnxdybpahBDCt5d7")
            .redirectUri("com.okta.dev-95080594:/callback")
            .endSessionRedirectUri("com.okta.dev-95080594:/logout")
            .scopes("openid", "profile", "offline_access")
            .discoveryUri("https://dev-95080594.okta.com/oauth2/default")
            .create()
        var webAuth = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(this)
            .withCallbackExecutor { command ->
                Log.d(
                    "withCallbackExecutor",
                    "${command.toString()}"
                )
            }
            .withEncryptionManager(DefaultEncryptionManager(this))
            .setRequireHardwareBackedKeyStore(false)
            .create()
        var sessionClient = webAuth.sessionClient

        oktaManager = OktaManager(this,webAuth,sessionClient)

    }
}