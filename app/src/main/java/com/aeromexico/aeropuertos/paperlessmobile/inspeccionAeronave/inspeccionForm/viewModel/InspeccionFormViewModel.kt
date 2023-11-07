package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionForm.viewModel

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ResponsableEstibaEntiy

class InspeccionFormViewModel: ViewModel() {

    //Llegada o salida
    var esLLegada = MutableLiveData<Boolean>(false)
    fun getEsLlegada(): MutableLiveData<Boolean>{
        return esLLegada
    }

    fun setEsLlegada(esLLegada: Boolean){
        this.esLLegada.value = esLLegada
    }

    //Pernocta
    private val esPernocta = MutableLiveData<Boolean>(false)

    fun setEsPernocta(esPernocta: Boolean){
        this.esPernocta.value = esPernocta
    }
    fun getEsPernocta(): MutableLiveData<Boolean>{
         return esPernocta
     }


    //Radomo
    val radomo_tieneAveria = MutableLiveData<Boolean>(false)
    val radomo_descripcionAveria = MutableLiveData<String>("")
    val radomo_ImgB64 = MutableLiveData<String>("")
    val radomo_mandatorio = MutableLiveData<Boolean>(false)

    fun setRadomoTieneAveria(tieneAveria: Boolean){
        this.radomo_tieneAveria.value = tieneAveria
    }
    fun setRadomoDescripcionAveria(descripcionAveria: String){
        this.radomo_descripcionAveria.value = descripcionAveria
    }
    fun setRadomoImgB64(img: String){
        this.radomo_ImgB64.value = img
    }
    fun setRadomoMandatorio(mandatorio: Boolean){
        radomo_mandatorio.value = mandatorio
    }
    fun getRadomoMandatorio(): MutableLiveData<Boolean>{
        return radomo_mandatorio
    }
    fun getRadomoTieneAveria(): MutableLiveData<Boolean>{
        return this.radomo_tieneAveria
    }


    //CompCargaDelantero
    val compCargaDelantero_tieneAveria = MutableLiveData<Boolean>(false)
    val compCargaDelantero_descripcionAveria = MutableLiveData<String>("")
    val compCargaDelantero_ImgB64 = MutableLiveData<String>("")
    val compCargaDelantero_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompCargaDelanteroTieneAveria(tieneAveria: Boolean){
        this.compCargaDelantero_tieneAveria.value = tieneAveria
    }
    fun setCompCargaDelanteroDescripcionAveria(descripcionAveria: String){
        this.compCargaDelantero_descripcionAveria.value = descripcionAveria
    }
    fun setCompCargaDelanteroImgB64(img: String){
        this.compCargaDelantero_ImgB64.value = img
    }
    fun setCompCargaDelanteroMandatorio(mandatorio: Boolean){
        compCargaDelantero_mandatorio.value = mandatorio
    }
    fun getCompCargaDelanteroMandatorio(): MutableLiveData<Boolean>{
        return compCargaDelantero_mandatorio
    }
    fun getCompCargaDelanteroTieneAveria(): MutableLiveData<Boolean>{
        return this.compCargaDelantero_tieneAveria
    }



    //CompCargaTrasero
    val compCargaTrasero_tieneAveria = MutableLiveData<Boolean>(false)
    val compCargaTrasero_descripcionAveria = MutableLiveData<String>("")
    val compCargaTrasero_ImgB64 = MutableLiveData<String>("")
    val compCargaTrasero_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompCargaTraseroTieneAveria(tieneAveria: Boolean){
        this.compCargaTrasero_tieneAveria.value = tieneAveria
    }
    fun setCompCargaTraseroDescripcionAveria(descripcionAveria: String){
        this.compCargaTrasero_descripcionAveria.value = descripcionAveria
    }
    fun setCompCargaTraseroImgB64(img: String){
        this.compCargaTrasero_ImgB64.value = img
    }
    fun setCompCargaTraseroMandatorio(mandatorio: Boolean){
        compCargaTrasero_mandatorio.value = mandatorio
    }
    fun getCompCargaTraseroMandatorio(): MutableLiveData<Boolean>{
        return compCargaTrasero_mandatorio
    }
    fun getCompCargaTraseroTieneAveria(): MutableLiveData<Boolean>{
        return this.compCargaTrasero_tieneAveria
    }


