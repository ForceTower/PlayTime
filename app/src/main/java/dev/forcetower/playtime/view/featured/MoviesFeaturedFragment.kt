package dev.forcetower.playtime.view.featured

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.FragmentMoviesFeaturedBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.closeKeyboardWithActivity
import dev.forcetower.toolkit.extensions.openKeyboardWithActivity
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
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

        val drawListener = object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                startPostponedEnterTransition()
                binding.recyclerMovies.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        }

        binding.recyclerMovies.viewTreeObserver.addOnPreDrawListener(drawListener)

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

        lifecycleScope.launchWhenCreated {
            viewModel.movies.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.searchSource.collectLatest {
                searchAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        val backHandler = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                viewModel.setSearching(false)
            }
        }

        viewModel.isSearching.observe(viewLifecycleOwner) { searching ->
            backHandler.isEnabled = searching
            binding.swipeRefresh.isEnabled = !searching
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backHandler)
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