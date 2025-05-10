package com.luisfagundes.discover.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luisfagundes.discover.presentation.DiscoverRoute
import kotlinx.serialization.Serializable

@Serializable data object DiscoverRoute

fun NavController.navigateToDiscover(navOptions: NavOptions) =
    navigate(route = DiscoverRoute, navOptions)

fun NavGraphBuilder.discoverScreen() {
    composable<DiscoverRoute> {
        DiscoverRoute()
    }
}