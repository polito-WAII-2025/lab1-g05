import java.io.File


object WaypointReader {
    fun readWaypoints(filePath: String): List<Waypoint> {
        val file = File(filePath)
        if (!file.exists()) {
            println("ERRORE: Il file $filePath non esiste! Assicurati di averlo copiato nella cartella corretta.")
            return emptyList()
        }

        return file.readLines()
            .drop(1) // Salta l'intestazione se presente
            .mapNotNull { line ->
                val parts = line.split(";", ",") // Supporta sia ; che , come separatori
                if (parts.size < 3) {
                    println("Riga ignorata: $line")
                    return@mapNotNull null
                }

                try {
                    val timestamp = parts[0].toDouble().toLong() // Converte numeri decimali in Long
                    val latitude = parts[1].toDouble()
                    val longitude = parts[2].toDouble()
                    Waypoint(timestamp, latitude, longitude)
                } catch (e: NumberFormatException) {
                    println("ERRORE: Riga non valida: $line")
                    null
                }
            }
    }
}
