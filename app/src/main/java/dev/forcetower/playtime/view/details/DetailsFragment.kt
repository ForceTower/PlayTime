package dev.forcetower.playtime.view.details

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.core.model.ui.MovieSimpleUI
import dev.forcetower.playtime.core.util.PaletteUtils.getFirstNonBright
import dev.forcetower.playtime.databinding.FragmentMovieDetailsBinding
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var imagesAdapter: ImagesAdapter
    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel: DetailsViewModel by viewModels()

    private val listener = object : RequestListener<Drawable> {
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
            startPostponedEnterTransition()
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  FragmentMovieDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.listener = listener
        }.root

        imagesAdapter = ImagesAdapter()
        binding.recyclerImages.apply {
            adapter = imagesAdapter
            itemAnimator?.run {
                changeDuration = 0L
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movie(args.movieId).observe(viewLifecycleOwner) {
            Timber.d("Updated data: $it")
            binding.value = it
            imagesAdapter.submitList(it.images.filter { img -> img.type == 0 }.sortedByDescending { img -> img.voteAverage }.take(4))
        }

        viewModel.releaseDate(args.movieId).observe(viewLifecycleOwner) {
            binding.release = it
        }
    }
}