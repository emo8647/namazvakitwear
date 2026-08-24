package com.emo8647.namazvakitwear

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ -> }
        locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        setContent {
            NamazVakitApp(this)
        }
    }
}

@Composable
fun NamazVakitApp(context: Context) {
    var cityName by remember { mutableStateOf("Taranıyor...") }
    var timings by remember { mutableStateOf<PrayerEntity?>(null) }
    var fontSizeMultiplier by remember { mutableFloatStateOf(1.0f) } // Wear OS Slider ile yazı boyutu ayarı
    val scope = rememberCoroutineScope()
    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    LaunchedEffect(Unit) {
        scope.launch {
            val locationHelper = LocationHelper(context)
            val detectedCity = locationHelper.getCurrentCity()
            cityName = detectedCity

            val db = Room.databaseBuilder(context, AppDatabase::class.java, "namaz-db").build()
            val dao = db.prayerDao()

            val localData = dao.getTodayTimings(todayDate)
            if (localData != null) {
                timings = localData
            } else {
                try {
                    val res = RetrofitClient.instance.getTimings(city = detectedCity)
                    val apiTimings = res.data.timings
                    val entity = PrayerEntity(
                        date = todayDate,
                        city = detectedCity,
                        fajr = apiTimings["Fajr"] ?: "",
                        sunrise = apiTimings["Sunrise"] ?: "",
                        dhuhr = apiTimings["Dhuhr"] ?: "",
                        asr = apiTimings["Asr"] ?: "",
                        maghrib = apiTimings["Maghrib"] ?: "",
                        isha = apiTimings["Isha"] ?: ""
                    )
                    dao.insertTimings(entity)
                    timings = entity
                } catch (e: Exception) {
                    // Hata yönetimi
                }
            }
        }
    }

    Scaffold {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
                          horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = cityName,
                     style = MaterialTheme.typography.titleMedium,
                     modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            timings?.let { data ->
                val list = listOf(
                    "İmsak" to data.fajr,
                    "Güneş" to data.sunrise,
                    "Öğle" to data.dhuhr,
                    "İkindi" to data.asr,
                    "Akşam" to data.maghrib,
                    "Yatsı" to data.isha
                )
                items(list.size) { i ->
                    val (title, time) = list[i]
                    Card(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = title, style = MaterialTheme.typography.bodyMedium)
                            Text(text = time, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            } ?: item {
                CircularProgressIndicator()
            }

            // Wear OS Dokunmatik Slider (Yazı boyutu hassasiyeti)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                InlineSlider(
                    value = fontSizeMultiplier,
                    onValueChange = { fontSizeMultiplier = it },
                    valueRange = 0.8f..1.4f,
                    steps = 5,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }
        }
    }
}
