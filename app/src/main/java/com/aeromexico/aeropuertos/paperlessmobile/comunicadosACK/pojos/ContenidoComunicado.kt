import com.google.gson.annotations.SerializedName



data class ContenidoComunicado (

	@SerializedName("numero") val numero : Int,
	@SerializedName("titulo") val titulo : String,
	@SerializedName("vigencia") val vigencia : String,
	@SerializedName("version") val version : Int,
	@SerializedName("cuerpoMensaje") val cuerpoMensaje : String,
	@SerializedName("link") val link : String,
	@SerializedName("idUnidadNegocio") val idUnidadNegocio : Int
)