    //CompCargaBulk
    val compCargaBulk_tieneAveria = MutableLiveData<Boolean>(false)
    val compCargaBulk_descripcionAveria = MutableLiveData<String>("")
    val compCargaBulk_ImgB64 = MutableLiveData<String>("")
    val compCargaBulk_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompCargaBulkTieneAveria(tieneAveria: Boolean){
        this.compCargaBulk_tieneAveria.value = tieneAveria
    }
    fun setCompCargaBulkDescripcionAveria(descripcionAveria: String){
        this.compCargaBulk_descripcionAveria.value = descripcionAveria
    }
    fun setCompCargaBulkImgB64(img: String){
        this.compCargaBulk_ImgB64.value = img
    }
    fun setCompCargaBulkMandatorio(mandatorio: Boolean){
        compCargaBulk_mandatorio.value = mandatorio
    }
    fun getCompCargaBulkMandatorio(): MutableLiveData<Boolean>{
        return compCargaBulk_mandatorio
    }
    fun getCompCargaBulkTieneAveria(): MutableLiveData<Boolean>{
        return this.compCargaBulk_tieneAveria
    }


    //CompIntDelantero
    val compIntDelantero_tieneAveria = MutableLiveData<Boolean>(false)
    val compIntDelantero_descripcionAveria = MutableLiveData<String>("")
    val compIntDelantero_ImgB64 = MutableLiveData<String>("")
    val compIntDelantero_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompIntDelanteroTieneAveria(tieneAveria: Boolean){
        this.compIntDelantero_tieneAveria.value = tieneAveria
    }
    fun setCompIntDelanteroDescripcionAveria(descripcionAveria: String){
        this.compIntDelantero_descripcionAveria.value = descripcionAveria
    }
    fun setCompIntDelanteroImgB64(img: String){
        this.compIntDelantero_ImgB64.value = img
    }
    fun setCompIntDelanteroMandatorio(mandatorio: Boolean){
        compIntDelantero_mandatorio.value = mandatorio
    }
    fun getCompIntDelanteroMandatorio(): MutableLiveData<Boolean>{
        return compIntDelantero_mandatorio
    }
    fun getCompIntDelanteroTieneAveria(): MutableLiveData<Boolean>{
        return this.compIntDelantero_tieneAveria
    }



    //CompIntTrasero
    val compIntTrasero_tieneAveria = MutableLiveData<Boolean>(false)
    val compIntTrasero_descripcionAveria = MutableLiveData<String>("")
    val compIntTrasero_ImgB64 = MutableLiveData<String>("")
    val compIntTrasero_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompIntTraseroTieneAveria(tieneAveria: Boolean){
        this.compIntTrasero_tieneAveria.value = tieneAveria
    }
    fun setCompIntTraseroDescripcionAveria(descripcionAveria: String){
        this.compIntTrasero_descripcionAveria.value = descripcionAveria
    }
    fun setCompIntTraseroImgB64(img: String){
        this.compIntTrasero_ImgB64.value = img
    }
    fun setCompIntTraseroMandatorio(mandatorio: Boolean){
        compIntTrasero_mandatorio.value = mandatorio
    }
    fun getCompIntTraseroMandatorio(): MutableLiveData<Boolean>{
        return compIntTrasero_mandatorio
    }
    fun getCompIntTraseroTieneAveria(): MutableLiveData<Boolean>{
        return this.compIntTrasero_tieneAveria
    }



