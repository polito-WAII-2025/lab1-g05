import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class OutputData(
    val maxDistanceFromStart: MaxDistanceFromStart,
    val mostFrequentedArea: MostFrequentedArea,
    val waypointsOutsideGeofence: WaypointsOutsideGeofence
)

@Serializable
data class Waypoint(val timestamp: Long, val latitude: Double, val longitude: Double)

@Serializable
data class MaxDistanceFromStart(
    @Contextual val waypoint: Waypoint,
    val distanceKm: Double
)

@Serializable
data class MostFrequentedArea(
    @Contextual val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int
)

@Serializable
data class WaypointsOutsideGeofence(
    val count: Int,
    @Contextual val waypoints: List<Waypoint>
)

// Calcola la distanza massima dal punto di partenza
fun maxDistanceFromStart(waypoints: List<Waypoint>): Pair<Waypoint, Double> {
    val start = waypoints.first()
    return waypoints.map { wp ->
        val distance = GeoUtils.haversine(start.latitude, start.longitude, wp.latitude, wp.longitude)
        wp to distance
    }.maxByOrNull { it.second } ?: (start to 0.0)
}

// Trova l'area più frequentata
fun mostFrequentedArea(waypoints: List<Waypoint>, radiusKm: Double): Pair<Waypoint, Int> {
    var bestCenter = waypoints.first()
    var maxCount = 0

    waypoints.forEach { center ->
        val count = waypoints.count { wp ->
            GeoUtils.haversine(center.latitude, center.longitude, wp.latitude, wp.longitude) <= radiusKm
        }
        if (count > maxCount) {
            maxCount = count
            bestCenter = center
        }
    }
    return bestCenter to maxCount
}

// Trova i waypoint fuori dal geo-fence
fun waypointsOutsideGeofence(waypoints: List<Waypoint>, centerLat: Double, centerLon: Double, radiusKm: Double): List<Waypoint> {
    return waypoints.filter { wp ->
        GeoUtils.haversine(centerLat, centerLon, wp.latitude, wp.longitude) > radiusKm
    }.distinctBy { it.latitude to it.longitude }
}

// Salva i risultati in output.json (con formattazione leggibile)
fun saveResultsToJson(outputPath: String, maxDist: Pair<Waypoint, Double>, freqArea: Pair<Waypoint, Int>, outWaypoints: List<Waypoint>) {
    val outputData = OutputData(
        maxDistanceFromStart = MaxDistanceFromStart(maxDist.first, maxDist.second),
        mostFrequentedArea = MostFrequentedArea(freqArea.first, 0.5, freqArea.second),
        waypointsOutsideGeofence = WaypointsOutsideGeofence(outWaypoints.size, outWaypoints)
    )

    val json = Json { prettyPrint = true }  // 🔹 Aggiunto per formattare il JSON
    File(outputPath).writeText(json.encodeToString(outputData))
}

fun main() {
    val configPath = "custom_parameters.yml"  // Percorso del file YAML
    val config = ConfigReader.loadConfig(configPath)  // Legge i parametri YAML

    val filePath = "waypoints.csv"
    val waypoints = WaypointReader.readWaypoints(filePath)

    if (waypoints.isEmpty()) {
        println("❌ ERRORE: Il file $filePath è vuoto o non esiste!")
        return
    }

    // Determina il raggio dell'area più frequentata se non specificato nel YAML
    val areaRadius = config.mostFrequentedAreaRadiusKm ?: (maxDistanceFromStart(waypoints).second * 0.1)

    val maxDist = maxDistanceFromStart(waypoints)
    val freqArea = mostFrequentedArea(waypoints, areaRadius)
    val outWaypoints = waypointsOutsideGeofence(
        waypoints,
        config.geofenceCenterLatitude,
        config.geofenceCenterLongitude,
        config.geofenceRadiusKm
    )

    saveResultsToJson("output.json", maxDist, freqArea, outWaypoints)

    println("✅ Analisi completata! Risultati salvati in output.json")
}
