package za.ac.iie.smartx

import com.google.gson.annotations.SerializedName


data class SensorData(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val deviceId: Int,
    @SerializedName("title") val sensorType: String,
    @SerializedName("body") val reading: String
)