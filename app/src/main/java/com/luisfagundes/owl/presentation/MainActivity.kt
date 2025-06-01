package com.luisfagundes.owl.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.luisfagundes.designsystem.theme.OwlTheme
import com.luisfagundes.network.monitor.NetworkMonitor
import com.luisfagundes.owl.ui.OwlApp
import com.luisfagundes.owl.ui.rememberOwlAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberOwlAppState(
                networkMonitor = networkMonitor
            )

            OwlTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false
            ) {
                OwlApp(
                    appState = appState,
                )
            }
        }
    }
}