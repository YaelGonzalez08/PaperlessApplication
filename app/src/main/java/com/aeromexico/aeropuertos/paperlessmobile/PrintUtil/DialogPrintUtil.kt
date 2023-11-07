package com.aeromexico.aeropuertos.paperlessmobile.PrintUtil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aeromexico.aeropuertos.paperlessmobile.PrintUtil.utilities.Printooth
import com.aeromexico.aeropuertos.paperlessmobile.R
import com.aeromexico.aeropuertos.paperlessmobile.databinding.DialogPrintUtilBinding
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class DialogPrintUtil(val bitmap: Bitmap) : DialogFragment(),
    PrintingCallback {
    lateinit var binding: DialogPrintUtilBinding
    var msg = MutableLiveData<String>()
    private var printing: Printing? = null
    private var numberCopies = 2

    var mutex = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
        configListerPRint()
    }

    private fun configListerPRint() {
        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()

        printing?.printingCallback = this@DialogPrintUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPrintUtilBinding.inflate(inflater, container, false)

        binding.apply {
            numberOfCopies.text = numberCopies.toString()

            closeDialog.setOnClickListener {
                this@DialogPrintUtil.dismiss()
                // unPAirPRint()
            }
            btnCerrar.setOnClickListener {
                this@DialogPrintUtil.dismiss()
                // unPAirPRint()
            }
            img.apply {
                setImageBitmap(bitmap)
            }
            btnPrint.setOnClickListener {
                if (!Printooth.hasPairedPrinter()) {
                    openScannActivity()
                } else printSomeImages()
            }
            addBtn.setOnClickListener {
                if (numberCopies in 0..1) {
                    numberCopies++
                    numberOfCopies.text = numberCopies.toString()
                }

            }
            removeBtn.setOnClickListener {
                if (numberCopies > 1) {
                    numberCopies--
                    numberOfCopies.text = numberCopies.toString()
                }
            }
        }
        msg.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.progress.visibility = View.VISIBLE
                binding.msjInfoPrint.text = it

            } else {
                binding.progress.visibility = View.GONE
            }
            // poner aqui una espera de 10s para que se oculte

        })
        return binding.root
    }

    private fun openScannActivity() {
        Log.w("Intent: ", "startActivityForResult")
        startActivityForResult(
            Intent(
                requireContext(),
                ScanningActivity::class.java
            ),
            ScanningActivity.SCANNING_FOR_PRINTER
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK && !mutex) {
            configListerPRint()
            printSomeImages()
            mutex = true
        }

    }

    private fun unPairPrinter() {
        if (Printooth.hasPairedPrinter()) Printooth.removeCurrentPrinter()
    }

    private fun printSomeImages() {
        msg.value = "Imprimiendo . . ."
        val printables = ArrayList<Printable>()
        for (i in numberCopies downTo 1 step 1) {
            printables.apply {
                add(ImagePrintable.Builder(bitmap).build())
                add(ImagePrintable.Builder(R.drawable.spacetk, resources).build())
            }
        }
        printing?.print(printables)
    }

    override fun connectingWithPrinter() {
        msg.value = "Connecting with printer"
    }

    override fun printingOrderSentSuccessfully() {
        msg.value = "Order sent to printer"
    }

    override fun connectionFailed(error: String) {
        msg.value = "Failed to connect printer cause:$error"
    }

    override fun onError(error: String) {
        msg.value = "Error: $error"
    }

    override fun onMessage(message: String) {
        msg.value = "Message: $message"
    }

    override fun disconnected() {
        msg.value = "Imprimiendo . . ."
        mutex = false
        GlobalScope.launch(context = Dispatchers.IO) {
            delay(10000)
            msg.postValue("Impresión Correcta")
            delay(5000)
            msg.postValue("")
            unPairPrinter()
            this@DialogPrintUtil.dismiss()
        }

    }

    override fun onStop() {
        super.onStop()
        unPairPrinter()
    }
}