package com.emo8647.namazvakitwear

import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.future

class NamazTileService : TileService() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        return serviceScope.future {
            val response = try {
                RetrofitClient.instance.getTimings()
            } catch (e: Exception) {
                null
            }

            val nextPrayerText = response?.data?.timings?.get("Fajr")?.let { "İmsak: $it" } ?: "Yükleniyor..."

            val layout = LayoutElementBuilders.Column.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(
                Text.Builder(applicationContext, nextPrayerText)
                .setTypography(Typography.TYPOGRAPHY_TITLE3)
                .setColor(Colors.DEFAULT.primary)
                .build()
            )
            .build()

            TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                .addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(LayoutElementBuilders.Layout.Builder().setRoot(layout).build())
                    .build()
                ).build()
            ).build()
        }
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return serviceScope.future {
            ResourceBuilders.Resources.Builder().setVersion("1").build()
        }
    }
}
