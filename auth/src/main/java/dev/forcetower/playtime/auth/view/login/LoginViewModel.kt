package dev.forcetower.playtime.auth.view.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.auth.core.AuthToken
import dev.forcetower.playtime.core.source.local.PlayDB
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val database: PlayDB,
    private val token: AuthToken
) : ViewModel() {
    fun getViewModelToken(): AuthToken {
        return token
    }

    fun getDatabaseDependency(): PlayDB {
        return database
    }
}