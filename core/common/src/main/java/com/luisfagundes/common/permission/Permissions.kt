package com.luisfagundes.common.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.luisfagundes.common.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(permission: String, rationaleMessage: String, onGrant: () -> Unit) {
    val permissionState = rememberPermissionState(permission)
    val showRationale = remember { mutableStateOf(true) }

    when {
        permissionState.status.isGranted -> onGrant()
        else -> {
            if (showRationale.value) {
                PermissionRationaleDialog(
                    message = rationaleMessage,
                    onRequestPermission = { permissionState.launchPermissionRequest() },
                    onDismiss = { showRationale.value = false }
                )
            }
        }
    }
}

@Composable
private fun PermissionRationaleDialog(
    message: String,
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.permission_required)) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text(stringResource(R.string.grant_permission))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
