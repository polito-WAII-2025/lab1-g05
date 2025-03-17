import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertFailsWith

@Test
fun `test loadConfig - file valido`() {
    val yamlContent = """
        earthRadiusKm: 6371
        geofenceCenterLatitude: 45.0
        geofenceCenterLongitude: 7.0
        geofenceRadiusKm: 5.0
    """.trimIndent()

    val tempFile = File.createTempFile("config", ".yml").apply {
        writeText(yamlContent)
    }

    val config = ConfigReader.loadConfig(tempFile.absolutePath)
    assertEquals(6371.0, config.earthRadiusKm)
    assertEquals(45.0, config.geofenceCenterLatitude)
    assertEquals(7.0, config.geofenceCenterLongitude)
    assertEquals(5.0, config.geofenceRadiusKm)
}

@Test
fun `test loadConfig - file inesistente`() {
    assertFailsWith<IllegalArgumentException> {
        ConfigReader.loadConfig("non_esiste.yml")
    }
}