package com.luisfagundes.common.presentation

import androidx.lifecycle.ViewModel

interface UiState

abstract class ViewModel<State : UiState>(
    initialState: State
) : ViewModel() {
    private val viewModelState = State(initialState)

    val uiState = viewModelState.state

    protected fun updateState(block: State.() -> State) {
        viewModelState.update { it.block() }
    }
}
