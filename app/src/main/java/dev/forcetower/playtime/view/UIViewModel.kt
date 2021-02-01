package dev.forcetower.playtime.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.toolkit.lifecycle.Event
import javax.inject.Inject

@HiltViewModel
class UIViewModel @Inject constructor() : ViewModel() {
    private val _onHideBottomNav = MutableLiveData<Event<Boolean>>()
    val onHideBottomNav: LiveData<Event<Boolean>> = _onHideBottomNav

    fun hideBottomNav() {
        _onHideBottomNav.value = Event(false)
    }

    fun showBottomNav() {
        _onHideBottomNav.value = Event(true)
    }
}