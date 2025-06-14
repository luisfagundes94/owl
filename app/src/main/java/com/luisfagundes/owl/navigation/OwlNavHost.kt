package com.luisfagundes.owl.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.luisfagundes.device.navigation.DeviceListRoute
import com.luisfagundes.device.navigation.deviceListScreen
import com.luisfagundes.history.navigation.historyScreen
import com.luisfagundes.owl.ui.OwlAppState

@Composable
fun OwlNavHost(appState: OwlAppState, modifier: Modifier = Modifier) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = DeviceListRoute,
        modifier = modifier
    ) {
        deviceListScreen()
        historyScreen()
    }
}
