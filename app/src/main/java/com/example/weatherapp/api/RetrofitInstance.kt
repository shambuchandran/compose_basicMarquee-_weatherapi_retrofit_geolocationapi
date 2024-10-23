package com.example.weatherapp.api

import com.example.weatherapp.ipapi.GeolocationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    // Create a single OkHttpClient to be reused by both weather and geolocation APIs
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Generic function to create Retrofit instance
    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Weather API
    private const val WEATHER_API_BASE_URL = "https://api.weatherapi.com"
    val weatherApi: WeatherApiInterface by lazy {
        getRetrofit(WEATHER_API_BASE_URL).create(WeatherApiInterface::class.java)
    }

    // Geolocation API
    private const val GEOLOCATION_API_BASE_URL = "https://ipinfo.io/"
    val geoApi: GeolocationApi by lazy {
        getRetrofit(GEOLOCATION_API_BASE_URL).create(GeolocationApi::class.java)
    }
}