package com.aeromexico.aeropuertos.paperlessmobile.SilkySignature

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.aeromexico.aeropuertos.paperlessmobile.SilkySignature.viewModel.SignatureViewModel
import com.aeromexico.aeropuertos.paperlessmobile.databinding.SignatureFragmentBinding
import com.aeromexico.aeropuertos.paperlessmobile.home.MainActivity
import com.williamww.silkysignature.views.SignaturePad
import com.williamww.silkysignature.views.SignaturePad.OnSignedListener
import java.io.*

class SignatureFragment : Fragment() {

    private  val viewModel: SignatureViewModel  by activityViewModels();
    private lateinit var mBinding: SignatureFragmentBinding
    private var mSilkySignaturePad: SignaturePad? = null
    private var mClearButton: Button? = null
    private var mSaveButton: Button? = null
    private var mCompressButton: Button? = null
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = SignatureFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(ContextCompat.checkSelfPermission
                (requireActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
        setupPad()
    }

    private fun setupPad(){
        mSilkySignaturePad =  mBinding.signaturePad
        mSaveButton = mBinding.saveButton
        mClearButton = mBinding.clearButton
        mSaveButton?.setOnClickListener {
            var signatureBitmap = mSilkySignaturePad?.signatureBitmap
            viewModel.setBase64String(getBase64(signatureBitmap!!))
            findNavController().popBackStack()
            /*if (addJpgSignatureToGallery(signatureBitmap!!)) {
                Toast.makeText(
                    context,
                    "Signature saved into the Gallery",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Unable to store the signature",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (addSvgSignatureToGallery(mSilkySignaturePad!!.signatureSvg)) {
                Toast.makeText(
                    context,
                    "SVG Signature saved into the Gallery",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Unable to store the SVG signature",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
        }
        mClearButton?.setOnClickListener {
            mSilkySignaturePad?.clear()
        }
        mSilkySignaturePad?.setOnSignedListener(object : OnSignedListener {
            override fun onStartSigning() {
//                Toast.makeText(context, "OnStartSigning", Toast.LENGTH_SHORT).show()
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
                mSaveButton!!.isEnabled = true
                mClearButton!!.isEnabled = true
                //mCompressButton!!.isEnabled = true
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
                mSaveButton!!.isEnabled = false
                mClearButton!!.isEnabled = false
                //mCompressButton!!.isEnabled = false
            }
        })
    }

    private fun getBase64(signature: Bitmap): String {
        val baos = ByteArrayOutputStream()
        signature.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            val photo: File = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )

            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }
    @Suppress("DEPRECATION")
    private fun getAlbumStorageDir(albumName: String?): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            file.mkdirs()
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    @Throws(IOException::class)
    private fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(mediaScanIntent)
    }

    private fun addSvgSignatureToGallery(signatureSvg: String?): Boolean {
        var result = false
        try {
            val svgFile = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.svg", System.currentTimeMillis())
            )
            val stream: OutputStream = FileOutputStream(svgFile)
            val writer = OutputStreamWriter(stream)
            writer.write(signatureSvg)
            writer.close()
            stream.flush()
            stream.close()
            scanMediaFile(svgFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }



}