package com.aeromexico.aeropuertos.paperlessmobile.metar

import android.R.attr
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Dialogo
import com.aeromexico.aeropuertos.paperlessmobile.databinding.FragmentACARSBinding
import im.delight.android.webview.AdvancedWebView
import android.content.Intent
import android.net.Uri

import android.webkit.DownloadListener
import android.widget.Toast

import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.content.Context.DOWNLOAD_SERVICE

import androidx.core.content.ContextCompat.getSystemService

import android.app.DownloadManager
import android.content.Context

import android.os.Environment
import androidx.core.content.ContextCompat
import java.util.*
import android.content.Context.DOWNLOAD_SERVICE

import androidx.core.content.ContextCompat.getSystemService

import android.R.attr.mimeType
import android.webkit.CookieManager
import android.webkit.URLUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import android.os.AsyncTask
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity

import android.content.ActivityNotFoundException
import android.icu.util.MeasureUnit.MEGABYTE
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication


class ACARSFragment : Fragment(), AdvancedWebView.Listener {
    lateinit var binding: FragmentACARSBinding
    lateinit var dialog: Dialogo


    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentACARSBinding.inflate(layoutInflater)

        dialog = Dialogo(requireContext())


        binding.tvMensajesOperacionales.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.webview.setListener(activity, this)

        showDialogOptions()

        return binding.root
    }
    override fun onStop() {
        super.onStop()
        binding.webview.onDestroy()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
    private fun showDialogOptions() {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Selecciona una opción").setCancelable(false)

// add a list
        val animals = arrayOf("Aeroméxico", "Connect")

        builder.setItems(animals) { dialog, which ->
            when (which) {
                0 -> {
                    webview("https://am1.flitebrief-stage.aero/login")
                }
                1 -> {
                    webview("https://sl1.flitebrief-stage.aero/login")
                }
            }
        }

// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }


    private class DownloadFile :
        AsyncTask<String?, Void?, Void?>() {

        override fun doInBackground(vararg params: String?): Void? {
             val MEGABYTE = 1024 * 1024


            val fileUrl = params[0] // -> http://maven.apache.org/maven-1.x/maven.pdf
            val fileName = params[1] // -> maven.pdf

            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val folder = File(extStorageDirectory, "testthreepdf")


            folder.mkdir()

         //   val pdfFile = File(folder, fileName)

            val pdfFile = File(PaperlessApplication.contextAppSingleton.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),fileName)
            try {
                pdfFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                val url = URL(fileUrl)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect()
                val inputStream: InputStream = urlConnection.getInputStream()
                val fileOutputStream = FileOutputStream(pdfFile)
                val totalSize: Int = urlConnection.getContentLength()
                val buffer = ByteArray(MEGABYTE)
                var bufferLength = 0
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength)
                }
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }

    private fun webview(url: String) {
        binding.webview.settings.allowFileAccess= true
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.setMixedContentAllowed(true)
        binding.webview.loadUrl(url)
        binding.webview.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->

            DownloadFile().execute(url, contentDisposition.toString().substringAfter("''"))

            Toast.makeText(
                requireContext(),
                "Downloading File",  //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG
            ).show()



        })
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        dialog.mostrarCargando(null)
    }

    override fun onPageFinished(url: String?) {
        dialog.Ocultar()
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        dialog.mostrarError("Error",description)
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String?) {

    }

}