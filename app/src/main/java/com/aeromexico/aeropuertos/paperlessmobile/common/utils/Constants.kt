package com.aeromexico.aeropuertos.paperlessmobile.common.utils

object Constants {
    enum class Modulos{
        Mensajes_Operacionales, Inspeccion_Aviones, Inspeccion_primer_vuelo , Deshielo, Orden_carga_combustible, OPT_BDM, Search_list,ControlAbordaje,NOTOC, ManifiestoCarga,Metar,NewInspeccion
    }
    //Controladores
    const val VUELOS = "Flights/"
    const val MENSAJES_OPERACIONALES = "MsjsOperacionales/"
    const val CORE = "Core/"
    const val  CHECKLIST = "CheckList/"
    const val INSPECCION_AERONAVE = "Inspeccion/"
    const val OPT_EDM = "OPT_EDM/"
    const val NOTOC = "Notoc/"
    const val MANIFIESTOCARGA = "ManifiestoCarga/"

    //OPT_EDM
    const val GETINFOOPT = "GetInfoOPTEDM/"
    const val FIRMAROPT = "FirmarOPT_EDM/"

    //check list
    const val GETEQUIPO = "GetEquipoTerrestre"

    //Vuelos
    const val BUSCAR_VUELOS = "Vuelos"
    const val VUELO_MANUAL = "VueloManual"

    //Mensajes operacionales
    const val GET_MENSAJES_OPERACIONALES = "GetMensajesOps"
    const val SET_DETALLE_MSJ_OPS = "SetDetalleMsjOps"

    //Orden de carga de combustible
    const val END_POINT_ORDEN_SEND_FORM = "Orden/NuevaOrdenCarga"
    const val END_POINT_ORDEN_CHECK_FORM = "Orden/GetInfo"

    //Primer Vuelo dia
    const val END_POINT_GET_IATAS = "Catalogos/GetCarriesIATAs"
    const val END_POINT_SEND_FORM = "PrimerVuelo/Preguntas"
    const val PRIMERVUELO_GET_INFO = "PrimerVuelo/GetInfoPrimerVuelo"

    //SearchList
    const val END_POINT_SEARCH_LIST = "SearchList/NuevoFormato"
    const val END_POINT_GET_INFO = "SearchList/CheckInfo"

    //Inspección de aeronave
    const val CREATE_INSPECCION_AERONAVE = "NuevaInspeccion"
    const val GET_TIPOS_AVERIA_INSPECCION_AERONAVE = "GetTiposAveria"


    //Core
    const val GET_EMPLEADO = "GetEmpleado"

    //NOTOC
    const val GET_NOTOC = "GetInfo"
    const val SET_DETALLE_NOTOC = "InsertInfo"

    //Manifiesto de carga
    const val GET_MANIFIESTO = "GetPDFManifiestoCarga"

    //AppInfo
    const val APP_VERSION = "1.0.0"
    const val APP_COMPILE_INFO = "Información de compilación"
    const val APP_SUPPORT_INFO = "Información de soporte"
    const val MESSAGE_4488 = "Ext. 4488"
    const val TEXT_TICKET = "Grupo para levantar ticket"
    const val GROUP_RELEASE_TICKET = "AMX-APPSUPP-PAPERLESSATOS"

//    Permisos
    const val REQUEST_TAKE_PICTURE = 1
    const val REQUEST_CHOOSE_PICTURE = 2
    const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
    const val PERMISSION_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
    const val PERMISSION_READ_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
    const val PERMISSION_WRITE_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    //Códigos inspección aeronave
    enum class TiposAveria{
        radomo,
        comp_carga_delantero,
        comp_carga_trasero,
        comp_carga_bulk,
        comp_int_delantero,
        comp_int_trasero,
        comp_int_bulk,
        semiala_izq,
        semiala_der,
        agua_potable,
        agua_negra,
        puerta_principal,
        puerta_trasera,
        sep_dos_pulg,
        colocacion_redes,
        otro
    }
}