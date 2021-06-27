package dev.forcetower.playtime.auth.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.auth.core.AuthToken
import dev.forcetower.playtime.auth.databinding.FragmentLoginBinding
import dev.forcetower.playtime.view.UIViewModel
import dev.forcetower.playtime.view.details.DetailsViewModel
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject lateinit var test: AuthToken
    private val detailsVM by viewModels<DetailsViewModel>()
    private val uiVM by activityViewModels<UIViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::test.isInitialized) {
            Timber.d("Injection of test actually happened!")
        }

        try {
            detailsVM.movie(550)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            uiVM.hideBottomNav()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}