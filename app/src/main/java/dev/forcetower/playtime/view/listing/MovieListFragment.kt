package dev.forcetower.playtime.view.listing

import android.os.Bundle
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.FragmentTitleListBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : BaseFragment() {
    private lateinit var binding: FragmentTitleListBinding
    private lateinit var adapter: MovieAdapter
    private val viewModel: ListingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        val view = FragmentTitleListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        adapter = MovieAdapter(viewModel)

        binding.recyclerMovies.apply {
            adapter = this@MovieListFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0L
            }
        }

        binding.recyclerMovies.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getInt(LIST_TYPE)

        lifecycleScope.launch {
            viewModel.getListOfType(type).collectLatest {
                adapter.submitData(it)
            }
        }
        viewModel.movieClick.observe(viewLifecycleOwner, EventObserver { movie ->
            navigateToDetails(movie)
        })
    }

    private fun navigateToDetails(movie: Movie) {
        val view = findViewForTransition(binding.recyclerMovies, movie.id)
        val directions = ListingFragmentDirections.actionListingToMovieDetails(movie.id, movie.posterPath ?: movie.backdropPath)
        val extras = FragmentNavigatorExtras(view to view.transitionName)
        (exitTransition as? TransitionSet)?.excludeTarget(view, true)
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

    companion object {
        private const val LIST_TYPE = "list_type"

        fun ofOption(value: Int): MovieListFragment {
            return MovieListFragment().apply {
                arguments = bundleOf(LIST_TYPE to value)
            }
        }
    }
}