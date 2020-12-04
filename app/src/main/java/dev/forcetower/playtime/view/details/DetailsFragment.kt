package dev.forcetower.playtime.view.details

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.util.PaletteUtils.getFirstNonBright
import dev.forcetower.playtime.databinding.FragmentMovieDetailsBinding
import dev.forcetower.playtime.widget.behavior.ScrollingAlphaBehavior
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.windowInsetsControllerCompat
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var imagesAdapter: ImagesAdapter
    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel: DetailsViewModel by viewModels()
    private var videoLoaded = false

    private val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            if (resource is BitmapDrawable) {
                val palette = Palette.from(resource.bitmap).generate()

                val dominant = palette.getFirstNonBright()
                val dominantAlpha = ColorUtils.setAlphaComponent(dominant, 0xB2)
                binding.overlay.setBackgroundColor(dominantAlpha)
                binding.btnWatchTrailer.setBackgroundColor(dominantAlpha)
            } else {
                val alpha = ColorUtils.setAlphaComponent(Color.BLACK, 0xB2)
                binding.overlay.setBackgroundColor(alpha)
                binding.btnWatchTrailer.setBackgroundColor(alpha)
            }
            return false
        }
    }

    private val posterListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition(500L, TimeUnit.MILLISECONDS)

        val transition = TransitionSet()
            .addTransition(ChangeBounds().apply {
                pathMotion = ArcMotion()
            })
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
        val view =  FragmentMovieDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.listener = listener
            binding.posterListener = posterListener
            binding.lastImage = args.lastImage
        }.root

        imagesAdapter = ImagesAdapter()
        binding.recyclerImages.apply {
            adapter = imagesAdapter
            itemAnimator?.run {
                changeDuration = 0L
            }
        }

        binding.up.setOnClickListener { findNavController().popBackStack() }
        binding.cover.transitionName = getString(R.string.transition_movie_poster, args.movieId)
        lifecycle.addObserver(binding.youtubePlayerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.cover, getString(R.string.transition_movie_poster, args.movieId))
        viewModel.movie(args.movieId).observe(viewLifecycleOwner) {
            binding.value = it

            imagesAdapter.submitList(it.images.filter { img -> img.type == 0 }.sortedByDescending { img -> img.voteAverage }.take(4))

            if (!videoLoaded) {
                val video = it.videos.firstOrNull { vid -> vid.site == "YouTube" }
                if (video != null) {
                    videoLoaded = true
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

            binding.executePendingBindings()
        }

        viewModel.releaseDate(args.movieId).observe(viewLifecycleOwner) {
            binding.release = it
            binding.executePendingBindings()
        }
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