    //CompIntBulk
    val compIntBulk_tieneAveria = MutableLiveData<Boolean>(false)
    val compIntBulk_descripcionAveria = MutableLiveData<String>("")
    val compIntBulk_ImgB64 = MutableLiveData<String>("")
    val compIntBulk_mandatorio = MutableLiveData<Boolean>(false)

    fun setCompIntBulkTieneAveria(tieneAveria: Boolean){
        this.compIntBulk_tieneAveria.value = tieneAveria
    }
    fun setCompIntBulkDescripcionAveria(descripcionAveria: String){
        this.compIntBulk_descripcionAveria.value = descripcionAveria
    }
    fun setCompIntBulkImgB64(img: String){
        this.compIntBulk_ImgB64.value = img
    }
    fun setCompIntBulkMandatorio(mandatorio: Boolean){
        compIntBulk_mandatorio.value = mandatorio
    }
    fun getCompIntBulkMandatorio(): MutableLiveData<Boolean>{
        return compIntBulk_mandatorio
    }
    fun getCompIntBulkTieneAveria(): MutableLiveData<Boolean>{
        return this.compIntBulk_tieneAveria
    }



    //SemialaIzq
    val semialaIzq_tieneAveria = MutableLiveData<Boolean>(false)
    val semialaIzq_descripcionAveria = MutableLiveData<String>("")
    val semialaIzq_ImgB64 = MutableLiveData<String>("")
    val semialaIzq_mandatorio = MutableLiveData<Boolean>(false)

    fun setSemialaIzqTieneAveria(tieneAveria: Boolean){
        this.semialaIzq_tieneAveria.value = tieneAveria
    }
    fun setSemialaIzqDescripcionAveria(descripcionAveria: String){
        this.semialaIzq_descripcionAveria.value = descripcionAveria
    }
    fun setSemialaIzqImgB64(img: String){
        this.semialaIzq_ImgB64.value = img
    }
    fun setSemialaIzqMandatorio(mandatorio: Boolean){
        semialaIzq_mandatorio.value = mandatorio
    }
    fun getSemialaIzqMandatorio(): MutableLiveData<Boolean>{
        return semialaIzq_mandatorio
    }
    fun getSemialaIzqTieneAveria(): MutableLiveData<Boolean>{
        return this.semialaIzq_tieneAveria
    }



    //SemialaDer
    val semialaDer_tieneAveria = MutableLiveData<Boolean>(false)
    val semialaDer_descripcionAveria = MutableLiveData<String>("")
    val semialaDer_ImgB64 = MutableLiveData<String>("")
    val semialaDer_mandatorio = MutableLiveData<Boolean>(false)

    fun setSemialaDerTieneAveria(tieneAveria: Boolean){
        this.semialaDer_tieneAveria.value = tieneAveria
    }
    fun setSemialaDerDescripcionAveria(descripcionAveria: String){
        this.semialaDer_descripcionAveria.value = descripcionAveria
    }
    fun setSemialaDerImgB64(img: String){
        this.semialaDer_ImgB64.value = img
    }
    fun setSemialaDerMandatorio(mandatorio: Boolean){
        semialaDer_mandatorio.value = mandatorio
    }
    fun getSemialaDerMandatorio(): MutableLiveData<Boolean>{
        return semialaDer_mandatorio
    }
    fun getSemialaDerTieneAveria(): MutableLiveData<Boolean>{
        return this.semialaDer_tieneAveria
    }



    //AguaPotable
    val aguaPotable_tieneAveria = MutableLiveData<Boolean>(false)
    val aguaPotable_descripcionAveria = MutableLiveData<String>("")
    val aguaPotable_ImgB64 = MutableLiveData<String>("")
    val aguaPotable_mandatorio = MutableLiveData<Boolean>(false)

