package za.ac.iie.smartx

import retrofit2.http.GET


interface ApiService {


    @GET("posts")
    suspend fun getSensors(): List<SensorData>
}