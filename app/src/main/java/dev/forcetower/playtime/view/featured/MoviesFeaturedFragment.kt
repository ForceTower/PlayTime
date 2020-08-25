package dev.forcetower.playtime.view.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.databinding.FragmentMoviesFeaturedBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MoviesFeaturedFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesFeaturedBinding
    private lateinit var adapter: FeaturedAdapter

    private val viewModel by activityViewModels<FeaturedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = FeaturedAdapter(viewModel)
        return FragmentMoviesFeaturedBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMovies.adapter = adapter

        lifecycleScope.launch {
            viewModel.movies().collect {
                adapter.submitData(it)
            }
        }

        viewModel.movieClick.observe(viewLifecycleOwner, EventObserver {
            val directions = MoviesFeaturedFragmentDirections.actionMoviesFeaturedToMovieDetails(it.id)
            findNavController().navigate(directions)
        })
    }
}