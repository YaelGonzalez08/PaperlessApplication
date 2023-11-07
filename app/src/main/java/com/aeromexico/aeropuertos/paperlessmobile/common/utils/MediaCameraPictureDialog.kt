package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.MediaCameraPictureDialogBinding

class MediaCameraPictureDialog(var imgPreview: String?="",val imgListener: (img: Bitmap) -> Unit) : DialogFragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private var umgURI: Uri? = null
    lateinit var binding: MediaCameraPictureDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MediaCameraPictureDialogBinding.inflate(inflater, container, false)

        binding.apply {
            btnTakeCamera.setOnClickListener {
                takePhotoCamaraWithPermissions()
            }
            btnTakeGaleria.setOnClickListener {
                selectPhotoWithPermissions()
            }
            closeDialog.setOnClickListener {
                this@MediaCameraPictureDialog.dismiss()
            }
            if(!imgPreview.isNullOrEmpty()){
                showPreview(CreateImageFile.setBitmapFromB64String(imgPreview!!))
            }
        }

        return binding.root

    }

    //Resultados de la actividad (se obtuvo una imagen)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val options = BitmapFactory.Options()
        options.inSampleSize = 8

        if (requestCode == Constants.REQUEST_TAKE_PICTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = Uri.parse(CreateImageFile.getURIImageFile())
            umgURI = uri
            val stream = requireActivity().contentResolver.openInputStream(uri)
            val imageBitmap = BitmapFactory.decodeStream(stream, null, options) as Bitmap
            showPreview(imageBitmap)

        }
        if (requestCode == Constants.REQUEST_CHOOSE_PICTURE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                umgURI = uri
                val stream = requireActivity().contentResolver.openInputStream(uri)
                val imageBitmap = BitmapFactory.decodeStream(stream, null, options) as Bitmap
                showPreview(imageBitmap)
            }
        }

    }

    private fun showPreview(imageBitmap: Bitmap) {

        binding.apply {
            contendPhoto.visibility = View.VISIBLE
            btnAceptar.visibility = View.VISIBLE
            image.setImageBitmap(imageBitmap)

            btnAceptar.setOnClickListener {
                imgListener(imageBitmap)
                this@MediaCameraPictureDialog.dismiss()
            }
        }

    }

    private fun selectPhotoWithPermissions() {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Constants.PERMISSION_READ_STORAGE
        )
        this.requestPermissions(
            arrayOf(Constants.PERMISSION_READ_STORAGE),
            Constants.REQUEST_CHOOSE_PICTURE
        )
    }

    private fun takePhotoCamaraWithPermissions() {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Constants.PERMISSION_CAMERA
        )
        this.requestPermissions(
            arrayOf(
                Constants.PERMISSION_CAMERA,
                Constants.PERMISSION_WRITE_STORAGE,
                Constants.PERMISSION_READ_STORAGE, Constants.PERMISSION_FINE_LOCATION
            ), Constants.REQUEST_TAKE_PICTURE
        )

    }

    //Validar permisos y lanzar actividad (cámara o galería)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.REQUEST_TAKE_PICTURE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED
                ) {
                    // tenemos permiso
                    launchTakePicture()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "No hay permiso para acceder a la cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Constants.REQUEST_CHOOSE_PICTURE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // tenemos permiso
                    launchChoosePicture()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "No hay permiso para acceder al almacenamiento inerno",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun launchChoosePicture() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, Constants.REQUEST_CHOOSE_PICTURE)
    }

    private fun launchTakePicture() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = CreateImageFile.crearArchivoDeImagen()
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    requireActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, Constants.REQUEST_TAKE_PICTURE)
            }
        } catch (e: Exception) {
            throw(e)
        }

    }
}