    fun setAguaPotableTieneAveria(tieneAveria: Boolean){
        this.aguaPotable_tieneAveria.value = tieneAveria
    }
    fun setAguaPotableDescripcionAveria(descripcionAveria: String){
        this.aguaPotable_descripcionAveria.value = descripcionAveria
    }
    fun setAguaPotableImgB64(img: String){
        this.aguaPotable_ImgB64.value = img
    }
    fun setAguaPotableMandatorio(mandatorio: Boolean){
        aguaPotable_mandatorio.value = mandatorio
    }
    fun getAguaPotableMandatorio(): MutableLiveData<Boolean>{
        return aguaPotable_mandatorio
    }
    fun getAguaPotableTieneAveria(): MutableLiveData<Boolean>{
        return this.aguaPotable_tieneAveria
    }



    //AguaNegra
    val aguaNegra_tieneAveria = MutableLiveData<Boolean>(false)
    val aguaNegra_descripcionAveria = MutableLiveData<String>("")
    val aguaNegra_ImgB64 = MutableLiveData<String>("")
    val aguaNegra_mandatorio = MutableLiveData<Boolean>(false)

    fun setAguaNegraTieneAveria(tieneAveria: Boolean){
        this.aguaNegra_tieneAveria.value = tieneAveria
    }
    fun setAguaNegraDescripcionAveria(descripcionAveria: String){
        this.aguaNegra_descripcionAveria.value = descripcionAveria
    }
    fun setAguaNegraImgB64(img: String){
        this.aguaNegra_ImgB64.value = img
    }
    fun setAguaNegraMandatorio(mandatorio: Boolean){
        aguaNegra_mandatorio.value = mandatorio
    }
    fun getAguaNegraMandatorio(): MutableLiveData<Boolean>{
        return aguaNegra_mandatorio
    }
    fun getAguaNegraTieneAveria(): MutableLiveData<Boolean>{
        return this.aguaNegra_tieneAveria
    }


    //PuertaPrincipal
    val puertaPrincipal_tieneAveria = MutableLiveData<Boolean>(false)
    val puertaPrincipal_descripcionAveria = MutableLiveData<String>("")
    val puertaPrincipal_ImgB64 = MutableLiveData<String>("")
    val puertaPrincipal_mandatorio = MutableLiveData<Boolean>(false)

    fun setPuertaPrincipalTieneAveria(tieneAveria: Boolean){
        this.puertaPrincipal_tieneAveria.value = tieneAveria
    }
    fun setPuertaPrincipalDescripcionAveria(descripcionAveria: String){
        this.puertaPrincipal_descripcionAveria.value = descripcionAveria
    }
    fun setPuertaPrincipalImgB64(img: String){
        this.puertaPrincipal_ImgB64.value = img
    }
    fun setPuertaPrincipalMandatorio(mandatorio: Boolean){
        puertaPrincipal_mandatorio.value = mandatorio
    }
    fun getPuertaPrincipalMandatorio(): MutableLiveData<Boolean>{
        return puertaPrincipal_mandatorio
    }
    fun getPuertaPrincipalTieneAveria(): MutableLiveData<Boolean>{
        return this.puertaPrincipal_tieneAveria
    }



    //ServicioTrasera
    val servicioTrasera_tieneAveria = MutableLiveData<Boolean>(false)
    val servicioTrasera_descripcionAveria = MutableLiveData<String>("")
    val servicioTrasera_ImgB64 = MutableLiveData<String>("")
    val servicioTrasera_mandatorio = MutableLiveData<Boolean>(false)

    fun setServicioTraseraTieneAveria(tieneAveria: Boolean){
        this.servicioTrasera_tieneAveria.value = tieneAveria
    }
    fun setServicioTraseraDescripcionAveria(descripcionAveria: String){
        this.servicioTrasera_descripcionAveria.value = descripcionAveria
    }
    fun setServicioTraseraImgB64(img: String){
        this.servicioTrasera_ImgB64.value = img
    }
    fun setServicioTraseraMandatorio(mandatorio: Boolean){
        servicioTrasera_mandatorio.value = mandatorio
    }
    fun getServicioTraseraMandatorio(): MutableLiveData<Boolean>{
        return servicioTrasera_mandatorio
    }
    fun getServicioTraseraTieneAveria(): MutableLiveData<Boolean>{
        return this.servicioTrasera_tieneAveria
    }



