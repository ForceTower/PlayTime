package dev.forcetower.playtime.view.details

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.forcetower.playtime.core.model.ui.MovieFeatured
import dev.forcetower.playtime.databinding.FragmentMovieDetailsBinding
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentMovieDetailsBinding

    private val movie = MovieFeatured(
        1,
        "Star Wars: A Ascenção Skywalker",
        "2017/04/20",
        "https://image.tmdb.org/t/p/original/3mTyM2kywFuDmq3RQa2TFIpVCHt.jpg",
        "https://image.tmdb.org/t/p/original/jOzrELAzFxtMx2I4uDGHOotdfsS.jpg",
        "Com o retorno do Imperador Palpatine, todos voltam a temer seu poder e, com isso, a Resistência toma a frente da batalha que ditará os rumos da galáxia. Treinando para ser uma completa Jedi, Rey (Daisy Ridley) ainda se encontra em conflito com seu passado e futuro, mas teme pelas respostas que pode conseguir a partir de sua complexa ligação com Kylo Ren (Adam Driver), que também se encontra em conflito pela Força."
    )

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
                val dominant = palette.getDominantColor(Color.BLACK)
                val dominantAlpha = ColorUtils.setAlphaComponent(dominant, 0xB2)
                binding.overlay.setBackgroundColor(dominantAlpha)


                val vibrant = palette.getDarkVibrantColor(Color.BLACK)
                val vibrantAlpha = ColorUtils.setAlphaComponent(vibrant, 0XF0)
                binding.btnWatchTrailer.setBackgroundColor(vibrantAlpha)
            } else {
                val alpha = ColorUtils.setAlphaComponent(Color.BLACK, 0xB2)
                binding.overlay.setBackgroundColor(alpha)
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
    ): View? {
        return FragmentMovieDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.listener = listener
            binding.movie = movie
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("View created...")
    }
}