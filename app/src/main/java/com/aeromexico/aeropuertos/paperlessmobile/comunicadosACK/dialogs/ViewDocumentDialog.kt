package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.dialogs

import Documentos
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.CreateImageFile
import com.aeromexico.aeropuertos.paperlessmobile.databinding.ViewDocumentDialogBinding
import com.davemorrissey.labs.subscaleview.ImageSource
import org.jetbrains.anko.toast
import java.io.*


class ViewDocumentDialog(var doc: Documentos) : DialogFragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    lateinit var binding: ViewDocumentDialogBinding
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQUEST_CHOOSE_PICTURE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                binding.titleDocument.setText("${doc.titulo}${doc.extension}")
                if (doc.archivo?.isNotEmpty() == true) {

                    when {
                        doc.ruta.contains("pdf") -> {
                            cargarPdf(doc.archivo ?: "")
                        }
                        doc.ruta.contains("jpeg") || doc.ruta.contains("png") -> {
                            cargarImagen(doc.archivo ?: "")
                        }
                        doc.ruta.contains("xlsx") -> {
                            cargarXls(doc.archivo ?: "")
                        }
                        doc.ruta.contains("docx") -> {
                            cargarDocs(doc.archivo ?: "")
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Intente descargar el archivo de nuevo", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "No hay permiso para acceder al almacenamiento interno",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun cargarDocs(archivob64: String) {

        var filePDF: File? = CreateImageFile.getXLXSFIleFromB64Encode(
            requireActivity(),
            archivob64,
            doc.titulo + ".docx"
        )
        val targetUri = Uri.fromFile(filePDF)
        val pop: Intent = Intent(
            Intent.ACTION_VIEW,
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                filePDF!!
            )
        )

        val getUriForFile = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider", filePDF
        )


        val uri = Uri.fromFile(filePDF)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/msword")

        val pm: PackageManager = requireContext().packageManager
        val list = pm.queryIntentActivities(intent, 0)
        if (list.size > 0) startActivity(intent)
        this@ViewDocumentDialog.dismiss()
    }

    private fun cargarImagen(imgb64: String) {
        binding.imageView.visibility = View.VISIBLE
        var bitmap = CreateImageFile.setBitmapFromB64String(imgb64)

        binding.imageView.setImage(ImageSource.bitmap(bitmap));

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.aeromexico.aeropuertos.paperlessmobile.R.style.DialogFull)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewDocumentDialogBinding.inflate(inflater, container, false)

        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Constants.PERMISSION_READ_STORAGE
        )
        this.requestPermissions(
            arrayOf(Constants.PERMISSION_READ_STORAGE),
            Constants.REQUEST_CHOOSE_PICTURE
        )
        binding.apply {
            closeDialog.setOnClickListener {
                this@ViewDocumentDialog.dismiss()
            }
            btnAceptar.setOnClickListener {
                this@ViewDocumentDialog.dismiss()
            }
        }

        return binding.root

    }

    private fun cargarPdf(fileb64: String) {
        binding.pdf.visibility = View.VISIBLE

        if (!fileb64.isEmpty()) {
            var filePDF: File? = CreateImageFile.getPDFFIleFromB64Encode(
                fileb64,
                doc.titulo + ".pdf"
            )
            if (filePDF == null) {
                filePDF = CreateImageFile.getPDFFIleFromB64EncodeAlternativeFolder(
                    fileb64,
                    doc.titulo + ".pdf"
                )
                if (filePDF == null) {
                    requireContext().toast("Error al Cargar PDF")

                } else {
                    binding.pdf.fromFile(filePDF)
                        .enableAnnotationRendering(true)
                        .defaultPage(0)
                        .swipeHorizontal(false)
                        .spacing(12)
                        .enableAntialiasing(true)
                        .load()
                }

            } else {
                binding.pdf.fromFile(filePDF)
                    .enableAnnotationRendering(true)
                    .defaultPage(0)
                    .swipeHorizontal(false)
                    .spacing(12)
                    .enableAntialiasing(true)
                    .load()
            }
        }
    }

    private fun cargarXls(archivob64: String) {
        binding.webview.visibility = View.VISIBLE


        var filePDF: File? = CreateImageFile.getXLXSFIleFromB64Encode(
            requireActivity(),
            archivob64,
            doc.titulo + ".xlsx"
        )
        val targetUri = Uri.fromFile(filePDF)
        val pop: Intent = Intent(
            Intent.ACTION_VIEW,
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                filePDF!!
            )
        )

        val getUriForFile = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider", filePDF
        )


        val uri = Uri.fromFile(filePDF)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.ms-excel")

        val pm: PackageManager = requireContext().packageManager
        val list = pm.queryIntentActivities(intent, 0)
        if (list.size > 0) startActivity(intent)

        val url = "$getUriForFile"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        //  startActivity(i)

        val intento = Intent(Intent.ACTION_GET_CONTENT)
        intent.setDataAndType(uri, "file/*")
        // startActivity(intento)

        /*      val targetUri = Uri.fromFile(filePDF)
              val intent = Intent(Intent.ACTION_VIEW)
              intent.setDataAndType(targetUri, "application/*")
              startActivityForResult(intent, 0)
      */*/

        //   var doc = "<iframe src='https://docs.google.com/viewer?url=http://atosdev.gam-aeropuertos.com/api/Test/GetPDF_DirectURL/1&embedded=true' width='100%' height='100%' style='border: none;'></iframe>"
        // var doc = "<iframe src='https://docs.google.com/viewer?url=https://es.justexw.com/wp-content/uploads/sites/2/Plantilla-de-Excel-gratuita-Cronograma-de-actividades.xls&embedded=true' width='100%' height='100%' style='border: none;'></iframe>"
        //var doc = "<iframe src='https://docs.google.com/viewer?url=$targetUri&embedded=true' width='100%' height='100%' style='border: none;'></iframe>"
        //    var doc = "<iframe src='data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,$archivob64' height=\"100%\" width=\"100%\"></iframe>"
        var test =
            "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,$archivob64"

        var doc =
            "<iframe src='data:application/vnd.ms-excel;base64;archivob64' width='100%' height='100%'></iframe>"


        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setAllowFileAccess(true);
        //   binding.webview.loadUrl("file:///android_asset/$targetUri");
        //  binding.webview.loadData(doc, "text/html", "UTF-8")
        this@ViewDocumentDialog.dismiss()


    }

}