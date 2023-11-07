package com.aeromexico.aeropuertos.paperlessmobile.notoc.entities

import com.google.gson.annotations.SerializedName

/*
data class RequestNotoc(
    @SerializedName("FlightReferenceNumber")
    var flightReferenceNumber: Long,
    @SerializedName("InformacionMercancia")
    var informacionMercancia: MutableList<infoMercancia> = mutableListOf(),
    @SerializedName("OtraCarga")
    var otraCarga: MutableList<OtraCarga> = mutableListOf(),
    @SerializedName("Consentimiento")
    var consentimiento: Consentimiento= Consentimiento(),
    @SerializedName("EsEditable")
    var esEditable: Boolean=true,
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = ""
    )

data class Consentimiento(
    @SerializedName("Capitan")
    var capitan: Empleado= Empleado(),
    @SerializedName("Oficial")
    var oficial: Empleado= Empleado()
)

data class OtraCarga(
    @SerializedName("CargaEspecial")
    var cargaEspecial: CargaEspecial= CargaEspecial(),
    @SerializedName("Estibado")
    var estibado: Estibado= Estibado()
)
data class CargaEspecial(
    @SerializedName("GuiaAerea")
    var guiaAerea: Long=0,
    @SerializedName("Descripcion")
    var descripcion: String="",
    @SerializedName("Bultos")
    var bultos: MutableList<Bulto> = mutableListOf(),
    @SerializedName("InformacionComplementaria")
    var informacionComplementaria: String="",
    @SerializedName("Codigo")
    var codigo: String="",
)

data class Bulto(
    @SerializedName("NumeroDeBultos")
    var numeroBultos: Int =0,
    @SerializedName("Cantidad")
    var cantidad: Int=0
)

data class infoMercancia(
    @SerializedName("MercanciasPeligrosas")
    var mercanciasPeligrosas: MercanciasPeligrosas= MercanciasPeligrosas(),
    @SerializedName("Radiactivos")
    var radiactivos: Radiactivos= Radiactivos(),
    @SerializedName("Estibado")
    var estibado: Estibado= Estibado()
)

data class MercanciasPeligrosas(
    @SerializedName("NumeroGuiaAerea")
    var numeroGuiaAerea: Long=0,
    @SerializedName("NombreExpedicion")
    var nombreExpedicion: String="",
    @SerializedName("Clave")
    var clase: Int=0,
    @SerializedName("ONUID")
    var onuId: Int=0,
    @SerializedName("PeligroSecundario")
    var peligroSecundario: String="",
    @SerializedName("CantidadBultos")
    var cantidadBultos: Int=0,
    @SerializedName("GrupoEmbalaje")
    var grupoEmbalaje: Int=0,
    @SerializedName("Codigo")
    var codigo: String="",
    @SerializedName("CRE")
    var cre: Int=0
)

data class Radiactivos(
    @SerializedName("CantidadNeta")
    var cantidadNeta: Float=0f,
    @SerializedName("Categoria")
    var categoria: String=""
)
data class Estibado(
    @SerializedName("IDULD")
    var idUld: Int=0,
    @SerializedName("Posicion")
    var posicion: Int=0
)

data class Empleado(

    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)*/
data class RequestNotoc (

    @SerializedName("idNOTOC") var idNOTOC : Int = 0,
    @SerializedName("FlightReferenceNumber") var flightReferenceNumber : Long ,
    @SerializedName("InformacionMercancia") var informacionMercancia : MutableList<InformacionMercancia> = mutableListOf(),
    @SerializedName("OtraCarga") var otraCarga : MutableList<OtraCarga> = mutableListOf(),
    @SerializedName("Consentimiento") var consentimiento : Consentimiento = Consentimiento(),
    @SerializedName("CreadoPor") var creadoPor : String = "",
    @SerializedName("FechaCreacion") var fechaCreacion : String = "",
    @SerializedName("CatPosicion") val catPosicion : MutableList<CatPosicion> = mutableListOf(),
    @SerializedName("Editable") var editable : Boolean = false
)
data class CatPosicion (

    @SerializedName("idPosicionNOTOC") val idPosicionNOTOC : Int = 0,
    @SerializedName("DescPosicion") val descPosicion : String = ""
)
data class InformacionMercancia (
    @SerializedName("idMercanciaPeligrosa") val idMercanciaPeligrosa : Int = 0,
    @SerializedName("NumeroGuiaAerea") var numeroGuiaAerea : String = "",
    @SerializedName("NombreExpedicion") var nombreExpedicion : String = "",
    @SerializedName("Clave") var clave : String = "",
    @SerializedName("ONUID") var oNUID : String = "",
    @SerializedName("PeligroSecundario") var peligroSecundario : String ="",
    @SerializedName("CantidadBultos") var cantidadBultos : String = "",
    @SerializedName("GrupoEmbalaje") var grupoEmbalaje : String = "",
    @SerializedName("Codigo") var codigo : String = "",
    @SerializedName("CRE") var cRE : String = "",
    @SerializedName("CantidadNeta") var cantidadNeta : String = "",
    @SerializedName("Categoria") var categoria : String = "",
    @SerializedName("IDULD") var iDULD : String = "",
    @SerializedName("Posicion") var posicion : Int = 0
)
data class OtraCarga (

    @SerializedName("idCargaEspecial") var idCargaEspecial : Int = 0,
    @SerializedName("GuiaAerea") var guiaAerea : String = "",
    @SerializedName("Descripcion") var descripcion : String = "",
    @SerializedName("Bultos") var bultos : MutableList<Bultos> = mutableListOf(),
    @SerializedName("InformacionComplementaria") var informacionComplementaria : String = "",
    @SerializedName("Codigo") var codigo : String = "",
    @SerializedName("IDULD") var iDULD : String = "",
    @SerializedName("Posicion") var posicion : Int = 0
)
data class Consentimiento (

    @SerializedName("Capitan") var capitan : Capitan = Capitan(),
    @SerializedName("Oficial") var oficial : Oficial = Oficial()
)
data class Bultos (

    @SerializedName("IdCargaEspecial") var idCargaEspecial : Int = 0,
    @SerializedName("NumeroDeBultos") var numeroDeBultos : String = "",
    @SerializedName("Cantidad") var cantidad : String = ""
)
data class Capitan (

    @SerializedName("FirmaB64") var firmaB64 : String = "",
    @SerializedName("Nombre") var nombre : String = "",
    @SerializedName("NumEmpleado") var numEmpleado : String = ""
)
data class Oficial (

    @SerializedName("FirmaB64") var firmaB64 : String = "",
    @SerializedName("Nombre") var nombre : String = "",
    @SerializedName("NumEmpleado") var numEmpleado : String = ""
)
