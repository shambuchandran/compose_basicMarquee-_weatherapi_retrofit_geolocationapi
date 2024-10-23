package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult=viewModel.weatherResult.observeAsState()
    val keyboardController= LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = city, onValueChange = { city = it },
                label = { Text(text = "Search for the location") },
                trailingIcon = {
                    Image(imageVector = Icons.Rounded.Search, contentDescription = "search",
                        modifier = Modifier.clickable {
                            viewModel.getData(city)
                            keyboardController?.hide()
                        })
                })

        }
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> { CircularProgressIndicator()}
            is NetworkResponse.Success -> {
                WeatherUi(data = result.data)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherUi(data:WeatherModel) {

    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally){
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription ="location" , modifier = Modifier.size(40.dp))
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 14.sp, color = Color.DarkGray)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${data.current.temp_c}'C", fontSize = 56.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.DarkGray)
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "icon",
        )
        Text(text = "${data.current.condition.text}'C", fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeys(key = data.current.humidity, value ="Humidity" )
                    WeatherKeys(key = data.current.wind_kph+"km/h", value ="Wind Speed" )
                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeys(key = data.current.uv, value ="UV" )
                    WeatherKeys(key = data.current.cloud, value ="Cloud" )
                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeys(key = data.location.localtime.split(" ")[1], value ="Time" )
                    WeatherKeys(key = data.location.localtime.split(" ")[0], value ="Date" )
                }

            }

        }

    }
}

@Composable
fun WeatherKeys(key:String,value: String) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text =key, fontWeight = FontWeight.SemiBold, color = Color.Gray )
    }
    
}