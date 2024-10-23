package com.example.weatherapp.ipapi

import retrofit2.Response
import retrofit2.http.GET


interface GeolocationApi {
    @GET("json")
    suspend fun getLocationData(): Response<GeolocationResponse>
}
