package dev.forcetower.playtime.view.details

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.ArcMotion
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.databinding.FragmentMovieDetailsBinding
import dev.forcetower.playtime.view.UIViewModel
import dev.forcetower.playtime.widget.behavior.ScrollingAlphaBehavior
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.windowInsetsControllerCompat
import dev.forcetower.toolkit.lifecycle.EventObserver
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var providersAdapter: ProviderAdapter

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel>()
    private val uiViewModel by activityViewModels<UIViewModel>()

    private var overlayAnimator: ValueAnimator? = null
    private var animationsRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiViewModel.hideBottomNav()
        postponeEnterTransition(500L, TimeUnit.MILLISECONDS)

        val transition = TransitionSet()
            .addTransition(
                ChangeBounds().apply { pathMotion = ArcMotion() }
            )
            .addTransition(ChangeTransform())
            .addTransition(ChangeClipBounds())
            .addTransition(ChangeImageTransform())
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .setInterpolator(FastOutSlowInInterpolator())

        sharedElementEnterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentMovieDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lastImage = args.lastImage
            binding.actions = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }.root

        imagesAdapter = ImagesAdapter()
        providersAdapter = ProviderAdapter()
        binding.recyclerImages.apply {
            adapter = imagesAdapter
            itemAnimator?.run {
                changeDuration = 0L
            }
        }
        binding.recyclerProviders.apply {
            adapter = providersAdapter
            itemAnimator?.run {
                changeDuration = 0L
            }
        }

        binding.up.setOnClickListener { findNavController().popBackStack() }
        binding.cover.transitionName = getString(R.string.transition_movie_poster, args.movieId)
        lifecycle.addObserver(binding.youtubePlayerView)
        viewModel.setMovieId(args.movieId)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.images.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
        }

        viewModel.onPosterLoaded.observe(
            viewLifecycleOwner,
            EventObserver {
                startPostponedEnterTransition()
            }
        )

        viewModel.overlayColor.observe(viewLifecycleOwner) {
            animateColorOverlayTo(it)
        }

        viewModel.video.observe(viewLifecycleOwner) { video ->
            binding.youtubePlayerView.visibility = View.VISIBLE
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(video.key, 0.0f)
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    when (state) {
                        PlayerConstants.PlayerState.PLAYING -> dimBackground()
                        PlayerConstants.PlayerState.PAUSED, PlayerConstants.PlayerState.ENDED -> resetBackground()
                        else -> Unit
                    }
                }
            })
        }
    }

    private fun animateColorOverlayTo(next: Int) {
        overlayAnimator?.cancel()
        val current = overlayAnimator?.animatedValue as? Int ?: DetailsViewModel.DEFAULT_OVERLAY_COLOR
        overlayAnimator = ValueAnimator.ofArgb(current, next)
        overlayAnimator?.apply {
            duration = 250L
            addUpdateListener {
                setOverlayColors(it.animatedValue as Int)
            }
        }?.start()
    }

    private fun setOverlayColors(color: Int) {
        binding.overlay.setBackgroundColor(color)
        binding.btnMarkWatched.setBackgroundColor(color)
        binding.btnWarnMe.setBackgroundColor(color)
    }

    override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        // prevent start animations from running after
        if (animationsRun) return
        animationsRun = true

        val set = AnimationSet(false)
        val translation = TranslateAnimation(0f, 0f, 800f, 0f).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = 350
        }
        val fade = AlphaAnimation(0f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = 250
            startOffset = 100
        }
        set.addAnimation(translation)
        set.addAnimation(fade)
        set.startOffset = 250
        binding.detailsAnimGroup.startAnimation(set)
        set.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        uiViewModel.showBottomNav()
        binding.root.windowInsetsControllerCompat?.isAppearanceLightStatusBars = true
    }

    private fun dimBackground() {
        (binding.overlayDark.layoutParams as? CoordinatorLayout.LayoutParams)?.run {
            (behavior as? ScrollingAlphaBehavior)?.dimBackground()
        }
    }

    private fun resetBackground() {
        (binding.overlayDark.layoutParams as? CoordinatorLayout.LayoutParams)?.run {
            (behavior as? ScrollingAlphaBehavior)?.returnBackground()
        }
    }
}
