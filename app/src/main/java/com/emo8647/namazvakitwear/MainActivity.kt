package com.emo8647.namazvakitwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Ekran açıldığında API'den gerçek veriyi çek
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
                // Hata durumunda null kalır
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = prayerData?.let { "İmsak: ${it.fajr}" } ?: "Bağlantı Hatası!")
            }
        }
    }
}
