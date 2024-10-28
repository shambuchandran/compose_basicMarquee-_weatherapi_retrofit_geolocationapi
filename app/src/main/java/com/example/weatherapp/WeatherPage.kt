package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import coil3.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

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
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                TextScreen()
                Train()
                ScrollingWeather(result.data)
                WeatherUi(data = result.data)

            }

            null -> {}
        }
    }
}

@Composable
fun WeatherUi(data: WeatherModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "location",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 14.sp, color = Color.DarkGray)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c}'C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "icon",
        )
        Text(
            text = "${data.current.condition.text}'C",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeys(key = data.current.humidity, value = "Humidity")
                    WeatherKeys(key = data.current.wind_kph + "km/h", value = "Wind Speed")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeys(key = data.current.uv, value = "UV")
                    WeatherKeys(key = data.current.cloud, value = "Cloud")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeys(key = data.location.localtime.split(" ")[1], value = "Time")
                    WeatherKeys(key = data.location.localtime.split(" ")[0], value = "Date")
                }

            }

        }
    }

}

@Composable
fun TextScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(vertical = 8.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    initialDelayMillis = 0,
                    repeatDelayMillis = 0,
                    velocity = 100.dp
                ),
            color = Color.White,
            text = "However, in the last year, I decided to challenge myself and shift my focus toward Android development. I enrolled in an online bootcamp called Brototype, where I’ve been self-learning and exploring the technical aspects of app development. I’m someone who constantly seeks to do things differently and bring a creative, experimental approach to my work I love taking on challenges that push me to grow, both in terms of knowledge and skill. That’s why I’m excited to apply for the Junior Android Developer position here at ABC Company. Your company’s vision aligns with my future goals, and I’m confident that my drive to innovate, combined with my communication skills from previous roles, will allow me to contribute effectively to your team."
        )

    }
}

@Composable
fun Train() {
    val focusRequester= remember {
        FocusRequester()
    }
    Row (modifier = Modifier
        .basicMarquee(
            animationMode = MarqueeAnimationMode.WhileFocused,
            velocity = 100.dp
        )
        .focusRequester(focusRequester)
        .focusable()){
        Image(painter = painterResource(id = R.drawable.train),contentDescription = null, modifier = Modifier.size(40.dp))
        repeat(20) {
            Image(
                painter = painterResource(id = R.drawable.wagon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
    Button(onClick = { focusRequester.requestFocus() }) {
        Text(text = "start")
        
    }

}

@Composable
fun ScrollingWeather(data: WeatherModel) {
    val focusRequester= remember {
        FocusRequester()
    }
    Row (modifier = Modifier
        .basicMarquee(
            animationMode = MarqueeAnimationMode.WhileFocused,
            velocity = 100.dp
        )
        .focusRequester(focusRequester)
        .focusable()){
        Card {
            WeatherKeys(key = data.current.humidity, value = "Humidity")
        }
        Card {
            WeatherKeys(key = data.current.wind_kph + "km/h", value = "Wind Speed")
        }
        Card {
            WeatherKeys(key = data.current.uv, value = "UV")
        }
        Card {
            WeatherKeys(key = data.current.cloud, value = "Cloud")
        }
        Card {
            WeatherKeys(key = data.location.localtime.split(" ")[1], value = "Time")

        }
        Card {
            WeatherKeys(key = data.location.localtime.split(" ")[0], value = "Date")
        }
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
        GradientCapsule(text1 = data.current.humidity, text2 = "Humidity")
    }
    Button(onClick = { focusRequester.requestFocus() }) {
        Text(text = "start")

    }
}

@Composable
fun WeatherKeys(key: String, value: String) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }

}
@Composable
fun GradientCapsule(
    text1: String,
    text2: String,
) {

    val gradientBrush = Brush.horizontalGradient(
        //colors = listOf(Color(0xFF67e6dc.copy(alpha = 0.7f)), Color(0xFF7d5fff).copy(alpha = 0.7f)), // Define your gradient colors
        colors = listOf(Color(0xB300BFFF), Color(0xB387CEFA)),
        //colors = listOf(Color(0xFF40E0D0), Color(0xFFAFEEEE)),
        startX = 0f,
        endX = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = Modifier
            .size(160.dp, 60.dp) // Set the size of the capsule
            .background(
                gradientBrush,
                shape = RoundedCornerShape(30.dp)
            ) // Apply gradient and shape
            .border(1.dp, Color.Transparent, shape = RoundedCornerShape(30.dp)) // Optional border
            .padding(8.dp) // Padding inside the capsule
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Center items in the capsule
        ) {
                Text(
                    text = text1,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White // Change to white for better contrast on dark background
                )
                Text(
                    text = text2,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White // Change to white for better contrast on dark background
                )
        }
    }
}