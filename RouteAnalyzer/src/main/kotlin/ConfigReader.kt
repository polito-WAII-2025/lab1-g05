import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Config(
    val earthRadiusKm: Double,
    val geofenceCenterLatitude: Double,
    val geofenceCenterLongitude: Double,
    val geofenceRadiusKm: Double,
    val mostFrequentedAreaRadiusKm: Double? = null
)

object ConfigReader {
    fun loadConfig(filePath: String): Config {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("❌ ERRORE: Il file di configurazione $filePath non esiste!")
        }
        return Yaml.default.decodeFromString(Config.serializer(), file.readText())
    }
}
