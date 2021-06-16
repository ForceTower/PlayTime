package dev.forcetower.playtime.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestListener
import dev.forcetower.toolkit.bindings.imageUrl

@BindingAdapter(
    value = [
        "tmdbUrl",
        "clipCircle",
        "listener",
        "dontTransform",
        "blurImage",
        "useBlurSupport",
        "blurRadius",
        "blurSampling",
        "crossFade",
        "fallbackResource",
        "configWidthDivider",
        "elevationAfterLoad"
    ],
    requireAll = false
)
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
    crossFade: Boolean?,
    fallbackResource: Int?,
    configWidthDivider: Int?,
    elevationAfterLoad: Int?
) {
    val context = imageView.context
    val divider = configWidthDivider ?: 1
    val config = when (context.resources.displayMetrics.widthPixels / divider) {
        in 0..500 -> "w300"
        in 501..1000 -> "w780"
        in 1001..1600 -> "w1280"
        else -> "original"
    }
    if (tmdbUrl == null) {
        imageUrl(imageView, "https://image.freepik.com/vetores-gratis/erro-com-efeito-de-falha-na-tela-erro-404-pagina-nao-encontrada_143407-1.jpg", null, clipCircle, listener, dontTransform, blurImage, useBlurSupport, blurRadius, blurSampling, fallbackResource, crossFade, elevationAfterLoad)
    } else {
        imageUrl(
            imageView,
            "https://image.tmdb.org/t/p/${config}$tmdbUrl",
            null,
            clipCircle,
            listener,
            dontTransform,
            blurImage,
            useBlurSupport,
            blurRadius,
            blurSampling,
            fallbackResource,
            crossFade,
            elevationAfterLoad
        )
    }
}
