package dev.forcetower.playtime.view.profile

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.FragmentProfileBinding
import dev.forcetower.playtime.view.releases.ReleasesFragmentDirections
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.executeBindingsAfter
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var waitAdapter: MovieAdapter
    private lateinit var listAdapter: MovieAdapter
    private lateinit var watchedAdapter: MovieAdapter

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionSet()
            .apply {
                duration = 225
                startDelay = 25
            }
            .addTransition(Fade().addTarget(R.id.profile_content))
            .addTransition(Slide(Gravity.TOP).addTarget(R.id.app_bar))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        waitAdapter = MovieAdapter(viewModel, 1)
        listAdapter = MovieAdapter(viewModel, 2)
        watchedAdapter = MovieAdapter(viewModel, 3)

        val view = FragmentProfileBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.executeBindingsAfter {
            lifecycleOwner = viewLifecycleOwner
        }

        binding.recyclerWaitingLaunch.apply {
            adapter = waitAdapter
            itemAnimator?.apply {
                changeDuration = 0L
                moveDuration = 0L
                addDuration = 0L
            }
        }

        binding.recyclerUserList.apply {
            adapter = listAdapter
            itemAnimator?.apply {
                changeDuration = 0L
                moveDuration = 0L
                addDuration = 0L
            }
        }

        binding.recyclerWatched.apply {
            adapter = watchedAdapter
            itemAnimator?.apply {
                changeDuration = 0L
                moveDuration = 0L
                addDuration = 0L
            }
        }

        val drawListener = object : OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                startPostponedEnterTransition()
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        }

        binding.root.viewTreeObserver.addOnPreDrawListener(drawListener)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.unreleased.collectLatest {
                waitAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.released.collectLatest {
                listAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.watched.collectLatest {
                watchedAdapter.submitData(it)
            }
        }

        viewModel.countUnreleased.observe(viewLifecycleOwner, { binding.hasWaitingItems = it != 0 })
        viewModel.countReleased.observe(viewLifecycleOwner, { binding.hasReleasedItems = it != 0 })
        viewModel.countWatched.observe(viewLifecycleOwner, { binding.hasWatchedItems = it != 0 })
        viewModel.movieClick.observe(viewLifecycleOwner, EventObserver { navigateToDetails(it) })
    }

    private fun navigateToDetails(data: Pair<Movie, Int>) {
        val movie = data.first
        val parent = when (data.second) {
            1 -> binding.recyclerWaitingLaunch
            2 -> binding.recyclerUserList
            3 -> binding.recyclerWatched
            else -> throw IllegalStateException("Adapter ${data.second} is not valid")
        }

        val view = findViewForTransition(parent, movie.id)
        val directions = ProfileFragmentDirections.actionReleasesToMovieDetails(movie.id, movie.posterPath ?: movie.backdropPath)
        val extras = FragmentNavigatorExtras(view to view.transitionName)
        (exitTransition as TransitionSet).excludeTarget(view, true)
        findNavController().navigate(directions, extras)
    }

    private fun findViewForTransition(group: ViewGroup, id: Int): View {
        group.forEach {
            if (it.getTag(R.id.movie_id_tag) == id) {
                return it
            }
        }
        return group
    }
}