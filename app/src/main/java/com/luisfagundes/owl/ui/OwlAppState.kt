package com.luisfagundes.owl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.luisfagundes.device.navigation.navigateToDeviceList
import com.luisfagundes.history.navigation.navigateToHistory
import com.luisfagundes.network.monitor.NetworkMonitor
import com.luisfagundes.owl.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberOwlAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): OwlAppState = remember(
    networkMonitor,
    navController,
    coroutineScope
) {
    OwlAppState(
        networkMonitor = networkMonitor,
        navController = navController,
        coroutineScope = coroutineScope
    )
}

@Stable
class OwlAppState(
    val networkMonitor: NetworkMonitor,
    val navController: NavHostController,
    coroutineScope: CoroutineScope
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val shouldShowBottomBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.DEVICES -> navController.navigateToDeviceList(topLevelNavOptions)
            TopLevelDestination.HISTORY -> navController.navigateToHistory(topLevelNavOptions)
        }
    }
}
