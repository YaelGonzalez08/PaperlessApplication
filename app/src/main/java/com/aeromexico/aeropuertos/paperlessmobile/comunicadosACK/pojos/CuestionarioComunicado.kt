import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.Respuestas
import com.google.gson.annotations.SerializedName


data class CuestionarioComunicado (

	@SerializedName("idPregunta") val idPregunta : Int,
	@SerializedName("pregunta") val pregunta : String,
	@SerializedName("esPreguntaAbierta") val esPreguntaAbierta : Boolean,
	@SerializedName("justificacion") val justificacion : String,
	@SerializedName("respuestas") val respuestas : List<Respuestas>,
	@SerializedName("respuestaManual") var respuestaManual: String? = ""
){
	var showError : Boolean = false
}