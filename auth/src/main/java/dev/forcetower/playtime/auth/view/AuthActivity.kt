package dev.forcetower.playtime.auth.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dev.forcetower.hilt.dynamic.DynamicAndroidEntryPoint
import dev.forcetower.playtime.auth.R
import dev.forcetower.playtime.auth.core.AuthToken
import dev.forcetower.playtime.auth.databinding.FragmentLoginBinding
import dev.forcetower.playtime.auth.view.login.LoginViewModel
import dev.forcetower.toolkit.components.BaseActivity
import timber.log.Timber
import javax.inject.Inject

@DynamicAndroidEntryPoint
class AuthActivity : DynamicHilt_AuthActivity() {
    private lateinit var binding: FragmentLoginBinding
    @Inject lateinit var token: AuthToken
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_login)
        Timber.d("Token: $token")

        Timber.d("Token on ViewModel ${viewModel.getViewModelToken()}")
        Timber.d("Database Dependency ${viewModel.getDatabaseDependency()}")
    }
}