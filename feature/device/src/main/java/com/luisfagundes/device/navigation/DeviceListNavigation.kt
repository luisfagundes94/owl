package com.luisfagundes.device.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luisfagundes.device.presentation.list.DeviceListRoute
import kotlinx.serialization.Serializable

@Serializable data object DeviceListRoute

fun NavController.navigateToDeviceList(navOptions: NavOptions) =
    navigate(route = DeviceListRoute, navOptions)

fun NavGraphBuilder.deviceListScreen() {
    composable<DeviceListRoute> {
        DeviceListRoute()
    }
}