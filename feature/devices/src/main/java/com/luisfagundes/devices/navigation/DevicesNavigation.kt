package com.luisfagundes.devices.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luisfagundes.devices.presentation.DevicesRoute
import kotlinx.serialization.Serializable

@Serializable data object DevicesRoute

fun NavController.navigateToDevices(navOptions: NavOptions) =
    navigate(route = DevicesRoute, navOptions)

fun NavGraphBuilder.devicesScreen() {
    composable<DevicesRoute> {
        DevicesRoute()
    }
}