package com.emo8647.namazvakitwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.emo8647.namazvakitwear.data.PrayerEntity
import com.emo8647.namazvakitwear.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var prayerData by remember { mutableStateOf<PrayerEntity?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getTimings()
                val timings = response.data.timings
                prayerData = PrayerEntity(
                    fajr = timings["Fajr"] ?: "--:--",
                    sunrise = timings["Sunrise"] ?: "--:--",
                    dhuhr = timings["Dhuhr"] ?: "--:--",
                    asr = timings["Asr"] ?: "--:--",
                    maghrib = timings["Maghrib"] ?: "--:--",
                    isha = timings["Isha"] ?: "--:--"
                )
            } catch (e: Exception) {
                // Hata durumu
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold {
        if (isLoading) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item { CircularProgressIndicator() }
            }
        } else if (prayerData != null) {
            val p = prayerData!!
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item { Text(text = "İmsak: ${p.fajr}") }
                item { Text(text = "Güneş: ${p.sunrise}") }
                item { Text(text = "Öğle: ${p.dhuhr}") }
                item { Text(text = "İkindi: ${p.asr}") }
                item { Text(text = "Akşam: ${p.maghrib}") }
                item { Text(text = "Yatsı: ${p.isha}") }
            }
        } else {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item { Text(text = "Bağlantı Hatası!") }
            }
        }
    }
}
