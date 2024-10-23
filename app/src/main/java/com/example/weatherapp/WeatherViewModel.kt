package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherApiInterface=RetrofitInstance.weatherApi
    private val geolocationApiInterface = RetrofitInstance.geoApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult
    init {
        getCityFromIpAndFetchWeather()
    }

    private fun getCityFromIpAndFetchWeather() {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching city from IP...")
                val geoResponse = geolocationApiInterface.getLocationData()
                if (geoResponse.isSuccessful) {
                    geoResponse.body()?.let { location ->
                        val cityName = location.city
                        Log.i("WeatherViewModel", "City fetched: $cityName")
                        getData(cityName)
                    }?: run {
                        Log.e("WeatherViewModel", "Failed to load: Empty response")
                        _weatherResult.value = NetworkResponse.Error("Failed to load: Empty response")
                    }
                } else {
                    Log.e("WeatherViewModel", "Failed to load location: ${geoResponse.message()}")
                    _weatherResult.value = NetworkResponse.Error("Failed to get location${geoResponse.message()}")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Failed to load location: ${e.message}", e)
                _weatherResult.value = NetworkResponse.Error("Failed to get location")
                e.printStackTrace()
            }
        }
    }

    fun getData(city:String){
        _weatherResult.value=NetworkResponse.Loading
        Log.d("WeatherViewModel", "Fetching weather for city: $city")
        viewModelScope.launch {
            try {
                val response = weatherApiInterface.getWeather(Constant.apikey, city)
                if (response.isSuccessful){
                    response.body()?.let {
                        Log.i("WeatherViewModel", "Weather data loaded successfully")
                        _weatherResult.value=NetworkResponse.Success(it)
                    }?: run {
                        Log.e("WeatherViewModel", "Failed to load weather data: Empty response")
                        _weatherResult.value = NetworkResponse.Error("Failed to load: Empty weather data")
                    }
                }else{
                    Log.e("WeatherViewModel", "Failed to load weather: ${response.message()}")
                    _weatherResult.value=NetworkResponse.Error("Failed to load weather: ${response.message()}")
                }
            }catch (e:Exception){
                Log.e("WeatherViewModel", "Failed to load weather: ${e.message}", e)
                _weatherResult.value=NetworkResponse.Error("Failed to load weather: ${e.message}")
                e.printStackTrace()
            }


        }
    }
}