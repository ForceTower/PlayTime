package dev.forcetower.playtime.widget.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.SparseArray
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.withTranslation
import androidx.core.util.containsKey
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.ceil

class ReleaseMonthSeparatorItemDecoration(
    context: Context,
    indexed: ReleaseDayIndexed
) : RecyclerView.ItemDecoration() {
    private val labels: SparseArray<StaticLayout>
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private val textWidth: Int
    private val decorHeight: Int
    private val verticalBias: Float

    init {
        val attrs = context.obtainStyledAttributes(
            R.style.Widget_PlayTime_ReleaseMonthSeparatorItemDecoration,
            R.styleable.ReleaseMonthSeparatorItemDecoration
        )

        val textSize =
            attrs.getDimension(R.styleable.ReleaseMonthSeparatorItemDecoration_android_textSize, paint.textSize)
        paint.textSize = textSize
        try {
            paint.typeface = ResourcesCompat.getFont(
                context,
                attrs.getResourceIdOrThrow(R.styleable.ReleaseMonthSeparatorItemDecoration_android_fontFamily)
            )
        } catch (ignored: Exception) {
        }

        val textColor =
            attrs.getColor(R.styleable.ReleaseMonthSeparatorItemDecoration_android_textColor, Color.BLACK)
        paint.color = textColor

        textWidth =
            attrs.getDimensionPixelSizeOrThrow(R.styleable.ReleaseMonthSeparatorItemDecoration_android_width)
        val height =
            attrs.getDimensionPixelSizeOrThrow(R.styleable.ReleaseMonthSeparatorItemDecoration_android_height)
        val minHeight = ceil(textSize).toInt()
        decorHeight = height.coerceAtLeast(minHeight)

        verticalBias = attrs.getFloat(R.styleable.ReleaseMonthSeparatorItemDecoration_verticalBias, 0.5f)
            .coerceIn(0f, 1f)

        attrs.recycle()

        labels = buildLabels(indexed)
    }

    private fun buildLabels(
        indexer: ReleaseDayIndexed
    ): SparseArray<StaticLayout> {
        val sparseArray = SparseArray<StaticLayout>()
        for (day in indexer.days) {
            val position = indexer.positionForDay(day)
            val text = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault()).format(day).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val label = newStaticLayout(text, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
            sparseArray.put(position, label)
        }
        return sparseArray
    }

    override fun getItemOffsets(outRect: Rect, child: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(child)
        outRect.top = if (labels.containsKey(position)) decorHeight else 0
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return
        val centerX = parent.width / 2f

        parent.forEach { child ->
            if (child.top < parent.height && child.bottom > 0) {
                val layout = labels[parent.getChildAdapterPosition(child)]
                if (layout != null) {
                    val dx = centerX - (layout.width / 2)
                    val dy = layoutManager.getDecoratedTop(child) +
                        child.translationY +
                        (decorHeight - layout.height) * verticalBias
                    canvas.withTranslation(x = dx, y = dy) {
                        layout.draw(this)
                    }
                }
            }
        }
    }

    companion object {
        fun newStaticLayout(
            source: CharSequence,
            paint: TextPaint,
            width: Int,
            alignment: Layout.Alignment,
            spacingmult: Float,
            spacingadd: Float,
            includepad: Boolean
        ): StaticLayout {
            return StaticLayout.Builder.obtain(source, 0, source.length, paint, width).apply {
                setAlignment(alignment)
                setLineSpacing(spacingadd, spacingmult)
                setIncludePad(includepad)
            }.build()
        }
    }
}
