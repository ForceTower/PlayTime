package dev.forcetower.playtime.view.releases

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class ReleasesFragment : BaseFragment() {
    private val viewModel by viewModels<ReleasesViewModel>()
}