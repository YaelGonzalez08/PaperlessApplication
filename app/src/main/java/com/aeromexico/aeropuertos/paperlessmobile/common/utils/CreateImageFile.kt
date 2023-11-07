package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PDFB64FILEResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateImageFile {
    companion object {

        var image: File? = null
        fun crearArchivoDeImagen(): File {

            try{
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val imageName = "PhotoCamera" + timestamp + "_"
                val directory = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "Camera"
                )
                image = File. createTempFile(imageName, ".jpg", directory)
                return image!!
            }catch(e: Exception){
                throw(e);
            }


        }

        fun getURIImageFile(): String {
            if (image == null) {
                return Uri.fromFile(crearArchivoDeImagen()).toString()
            } else {
                return Uri.fromFile(image).toString()
            }
        }

        fun getB64FromPath(filePath: String): String{
            val bytes = File(filePath).readBytes()
            val base64 = android.util.Base64.encodeToString(bytes, 0);
            return base64
        }

        fun getB64FromBitmap(bitmap: Bitmap): String{
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        }

        fun setBitmapFromB64String(b64:String, quality: Int =0):Bitmap{
            val decode = Base64.decode(b64,Base64.DEFAULT)
            var bitmap: Bitmap
            if(quality !=0){
                val options = BitmapFactory.Options()
                options.inSampleSize = quality
                bitmap = BitmapFactory.decodeByteArray(decode,0,decode.size, options)
            }else{

                bitmap = BitmapFactory.decodeByteArray(decode,0,decode.size)
            }

            return bitmap
        }
        fun getPDFFIleFromB64Encode(result: PDFB64FILEResult): File? {
            // Get the PDF file to encode it into Base64

            try {
                val dwldsPath = File(Environment.getExternalStorageDirectory(), "${result.nombreArchivo}")
                val pdfAsBytes = Base64.decode(result.archivoB64, 0)
                val os: FileOutputStream = FileOutputStream(dwldsPath, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
                Log.d("getPDFFIleFromB64Encode", "File.getPDFFIleFromB64Encode () OK")
                return dwldsPath
            } catch (e: IOException) {
                Log.d("getPDFFIleFromB64Encode", "File.toByteArray() error")
                e.printStackTrace()
                return  null
            }
        }

        fun getPDFFIleFromB64Encode(b64: String, nombre: String): File? {
            // Get the PDF file to encode it into Base64

            try {
                val dwldsPath = File(Environment.getExternalStorageDirectory(), "${nombre}")
                val pdfAsBytes = Base64.decode(b64, 0)
                val os: FileOutputStream = FileOutputStream(dwldsPath, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
                Log.d("getPDFFIleFromB64Encode", "File.getPDFFIleFromB64Encode () OK")
                return dwldsPath
            } catch (e: IOException) {
                Log.d("getPDFFIleFromB64Encode", "File.toByteArray() error")
                e.printStackTrace()
                return  null
            }
        }   fun getPDFFIleFromB64EncodeAlternativeFolder(b64: String, nombre: String): File? {
            // Get the PDF file to encode it into Base64

            try {
                val dwldsPath = File(PaperlessApplication.contextAppSingleton.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${nombre}")
                val pdfAsBytes = Base64.decode(b64, 0)
                val os: FileOutputStream = FileOutputStream(dwldsPath, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
                Log.d("getPDFFIleFromB64Encode", "File.getPDFFIleFromB64Encode () OK")
                return dwldsPath
            } catch (e: IOException) {
                Log.d("getPDFFIleFromB64Encode", "File.toByteArray() error")
                e.printStackTrace()
                return  null
            }
        }
        fun getXLXSFIleFromB64Encode(activi: Activity?, b64: String, nombre: String): File? {
            // Get the PDF file to encode it into Base64

            try {
                // val dir: File = File(activi?.filesDir, "/external_files")
                //  dir.mkdir()
                // val dwldsPath: File = File(dir, "${nombre}")

                val dwldsPath = File(PaperlessApplication.contextAppSingleton.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${nombre}")
                val pdfAsBytes = Base64.decode(b64, 0)
                val os: FileOutputStream = FileOutputStream(dwldsPath, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
                Log.d("XLXSFIleFromB64Encode", "File.getXLXSFIleFromB64Encode () OK")
                return dwldsPath
            } catch (e: IOException) {
                Log.d("XLXSFIleFromB64Encode", "File.toByteArray() error")
                e.printStackTrace()
                return  null
            }
        }


    }
}