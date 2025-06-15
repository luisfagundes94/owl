package com.luisfagundes.history.presentation.wifiList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisfagundes.designsystem.theme.OwlTheme
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.history.R

@Composable
internal fun WifiRouterCard(name: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = stringResource(R.string.wifi_router_icon_description)
            )
            Spacer(Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = stringResource(R.string.see_wifi_router_details)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun WifiRouterCardPreview() {
    OwlTheme {
        WifiRouterCard(
            name = "My Home WiFi",
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.default)
        )
    }
}
