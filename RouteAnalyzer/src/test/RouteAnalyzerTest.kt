import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RouteAnalyzerTest {

    @Test
    fun `test maxDistanceFromStart`() {
        val waypoints = listOf(
            Waypoint(1L, 45.0, 7.0), // Punto di partenza
            Waypoint(2L, 45.1, 7.1),
            Waypoint(3L, 45.2, 7.2)  // Dovrebbe essere il più lontano
        )

        val result = maxDistanceFromStart(waypoints)

        assertEquals(45.2, result.first.latitude, 0.0001)
        assertEquals(7.2, result.first.longitude, 0.0001)
        assert(result.second > 0) { "La distanza dovrebbe essere positiva" }
    }
}

@Test
fun `test mostFrequentedArea`() {
    val waypoints = listOf(
        Waypoint(1L, 45.0, 7.0),
        Waypoint(2L, 45.05, 7.05),
        Waypoint(3L, 45.06, 7.06),
        Waypoint(4L, 45.0, 7.0) // Questo si ripete → dovrebbe essere il centro più frequentato
    )

    val result = mostFrequentedArea(waypoints, 0.1)

    assertEquals(45.0, result.first.latitude, 0.0001)
    assertEquals(7.0, result.first.longitude, 0.0001)
    assertEquals(2, result.second) // 2 waypoint nel raggio di 0.1 km
}

@Test
fun `test waypointsOutsideGeofence`() {
    val waypoints = listOf(
        Waypoint(1L, 45.0, 7.0), // Dentro la geofence
        Waypoint(2L, 45.5, 7.5), // Fuori
        Waypoint(3L, 46.0, 8.0)  // Fuori
    )

    val result = waypointsOutsideGeofence(waypoints, 45.0, 7.0, 10.0)

    assertEquals(2, result.size)
    assert(result.any { it.latitude == 45.5 && it.longitude == 7.5 })
    assert(result.any { it.latitude == 46.0 && it.longitude == 8.0 })
}
