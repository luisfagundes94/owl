package com.luisfagundes.common.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class State<State : UiState>(initialState: State) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> get() = _state

    fun update(state: (State) -> State) {
        _state.value = state(_state.value)
    }
}
