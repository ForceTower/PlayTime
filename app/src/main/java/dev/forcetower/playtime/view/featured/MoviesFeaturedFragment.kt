package dev.forcetower.playtime.view.featured

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.FragmentMoviesFeaturedBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.closeKeyboardWithActivity
import dev.forcetower.toolkit.extensions.openKeyboardWithActivity
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
@ExperimentalPagingApi
class MoviesFeaturedFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesFeaturedBinding
    private lateinit var adapter: FeaturedAdapter
    private lateinit var searchAdapter: FeaturedAdapter

    private val viewModel by activityViewModels<FeaturedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionSet()
            .apply {
                duration = 225
                startDelay = 25
            }
            .addTransition(Fade().addTarget(R.id.recycler_movies))
            .addTransition(Slide(Gravity.TOP).addTarget(R.id.app_bar))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        adapter = FeaturedAdapter(viewModel)
        searchAdapter = FeaturedAdapter(viewModel, true)

        val view = FragmentMoviesFeaturedBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.recyclerMovies.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        binding.searchView.run {
            binding.btnSearch.setOnClickListener {
                viewModel.setSearching(true)
                binding.inputSearch.openKeyboardWithActivity(requireActivity())
            }
            binding.btnCancelSearch.setOnClickListener {
                viewModel.setSearching(false)
            }
            binding.inputSearch.doAfterTextChanged { text ->
                val query = text.toString()
                if (query != viewModel.query) {
                    viewModel.query = query
                    searchAdapter.refresh()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.movies().collect {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.searchSource.collect {
                searchAdapter.submitData(it)
            }
        }

        viewModel.isSearching.observe(viewLifecycleOwner) { searching ->
            if (!searching) {
                binding.recyclerMovies.adapter = adapter
                binding.searchView.run {
                    if (isAttachedToWindow) closeSearch()
                    binding.inputSearch.closeKeyboardWithActivity(requireActivity())
                }
            } else {
                binding.recyclerMovies.adapter = searchAdapter
                binding.searchView.run { openSearch() }
            }
        }

        viewModel.movieClick.observe(
            viewLifecycleOwner,
            EventObserver { onNavigateToMovieDetails(it) }
        )
    }

    private fun onNavigateToMovieDetails(movie: Movie) {
        val view = findViewForTransition(binding.recyclerMovies, movie.id)
        val directions = MoviesFeaturedFragmentDirections.actionMoviesFeaturedToMovieDetails(movie.id, movie.posterPath ?: movie.backdropPath)
        val extras = FragmentNavigatorExtras(view to view.transitionName)
        (exitTransition as TransitionSet).excludeTarget(view, true)
        findNavController().navigate(directions, extras)
    }

    private fun findViewForTransition(group: ViewGroup, id: Int): View {
        group.forEach {
            if (it.getTag(R.id.movie_id_tag) == id) {
                return it.findViewById(R.id.cover)
            }
        }
        return group
    }

}