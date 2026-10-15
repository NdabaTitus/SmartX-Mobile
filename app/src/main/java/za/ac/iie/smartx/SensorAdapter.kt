package za.ac.iie.smartx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * SensorAdapter
 *
 * RecyclerView adapter that binds a list of SensorData to the item_sensor layout.
 * Follows the standard ViewHolder pattern for efficient scrolling.
 *
 * Since JSONPlaceholder returns Lorem Ipsum placeholder text, this adapter
 * transforms the raw API response into realistic IoT sensor readings based on
 * each record's ID. This demonstrates an ETL (Extract-Transform-Load) pattern
 * where external data is adapted to the application's domain model.
 */
class SensorAdapter(private val sensors: List<SensorData>) :
    RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sensorId: TextView = view.findViewById(R.id.sensorId)
        val sensorTitle: TextView = view.findViewById(R.id.sensorTitle)
        val sensorBody: TextView = view.findViewById(R.id.sensorBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensor = sensors[position]

        // Map each sensor ID to a rotating list of realistic IoT sensor types.
        val sensorTypes = listOf(
            "Soil Moisture",
            "Temperature",
            "Humidity",
            "Power Wattage",
            "Valve State",
            "Air Quality"
        )

        val index = sensor.id % sensorTypes.size
        val generatedType = sensorTypes[index]

        // Generate a plausible reading based on the sensor type.
        val generatedReading = when (index) {
            0 -> "Moisture: ${40 + sensor.id % 30}.${sensor.id % 10}%"
            1 -> "Temp: ${18 + sensor.id % 15}.${sensor.id % 10}°C"
            2 -> "Humidity: ${55 + sensor.id % 25}%RH"
            3 -> "Load: ${1200 + sensor.id * 37} W"
            4 -> "Valve: ${if (sensor.id % 2 == 0) "OPEN" else "CLOSED"}"
            else -> "AQI: ${35 + sensor.id % 40}"
        }

        holder.sensorId.text = "Sensor #${sensor.id} (Device ${sensor.deviceId})"
        holder.sensorTitle.text = generatedType
        holder.sensorBody.text = generatedReading
    }

    override fun getItemCount(): Int = sensors.size
}
