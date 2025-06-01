package com.luisfagundes.designsystem.component

import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import com.luisfagundes.designsystem.R
import com.luisfagundes.designsystem.theme.customColorPalette
import com.luisfagundes.designsystem.theme.spacing

@Composable
fun DeviceCard(
    modifier: Modifier = Modifier,
    hostName: String,
    ipAddress: String,
    isActive: Boolean
) {
    Card(
        modifier = modifier,
        enabled = isActive,
        onClick = {}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.default)
            ) {
                Text(
                    text = if (hostName.isEmpty() || hostName == ipAddress) {
                        stringResource(R.string.device_unknown)
                    } else {
                        hostName
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = ipAddress,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    }
                )
            }
            if (isActive) {
                Icon(
                    modifier = Modifier
                        .scale(0.5f)
                        .padding(MaterialTheme.spacing.default),
                    imageVector = Icons.Default.Circle,
                    contentDescription = stringResource(id = R.string.device_status),
                    tint = MaterialTheme.customColorPalette.activeGreen
                )
            }
        }
    }
}
