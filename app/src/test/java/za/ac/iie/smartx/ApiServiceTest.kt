package za.ac.iie.smartx

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Unit tests for ApiService using MockWebServer.
 * Verifies the Retrofit integration returns correctly parsed data.
 */
class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getSensors returns parsed list from API`() = runTest {
        val jsonResponse = """
            [
                {"id":1,"userId":100,"title":"Temperature","body":"24.5C"},
                {"id":2,"userId":101,"title":"Humidity","body":"65%"}
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json")
        )

        val sensors = apiService.getSensors()

        assertThat(sensors).hasSize(2)
        assertThat(sensors[0].id).isEqualTo(1)
        assertThat(sensors[0].sensorType).isEqualTo("Temperature")
        assertThat(sensors[1].sensorType).isEqualTo("Humidity")
    }

    @Test
    fun `getSensors returns empty list when API returns no data`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]")
                .addHeader("Content-Type", "application/json")
        )

        val sensors = apiService.getSensors()

        assertThat(sensors).isEmpty()
    }
}