package dev.forcetower.playtime.view.releases

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.DayIndicator
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed.Companion.days
import dev.forcetower.playtime.core.model.ui.ReleasesUI
import dev.forcetower.playtime.databinding.FragmentMovieReleasesBinding
import dev.forcetower.playtime.widget.decoration.BubbleDecoration
import dev.forcetower.playtime.widget.decoration.ReleaseMonthSeparatorItemDecoration
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.clearDecorations
import dev.forcetower.toolkit.extensions.executeBindingsAfter
import dev.forcetower.toolkit.layout.JumpSmoothScroller
import dev.forcetower.toolkit.lifecycle.EventObserver
import java.time.LocalDate

@AndroidEntryPoint
class ReleasesFragment : BaseFragment() {
    private lateinit var binding: FragmentMovieReleasesBinding
    private lateinit var adapter: ReleasesAdapter
    private lateinit var dayAdapter: DayAdapter

    private val viewModel by viewModels<ReleasesViewModel>()

    private lateinit var dayIndicatorItemDecoration: BubbleDecoration
    private lateinit var dayIndexed: ReleaseDayIndexed

    private var cachedBubbleRange: IntRange? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionSet()
            .apply {
                duration = 225
                startDelay = 25
            }
            .addTransition(Fade().addTarget(R.id.recycler_releases))
            .addTransition(Slide(Gravity.TOP).addTarget(R.id.app_bar))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        dayAdapter = DayAdapter(viewModel, this)
        adapter = ReleasesAdapter(viewModel)

        val view = FragmentMovieReleasesBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.recyclerReleases.adapter = adapter
        binding.dayIndicators.adapter = dayAdapter
        binding.executeBindingsAfter {
            actions = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        dayIndicatorItemDecoration = BubbleDecoration(requireContext())
        binding.dayIndicators.addItemDecoration(dayIndicatorItemDecoration)

        binding.recyclerReleases.run {
            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }

            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
                        onReleasesScrolled()
                    }
                }
            )
        }

        viewModel.loadReleasesIfNeeded()

        val drawListener = object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                startPostponedEnterTransition()
                binding.recyclerReleases.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        }

        binding.recyclerReleases.viewTreeObserver.addOnPreDrawListener(drawListener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildDayIndicators()

        viewModel.releases.observe(viewLifecycleOwner) {
            onUiUpdate(it)
        }

        viewModel.scrollToEvent.observe(
            viewLifecycleOwner,
            EventObserver { scrollEvent ->
                if (scrollEvent.targetPosition != -1) {
                    binding.recyclerReleases.run {
                        post {
                            val lm = layoutManager as LinearLayoutManager
                            if (scrollEvent.smoothScroll) {
                                val scroller = JumpSmoothScroller(requireContext())
                                scroller.targetPosition = scrollEvent.targetPosition
                                lm.startSmoothScroll(scroller)
                            } else {
                                lm.scrollToPositionWithOffset(scrollEvent.targetPosition, 0)
                            }
                        }
                    }
                }
            }
        )

        viewModel.movieClick.observe(
            viewLifecycleOwner,
            EventObserver { onNavigateToMovieDetails(it) }
        )

        binding.dayIndicators.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    dayIndicatorItemDecoration.userScrolled = true
                }
            }
        )
    }

    private fun onUiUpdate(data: ReleasesUI) {
        adapter.submitList(data.movies)

        val indexed = data.indexer
        dayIndexed = indexed

        cachedBubbleRange = null
        if (indexed.days.isEmpty()) {
            cachedBubbleRange = -1..-1
            buildDayIndicators()
        }

        binding.recyclerReleases.run {
            clearDecorations()
            if (data.movies.isNotEmpty()) {
                addItemDecoration(
                    ReleaseMonthSeparatorItemDecoration(
                        context,
                        indexed
                    )
                )
            }
        }

        binding.executeBindingsAfter {
            isEmpty = data.movies.isEmpty()
        }
    }

    private fun onReleasesScrolled() {
        val manager = (binding.recyclerReleases.layoutManager) as LinearLayoutManager
        val first = manager.findFirstVisibleItemPosition()
        val last = manager.findLastVisibleItemPosition()
        if (first < 0 || last < 0) {
            return
        }

        val firstDay = dayIndexed.dayForPosition(first) ?: return
        val lastDay = dayIndexed.dayForPosition(last) ?: return
        val highlightRange = dayIndexed.days.indexOf(firstDay)..dayIndexed.days.indexOf(lastDay)
        if (highlightRange != cachedBubbleRange) {
            cachedBubbleRange = highlightRange
            buildDayIndicators(dayIndexed.days)
        }
    }

    private fun buildDayIndicators(dates: List<LocalDate> = emptyList()) {
        val bubbleRange = cachedBubbleRange ?: return
        val mapped = days.filter { dates.contains(it.second) }.mapIndexed { index, day ->
            DayIndicator(res = day.first, date = day.second, checked = index in bubbleRange)
        }
        dayAdapter.submitList(mapped)
        dayIndicatorItemDecoration.bubbleRange = bubbleRange
    }

    private fun onNavigateToMovieDetails(movie: Movie) {
        val view = findViewForTransition(binding.recyclerReleases, movie.id)
        val directions = ReleasesFragmentDirections.actionReleasesToMovieDetails(movie.id, movie.posterPath ?: movie.backdropPath)
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
