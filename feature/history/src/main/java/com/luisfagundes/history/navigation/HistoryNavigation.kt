package com.luisfagundes.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luisfagundes.history.presentation.HistoryRoute
import kotlinx.serialization.Serializable

@Serializable data object HistoryRoute

fun NavController.navigateToHistory(navOptions: NavOptions) =
    navigate(route = HistoryRoute, navOptions)

fun NavGraphBuilder.historyScreen() {
    composable<HistoryRoute> {
        HistoryRoute()
    }
}
