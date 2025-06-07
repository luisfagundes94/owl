package com.luisfagundes.history.extensions

import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue

val SwipeToDismissBoxState.isEndToStartDirection: Boolean
    get() = this.currentValue == SwipeToDismissBoxValue.EndToStart

val SwipeToDismissBoxState.isStartToEndDirection: Boolean
    get() = this.currentValue == SwipeToDismissBoxValue.StartToEnd

val SwipeToDismissBoxState.isSettledDirection: Boolean
    get() = this.currentValue == SwipeToDismissBoxValue.Settled