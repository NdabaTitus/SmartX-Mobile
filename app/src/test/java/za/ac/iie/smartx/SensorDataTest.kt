package za.ac.iie.smartx

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for SensorData.
 * Verifies construction, equality, and copy behaviour.
 */
class SensorDataTest {

    @Test
    fun `sensorData holds correct values`() {
        val sensor = SensorData(
            id = 1,
            deviceId = 100,
            sensorType = "Temperature",
            reading = "24.5°C"
        )

        assertThat(sensor.id).isEqualTo(1)
        assertThat(sensor.deviceId).isEqualTo(100)
        assertThat(sensor.sensorType).isEqualTo("Temperature")
        assertThat(sensor.reading).isEqualTo("24.5°C")
    }

    @Test
    fun `two identical sensorData objects are equal`() {
        val a = SensorData(1, 100, "Temperature", "24.5°C")
        val b = SensorData(1, 100, "Temperature", "24.5°C")

        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `copy creates a modified instance`() {
        val original = SensorData(1, 100, "Temperature", "24.5°C")
        val modified = original.copy(reading = "26.0°C")

        assertThat(modified.id).isEqualTo(original.id)
        assertThat(modified.reading).isEqualTo("26.0°C")
        assertThat(modified).isNotEqualTo(original)
    }
}