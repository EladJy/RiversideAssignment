package com.ej.riversideassignment.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T: BaseIntent> : ViewModel() {

    private val _intent = MutableStateFlow<T?>(null)
    val intent: StateFlow<T?> get() = _intent.asStateFlow()

    protected val event: T?
        get() = _intent.value

    fun sendEvent(newEvent: T) {
        _intent.value = newEvent
        onTriggerEvent(newEvent)
    }

    abstract fun onTriggerEvent(eventType: T)
}