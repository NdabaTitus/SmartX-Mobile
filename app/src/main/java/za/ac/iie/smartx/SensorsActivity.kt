package za.ac.iie.smartx

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * SensorsActivity
 *
 * Fetches sensor data from a REST API using Retrofit and displays it
 * in a RecyclerView. Demonstrates asynchronous networking with coroutines.
 */
class SensorsActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "SensorsActivity"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var subtitle: TextView
    private lateinit var refreshButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_sensors)

        recyclerView = findViewById(R.id.sensorsRecyclerView)
        progressBar = findViewById(R.id.loadingIndicator)
        subtitle = findViewById(R.id.sensorsSubtitle)
        refreshButton = findViewById(R.id.refreshSensorsButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        refreshButton.setOnClickListener {
            Log.d(TAG, "Refresh tapped")
            fetchSensors()
        }

        // Initial fetch
        fetchSensors()
    }

    /**
     * Fetches sensors from the REST API using a coroutine.
     * Uses lifecycleScope so the request is cancelled if the activity is destroyed.
     */
    private fun fetchSensors() {
        Log.d(TAG, "Fetching sensors from REST API...")
        progressBar.visibility = View.VISIBLE
        subtitle.text = "Fetching from REST API..."
        recyclerView.adapter = null

        lifecycleScope.launch {
            try {
                val sensors = RetrofitClient.apiService.getSensors()
                Log.d(TAG, "Received ${sensors.size} sensors")

                recyclerView.adapter = SensorAdapter(sensors.take(20))
                subtitle.text = "Showing ${sensors.take(20).size} readings"
                Toast.makeText(
                    this@SensorsActivity,
                    "Loaded ${sensors.size} readings",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch sensors", e)
                subtitle.text = "Error: ${e.message}"
                Toast.makeText(
                    this@SensorsActivity,
                    "API Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}