    //SepDosIn
    val sepDosIn_tieneAveria = MutableLiveData<Boolean>(false)
    val sepDosIn_descripcionAveria = MutableLiveData<String>("")
    val sepDosIn_ImgB64 = MutableLiveData<String>("")
    val sepDosIn_mandatorio = MutableLiveData<Boolean>(false)

    fun setSepDosInTieneAveria(tieneAveria: Boolean){
        this.sepDosIn_tieneAveria.value = tieneAveria
    }
    fun setSepDosInDescripcionAveria(descripcionAveria: String){
        this.sepDosIn_descripcionAveria.value = descripcionAveria
    }
    fun setSepDosInImgB64(img: String){
        this.sepDosIn_ImgB64.value = img
    }
    fun setSepDosInMandatorio(mandatorio: Boolean){
        sepDosIn_mandatorio.value = mandatorio
    }
    fun getSepDosInMandatorio(): MutableLiveData<Boolean>{
        return sepDosIn_mandatorio
    }
    fun getSepDosInTieneAveria(): MutableLiveData<Boolean>{
        return this.sepDosIn_tieneAveria
    }


    //ColocacionRedes
    val colocacionRedes_tieneAveria = MutableLiveData<Boolean>(false)
    val colocacionRedes_descripcionAveria = MutableLiveData<String>("")
    val colocacionRedes_ImgB64 = MutableLiveData<String>("")
    val colocacionRedes_mandatorio = MutableLiveData<Boolean>(false)

    fun setColocacionRedesTieneAveria(tieneAveria: Boolean){
        this.colocacionRedes_tieneAveria.value = tieneAveria
    }
    fun setColocacionRedesDescripcionAveria(descripcionAveria: String){
        this.colocacionRedes_descripcionAveria.value = descripcionAveria
    }
    fun getColocacionRedesTieneAveria(): MutableLiveData<Boolean>{
        return this.colocacionRedes_tieneAveria
    }
    fun setColocacionRedesMandatorio(mandatorio: Boolean){
        colocacionRedes_mandatorio.value = mandatorio
    }
    fun getColocacionRedesMandatorio(): MutableLiveData<Boolean>{
        return colocacionRedes_mandatorio
    }
    fun setColocacionRedesImgB64(img: String){
        this.colocacionRedes_ImgB64.value = img
    }


    //Otros
    val otros_tieneAveria = MutableLiveData<Boolean>(false)
    val otros_nombre = MutableLiveData<String>("")
    val otros_descripcionAveria = MutableLiveData<String>("")
    var otros_imagenes = MutableLiveData<MutableList<String>>();
    val otros_mandatorio = MutableLiveData<Boolean>(false)

    fun setOtrosTieneAveria(tieneAveria: Boolean){
        this.otros_tieneAveria.value = tieneAveria
    }
    fun setOtrosDescripcionAveria(descripcionAveria: String){
        this.otros_descripcionAveria.value = descripcionAveria
    }
    fun setOtrosNombre(nombre: String){
        this.otros_nombre.value = nombre
    }
    fun setOtrosMandatorio(mandatorio: Boolean){
        otros_mandatorio.value = mandatorio
    }
    fun getOtrosMandatorio(): MutableLiveData<Boolean>{
        return otros_mandatorio
    }
    fun getOtrosTieneAveria(): MutableLiveData<Boolean>{
        return this.otros_tieneAveria
    }
    fun addOtrosImagen(img: String){
        var lista = otros_imagenes.value;
        lista?.add(img);
        otros_imagenes.value = lista!!;
    }
    fun deleteOtrosImagen(img: String){
        val index = otros_imagenes.value?.indexOf(img);
        if(index != -1){
            var lista = otros_imagenes.value;
            lista?.removeAt(index!!);
            otros_imagenes.value = lista!!;
        }
    }
    fun getOtrosImagenes() : LiveData<MutableList<String>> {
        return otros_imagenes
    }



