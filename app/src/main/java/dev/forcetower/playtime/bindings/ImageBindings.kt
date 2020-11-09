package dev.forcetower.playtime.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestListener
import dev.forcetower.toolkit.bindings.imageUrl
import timber.log.Timber

@BindingAdapter(value = [
    "tmdbUrl",
    "clipCircle",
    "listener",
    "dontTransform",
    "blurImage",
    "useBlurSupport",
    "blurRadius",
    "blurSampling",
    "crossFade"
], requireAll = false)
fun tmdbUrl(
    imageView: ImageView,
    tmdbUrl: String?,
    clipCircle: Boolean?,
    listener: RequestListener<Drawable>?,
    dontTransform: Boolean?,
    blurImage: Boolean?,
    useBlurSupport: Boolean?,
    blurRadius: Int?,
    blurSampling: Int?,
    crossFade: Boolean?
) {
    val context = imageView.context
    val config = when (context.resources.displayMetrics.widthPixels) {
        in 0..500 -> "w300"
        in 501..1000 -> "w780"
        in 1001..1600 -> "w1280"
        else -> "original"
    }
    if (tmdbUrl == null) {
        imageUrl(imageView, "https://image.freepik.com/vetores-gratis/erro-com-efeito-de-falha-na-tela-erro-404-pagina-nao-encontrada_143407-1.jpg", null, clipCircle, listener, dontTransform, blurImage, useBlurSupport, blurRadius, blurSampling, crossFade)
    } else {
        imageUrl(
            imageView,
            "https://image.tmdb.org/t/p/${config}${tmdbUrl}",
            null,
            clipCircle,
            listener,
            dontTransform,
            blurImage,
            useBlurSupport,
            blurRadius,
            blurSampling,
            crossFade
        )
    }
}