package com.luisfagundes.owl.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
) {
    DEVICES(
        unselectedIcon = Icons.Default.Devices,
        selectedIcon = Icons.Filled.Devices,
        iconTextId = com.luisfagundes.device.R.string.feature_devices_title,
        titleTextId = com.luisfagundes.device.R.string.feature_devices_title,
        route = com.luisfagundes.device.navigation.DeviceListRoute::class
    ),
    HISTORY(
        unselectedIcon = Icons.Default.History,
        selectedIcon = Icons.Filled.History,
        iconTextId = com.luisfagundes.history.R.string.feature_history_title,
        titleTextId = com.luisfagundes.history.R.string.feature_history_title,
        route = com.luisfagundes.history.navigation.HistoryRoute::class
    ),
    DISCOVER(
        unselectedIcon = Icons.Default.Explore,
        selectedIcon = Icons.Filled.Explore,
        iconTextId = com.luisfagundes.discover.R.string.feature_discover_title,
        titleTextId = com.luisfagundes.discover.R.string.feature_discover_title,
        route = com.luisfagundes.discover.navigation.DiscoverRoute::class
    )
}