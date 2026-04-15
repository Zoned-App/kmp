package com.github.zoned.app

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
actual fun MapView(modifier: Modifier, lat: Double, lon: Double) {
    val loc = LatLng(lat, lon)
    val colorScheme = MaterialTheme.colorScheme

    // 1. Re-generate the style only when the colorScheme changes
    val style = remember(colorScheme) {
        themeJson(colorScheme)
    }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(loc, 16f)
    }

    GoogleMap(
        modifier = modifier, cameraPositionState = cameraPosition, googleMapOptionsFactory = {
            GoogleMapOptions().liteMode(true)
        }, uiSettings = MapUiSettings(mapToolbarEnabled = false),
        onMapClick = {},
        properties = MapProperties(mapStyleOptions = style)
    ) {
        Circle(
            center = loc,
            fillColor = Color.Red.copy(alpha = 0.2f),
            radius = 300.0,
            strokeColor = Color.Red.copy(alpha = 0.3f)
        )
    }
}

// 2. Helper to get clean #RRGGBB hex strings
fun Color.toMapHex(): String = String.format("#%06X", 0xFFFFFF and this.toArgb())

fun themeJson(colorScheme: ColorScheme): MapStyleOptions {
    val json = """
    [
      {
        "featureType": "all",
        "elementType": "geometry",
        "stylers": [ { "color": "${colorScheme.surface.toMapHex()}" } ]
      },
      {
        "featureType": "water",
        "elementType": "geometry",
        "stylers": [ { "color": "${colorScheme.primaryContainer.toMapHex()}" } ]
      },
      {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": [ { "color": "${colorScheme.surfaceVariant.toMapHex()}" } ]
      },
      {
        "elementType": "labels.text.fill",
        "stylers": [ { "color": "${colorScheme.onSurface.toMapHex()}" } ]
      }
    ]
    """.trimIndent()

    return MapStyleOptions(json)
}