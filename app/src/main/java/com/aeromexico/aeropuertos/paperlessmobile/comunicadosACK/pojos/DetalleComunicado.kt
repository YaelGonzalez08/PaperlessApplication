import com.google.gson.annotations.SerializedName

data class DetalleComunicado(

    @SerializedName("ContenidoComunicado") val contenidoComunicado : ContenidoComunicado,
    @SerializedName("CuestionarioComunicado") val cuestionarioComunicado : ArrayList<CuestionarioComunicado>,
    @SerializedName("solicitarCorreo") val solicitarCorreo : Boolean,
    @SerializedName("solicitarPuesto") val solicitarPuesto : Boolean,
    @SerializedName("solicitarNombre") val solicitarNombre : Boolean,
    @SerializedName("solicitarNumEmpleado") val solicitarNumEmpleado : Boolean,
    @SerializedName("documentos") val documentos : ArrayList<Documentos>? = arrayListOf()
)
