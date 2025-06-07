package com.luisfagundes.history.extensions

import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue


val SwipeToDismissBoxState.isEndToStartDirection: Boolean
    get() = this.dismissDirection == SwipeToDismissBoxValue.EndToStart

val SwipeToDismissBoxState.isSettledDirection: Boolean
    get() = this.dismissDirection == SwipeToDismissBoxValue.Settled