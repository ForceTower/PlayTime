package dev.forcetower.playtime.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.databinding.FragmentListingBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class ListingFragment : BaseFragment() {
    private lateinit var binding: FragmentListingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  FragmentListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        setupViewPager()
        return view
    }

    private fun setupViewPager() {
        val fragments = listOf(
            getString(R.string.listing_my_list) to MovieListFragment.ofOption(1),
            getString(R.string.listing_watched) to MovieListFragment.ofOption(2),
        )
        binding.viewPager.adapter = PagerAdapter(childFragmentManager, fragments)
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private inner class PagerAdapter(
        fm: FragmentManager,
        private val fragments: List<Pair<String, Fragment>>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = fragments.size

        override fun getItem(position: Int) = fragments[position].second

        override fun getPageTitle(position: Int) = fragments[position].first
    }
}