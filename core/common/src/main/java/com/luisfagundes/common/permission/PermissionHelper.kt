package com.luisfagundes.common.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.luisfagundes.common.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(
    permissionState: PermissionState,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onGrant: () -> Unit,
    onRequestPermission: () -> Unit,
    onDismiss: (Boolean) -> Unit
) {
    when {
        permissionState.status.isGranted -> onGrant()
        else -> {
            if (shouldShowRationale) {
                PermissionRationaleDialog(
                    message = rationaleMessage,
                    onRequestPermission = onRequestPermission,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun PermissionRationaleDialog(
    message: String,
    onRequestPermission: () -> Unit,
    onDismiss: (dontAskAgain: Boolean) -> Unit,
) {
    var dontAskAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss(dontAskAgain) },
        title = { Text(stringResource(R.string.permission_required)) },
        text = {
            DontAskAgainCheckBox(
                message = message,
                dontAskAgain = dontAskAgain,
                onDontAskAgainCheck = { dontAskAgain = it }
            )
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text(stringResource(R.string.grant_permission))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss(dontAskAgain) }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun DontAskAgainCheckBox(
    message: String,
    dontAskAgain: Boolean,
    onDontAskAgainCheck: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(message)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = dontAskAgain,
                onCheckedChange = { onDontAskAgainCheck(it) }
            )
            Text(stringResource(R.string.dont_ask_again))
        }
    }
}
