package com.github.zoned.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(modifier: Modifier, lat: Double, lon: Double)