    //Validar si existe alguna avería
    fun existenAverias(): Boolean{
        return (
                    radomo_tieneAveria.value!! ||
                    compCargaDelantero_tieneAveria.value!! ||
                    compCargaTrasero_tieneAveria.value!! ||
                    compCargaBulk_tieneAveria.value!! ||
                    compIntDelantero_tieneAveria.value!! ||
                    compIntTrasero_tieneAveria.value!! ||
                    compIntBulk_tieneAveria.value!! ||
                    semialaIzq_tieneAveria.value!! ||
                    semialaDer_tieneAveria.value!! ||
                    aguaPotable_tieneAveria.value!! ||
                    aguaNegra_tieneAveria.value!! ||
                    puertaPrincipal_tieneAveria.value!! ||
                    servicioTrasera_tieneAveria.value!! ||
                  //  sepDosIn_tieneAveria.value!! ||    ESTE ELEMENTO NO ENTRA EN LA VALIDACIÓN
                    colocacionRedes_tieneAveria.value!! ||
                    otros_tieneAveria.value!!
                )
    }

    //Reiniciar VM
    fun reiniciarVM(){
        esPernocta.value = false
        esLLegada.value = false

        radomo_tieneAveria.value = false
        radomo_descripcionAveria.value = ""
        radomo_ImgB64.value = ""

        compCargaDelantero_tieneAveria.value =false
        compCargaDelantero_descripcionAveria.value = ""
        compCargaDelantero_ImgB64.value = ""

        compCargaTrasero_tieneAveria.value =false
        compCargaTrasero_descripcionAveria.value = ""
        compCargaTrasero_ImgB64.value = ""

        compCargaBulk_tieneAveria.value =false
        compCargaBulk_descripcionAveria.value = ""
        compCargaBulk_ImgB64.value = ""

        compIntDelantero_tieneAveria.value =false
        compIntDelantero_descripcionAveria.value = ""
        compIntDelantero_ImgB64.value = ""

        compIntTrasero_tieneAveria.value =false
        compIntTrasero_descripcionAveria.value = ""
        compIntTrasero_ImgB64.value = ""

        compIntBulk_tieneAveria.value =false
        compIntBulk_descripcionAveria.value = ""
        compIntBulk_ImgB64.value = ""

        semialaIzq_tieneAveria.value =false
        semialaIzq_descripcionAveria.value = ""
        semialaIzq_ImgB64.value = ""

        semialaDer_tieneAveria.value =false
        semialaDer_descripcionAveria.value = ""
        semialaDer_ImgB64.value = ""

        aguaPotable_tieneAveria.value =false
        aguaPotable_descripcionAveria.value = ""
        aguaPotable_ImgB64.value = ""

        aguaNegra_tieneAveria.value =false
        aguaNegra_descripcionAveria.value = ""
        aguaNegra_ImgB64.value = ""

        puertaPrincipal_tieneAveria.value =false
        puertaPrincipal_descripcionAveria.value = ""
        puertaPrincipal_ImgB64.value = ""

        servicioTrasera_tieneAveria.value =false
        servicioTrasera_descripcionAveria.value = ""
        servicioTrasera_ImgB64.value = ""

        sepDosIn_tieneAveria.value =false
        sepDosIn_descripcionAveria.value = ""
        sepDosIn_ImgB64.value = ""

        colocacionRedes_tieneAveria.value =false
        colocacionRedes_descripcionAveria.value = ""
        colocacionRedes_ImgB64.value = ""

        otros_tieneAveria.value =false
        otros_descripcionAveria.value = ""
        otros_imagenes.value = mutableListOf()


    }

}