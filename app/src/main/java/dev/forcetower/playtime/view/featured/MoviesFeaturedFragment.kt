package dev.forcetower.playtime.view.featured

import android.R.attr.fragment
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.core.app.SharedElementCallback
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.FragmentMoviesFeaturedBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MoviesFeaturedFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesFeaturedBinding
    private lateinit var adapter: FeaturedAdapter

    private val viewModel by activityViewModels<FeaturedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionSet()
            .apply {
                duration = 225
                startDelay = 25
            }
            .addTransition(Fade().addTarget(R.id.recycler_movies))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        adapter = FeaturedAdapter(viewModel)
        val view = FragmentMoviesFeaturedBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.recyclerMovies.adapter = adapter
        binding.recyclerMovies.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
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

        viewModel.movieClick.observe(
            viewLifecycleOwner,
            EventObserver { onNavigateToMovieDetails(it) })
    }

    private fun onNavigateToMovieDetails(movie: Movie) {
        val view = findViewForTransition(binding.recyclerMovies, movie.id)
        val directions = MoviesFeaturedFragmentDirections.actionMoviesFeaturedToMovieDetails(movie.id, movie.posterPath ?: movie.backdropPath)
        view.transitionName = getString(R.string.transition_movie_poster, movie.id)
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