package com.luisfagundes.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Now in Android navigation suite scaffold with item and content slots.
 * Wraps Material 3 [NavigationSuiteScaffold].
 *
 * @param modifier Modifier to be applied to the navigation suite scaffold.
 * @param navigationSuiteItems A slot to display multiple items via [OwlNavigationSuiteScope].
 * @param windowAdaptiveInfo The window adaptive info.
 * @param content The app content inside the scaffold.
 */
@Composable
fun OwlNavigationSuiteScaffold(
    navigationSuiteItems: OwlNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = OwlNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = OwlNavigationDefaults.navigationContentColor(),
            selectedTextColor = OwlNavigationDefaults.navigationSelectedTextColor(),
            unselectedTextColor = OwlNavigationDefaults.navigationContentColor(),
            indicatorColor = OwlNavigationDefaults.navigationIndicatorColor()
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = OwlNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = OwlNavigationDefaults.navigationContentColor(),
            selectedTextColor = OwlNavigationDefaults.navigationSelectedTextColor(),
            unselectedTextColor = OwlNavigationDefaults.navigationContentColor(),
            indicatorColor = OwlNavigationDefaults.navigationIndicatorColor()
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = OwlNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = OwlNavigationDefaults.navigationContentColor(),
            selectedTextColor = OwlNavigationDefaults.navigationSelectedTextColor(),
            unselectedTextColor = OwlNavigationDefaults.navigationContentColor()
        )
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            OwlNavigationSuiteScope(
                navigationSuiteScope = this,
                navigationSuiteItemColors = navigationSuiteItemColors
            ).run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContentColor = OwlNavigationDefaults.navigationContentColor(),
            navigationRailContainerColor = Color.Transparent
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * A wrapper around [NavigationSuiteScope] to declare navigation items.
 */
class OwlNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        colors = navigationSuiteItemColors,
        modifier = modifier
    )
}

object OwlNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationSelectedTextColor() = MaterialTheme.colorScheme.primary

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
