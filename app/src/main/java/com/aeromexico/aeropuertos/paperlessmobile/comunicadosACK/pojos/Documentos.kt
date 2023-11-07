import com.google.gson.annotations.SerializedName

data class Documentos(

    @SerializedName("idArchivo") val idArchivo: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("ruta") val ruta: String,
    @SerializedName("extension") val extension: String,
    @SerializedName("activo") val activo: Boolean,
    @SerializedName("archivo") val archivo: String? = ""
)
