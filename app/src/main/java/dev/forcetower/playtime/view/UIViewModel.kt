package dev.forcetower.playtime.view

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.toolkit.lifecycle.Event

class UIViewModel @ViewModelInject constructor() : ViewModel() {
    private val _onHideBottomNav = MutableLiveData<Event<Boolean>>()
    val onHideBottomNav: LiveData<Event<Boolean>> = _onHideBottomNav

    fun hideBottomNav() {
        _onHideBottomNav.value = Event(false)
    }

    fun showBottomNav() {
        _onHideBottomNav.value = Event(true)
    }
}