package com.emo8647.namazvakitwear.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_table")
data class PrayerEntity(
    @PrimaryKey val id: Int = 1,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)
